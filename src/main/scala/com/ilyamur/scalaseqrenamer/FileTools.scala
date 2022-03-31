package com.ilyamur.scalaseqrenamer

import java.io.IOException
import java.nio.file.{CopyOption, FileSystem, Files, StandardCopyOption}

class FileTools(fileSystem: FileSystem) {

  def normalize(p: String): String = {
    p.replace("\\", "/")
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
    Files.copy(srcPath, trgPath, StandardCopyOption.REPLACE_EXISTING)
  }

  def delete(trg: String): Unit = {
    val trgPath = fileSystem.getPath(trg)
    Files.delete(trgPath)
  }
}
