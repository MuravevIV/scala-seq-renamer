package com.ilyamur.scalaseqrenamer

import java.nio.file.FileSystem

class ExecutionPlanCreator(fileSystem: FileSystem,
                           fileScanner: FileScanner,
                           fileMatcher: FileMatcher,
                           fileTools: FileTools) {

  def create(input: String, output: String, optPattern: Option[String] = None): ExecutionPlan = {
    val srcFilesList = fileScanner.scan(input)
    val trgFilesList = fileScanner.scan(output)

    val srcPosList = srcFilesList.files.map(_.pos)
    val trgPosList = trgFilesList.files.map(_.pos)
    val posList = (srcPosList ::: trgPosList).distinct.sorted

    val pattern = optPattern match {
      case Some(str) =>
        val min = if (srcPosList.nonEmpty) srcPosList.min else 1
        val max = if (srcPosList.nonEmpty) srcPosList.max else Int.MaxValue
        Pattern(str, min, max)
      case _ =>
        Pattern.direct
    }

    val actions: List[FileAction] = posList.map { pos =>
      val optTrgFile = trgFilesList.files.find(_.pos == pos)
      optTrgFile match {
        case Some(trgFile) =>
          val srcPos = pattern.project(pos)
          val optSrcFile = srcFilesList.files.find(_.pos == srcPos)
          optSrcFile match {
            case Some(srcFile) =>
              Overwrite(srcFile, trgFile)
            case _ =>
              Delete(trgFile)
          }
        case _ =>
          val srcPos = pattern.project(pos)
          val optSrcFile = srcFilesList.files.find(_.pos == srcPos)
          optSrcFile match {
            case Some(srcFile) =>
              val optTemplateFile = srcFilesList.files.find(_.pos == pos)
              optTemplateFile match {
                case Some(templateFile) =>
                  create(srcFile, templateFile, output)
                case _ =>
                  throw new IllegalStateException("Source file not found: " + pos)
              }
            case _ =>
              // should be unreachable
              throw new IllegalStateException()
          }
      }
    }

    ExecutionPlan(actions)
  }

  private def create(srcFile: FileId, templateFile: FileId, output: String): Create = {
    val trgPath = createTargetPath(templateFile, output)
    val trgFile = InitFileId(templateFile.pos, trgPath)
    Create(srcFile, trgFile)
  }

  private def createTargetPath(templateFile: FileId, output: String): String = {
    val srcPath = fileSystem.getPath(templateFile.path)
    val fileName = srcPath.getFileName.toString
    val baseOutputDir = fileMatcher.getNearestDir(output)
    val trgPath = fileSystem.getPath(baseOutputDir, fileName)
    fileTools.normalize(trgPath.toString)
  }
}
