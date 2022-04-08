package com.ilyamur.scalaseqrenamer

import com.google.common.hash.{HashCode, Hashing}
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.lang3.time.StopWatch
import org.slf4j.LoggerFactory

import java.io.{Closeable, IOException, InputStream}
import java.nio.file.*

class FileTools(fileSystem: FileSystem) {

  private val LOG = LoggerFactory.getLogger(classOf[FileTools])

  private val swCompare = StopWatch.create()
  private val swCopy = StopWatch.create()
  private val swDelete = StopWatch.create()

  private def inStopWatch[R](sw: StopWatch)(code: => R): R = {
    if (!sw.isStarted) {
      sw.start()
    } else {
      sw.resume()
    }
    val result = code
    sw.suspend()
    result
  }

  def logStopWatch(sw: StopWatch, msg: String): Unit = {
    if (!sw.isStopped) {
      sw.stop()
    }
    LOG.info(s"$msg: ${sw.getTime} ms")
  }

  def logReport(): Unit = {
    logStopWatch(swCompare, "Comparing")
    logStopWatch(swCopy, "Copying")
    logStopWatch(swDelete, "Deleting")
  }

  def normalize(p: String): String = {
    p.replace("\\", "/")
  }

  private def usingResource[C <: Closeable, R](c: C)(code: C => R): R = {
    try {
      code(c)
    } finally {
      c.close()
    }
  }

  def copy(src: String, trg: String): Unit = {
    val srcPath = fileSystem.getPath(normalize(src))
    val trgPath = fileSystem.getPath(normalize(trg))
    try {
      Files.createDirectories(trgPath.getParent)
    } catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }

    if (Files.exists(trgPath)) {
      val isSameContent = usingResource(Files.newInputStream(srcPath)) { srcIs =>
        usingResource(Files.newInputStream(trgPath)) { trgIs =>
          contentEquals(srcPath, trgPath)
        }
      }
      if (!isSameContent) {
        internalCopy(srcPath, trgPath)
      }
    } else {
      internalCopy(srcPath, trgPath)
    }
  }

  private def contentEquals(srcPath: Path, trgPath: Path) = {
    inStopWatch(swCompare) {
      try {
        FileUtils.contentEquals(srcPath.toFile, trgPath.toFile)
      } catch {
        case _: UnsupportedOperationException =>
          false
      }
    }
  }

  private def internalCopy(srcPath: Path, trgPath: Path): Unit = {
    inStopWatch(swCopy) {
      Files.copy(srcPath, trgPath, StandardCopyOption.REPLACE_EXISTING)
    }
  }

  def delete(trg: String): Unit = {
    val trgPath = fileSystem.getPath(trg)
    inStopWatch(swDelete) {
      Files.delete(trgPath)
    }
  }
}
