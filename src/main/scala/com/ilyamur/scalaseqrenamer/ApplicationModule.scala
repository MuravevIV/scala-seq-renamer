package com.ilyamur.scalaseqrenamer

import java.nio.file.{FileSystem, FileSystems}

trait ApplicationModule {

  import com.softwaremill.macwire.*

  lazy val fileSystem: FileSystem = FileSystems.getDefault
  lazy val fileTools: FileTools = wire[FileTools]
  lazy val fileMatcher: FileMatcher = wire[FileMatcher]
  lazy val fileScanner: FileScanner = wire[FileScanner]
  lazy val executionPlanCreator: ExecutionPlanCreator = wire[ExecutionPlanCreator]
  lazy val executionPlanRunner: ExecutionPlanRunner = wire[ExecutionPlanRunner]
  lazy val applicationRunner: ApplicationRunner = wire[ApplicationRunner]
}
