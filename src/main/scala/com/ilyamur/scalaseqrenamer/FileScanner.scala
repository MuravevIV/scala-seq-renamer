package com.ilyamur.scalaseqrenamer

import io.github.azagniotov.matcher.AntPathMatcher
import org.apache.commons.lang3.StringUtils

import java.io.IOException
import java.nio.file.{FileSystem, FileSystems, Files, Path, Paths}
import java.util.concurrent.TimeUnit
import java.util.stream.{Collectors, Stream}
import scala.jdk.CollectionConverters.*

class FileScanner(fileSystem: FileSystem,
                  fileMatcher: FileMatcher,
                  fileTools: FileTools) {

  def scan(pattern: String): FileListId = {
    val nearestDirectory = fileMatcher.getNearestDir(pattern)
    val path = fileSystem.getPath(nearestDirectory)
    if (!Files.exists(path)) {
      return FileListId(List.empty)
    }
    val walk = Files.walk(path)
    try {
      val itemList = walk
        .filter(p => !Files.isDirectory(p))
        .filter(f => fileMatcher.isMatch(pattern, fileTools.normalize(f.toString)))
        .map(mapToFileListEntity)
        .collect(Collectors.toList)
        .asScala.toList

      FileListId(itemList)
    } catch {
      case e: IOException =>
        throw new RuntimeException(e)
    } finally {
      if (walk != null) {
        walk.close()
      }
    }
  }

  private def mapToFileListEntity(f: Path): ExistingFileId = {
    val pos = getFilePos(f)
    val path = fileTools.normalize(f.toString)
    val mod = getLastModified(f)
    ExistingFileId(pos, path, mod)
  }

  private def getFilePos(f: Path) = {
    val fileName = f.getFileName.toString
    val pureFileName = StringUtils.substringBeforeLast(fileName, ".")
    try {
      pureFileName.toInt
    } catch {
      case e: NumberFormatException =>
        throw new RuntimeException("Cannot parse file name into number: " + fileName, e)
    }
  }

  private def getLastModified(f: Path) = {
    try {
      Files.getLastModifiedTime(f).to(TimeUnit.MILLISECONDS)
    } catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }
  }
}
