package com.ilyamur.scalaseqrenamer

import org.apache.commons.lang3.StringUtils

import java.io.IOException
import java.nio.file.{FileSystem, Files, Path, StandardOpenOption}
import java.util.stream.Stream

class FileOpsHelper(fileSystem: FileSystem) {

  def cleanup(): Unit = {
    cleanup(fileSystem.getPath(StringUtils.EMPTY))
  }

  private def cleanup(directory: Path): Unit = {
    val childStream = Files.list(directory)
    try childStream.forEach(this.cleanupOrDelete)
    catch {
      case e: IOException =>
        throw new RuntimeException(e)
    } finally {
      if (childStream != null) {
        childStream.close()
      }
    }
  }

  private def cleanupOrDelete(p: Path): Unit = {
    if (Files.isDirectory(p)) {
      cleanup(p)
    } else {
      try {
        Files.delete(p)
      } catch {
        case e: IOException =>
          throw new RuntimeException(e)
      }
    }
  }

  def createFiles(directory: String, count: Int): Unit = {
    for (i <- 1 to count) {
      val fileName = StringUtils.leftPad(Integer.toString(i), 4, "0")
      val strPath = directory + "/" + fileName + ".jpg"
      createFile(strPath, strPath)
    }
  }

  private def createFile(strPath: String, content: String) = {
    val normStrPath = strPath.replace("/", fileSystem.getSeparator).replace("\\", fileSystem.getSeparator)
    if (normStrPath.contains(fileSystem.getSeparator)) {
      val dirName = StringUtils.substringBeforeLast(normStrPath, fileSystem.getSeparator)
      try {
        Files.createDirectories(fileSystem.getPath(dirName))
      } catch {
        case e: IOException =>
          throw new RuntimeException(e)
      }
    }
    try {
      val file = Files.createFile(fileSystem.getPath(normStrPath))
      if (content.nonEmpty) {
        Files.write(file, content.getBytes, StandardOpenOption.APPEND)
      }
      file
    } catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }
  }
}
