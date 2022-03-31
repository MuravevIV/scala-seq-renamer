package com.ilyamur.scalaseqrenamer

import com.google.common.jimfs.{Configuration, Jimfs}
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.{BeforeEach, Test}

import java.nio.file.FileSystem

class ExecutionPlanRunnerIT {

  object env extends ApplicationModule {

    import com.softwaremill.macwire.*

    override lazy val fileSystem: FileSystem = Jimfs.newFileSystem(Configuration.windows)
    lazy val fileOpsHelper: FileOpsHelper = wire[FileOpsHelper]
  }

  @BeforeEach
  def beforeEach(): Unit = {
    env.fileOpsHelper.cleanup()
  }

  @Test
  def testRunCreate(): Unit = {
    env.fileOpsHelper.createFiles("input", 1)

    env.executionPlanRunner.run(ExecutionPlan(List(
      Create(ExistingFileId(1, "input/0001.jpg", 0), InitFileId(1, "output/0001.jpg"))
    )))

    val fileList = env.fileScanner.scan("output/*.jpg")
    assertThat(fileList.files.size).isEqualTo(1)
    val file = fileList.files.head
    assertThat(file.pos).isEqualTo(1)
  }

  @Test
  def testRunOverwrite(): Unit = {
    env.fileOpsHelper.createFiles("input", 1)
    env.fileOpsHelper.createFiles("output", 1)

    env.executionPlanRunner.run(ExecutionPlan(List(
      Overwrite(ExistingFileId(1, "input/0001.jpg", 0), ExistingFileId(1, "output/0001.jpg", 0))
    )))

    val fileList = env.fileScanner.scan("output/*.jpg")
    assertThat(fileList.files.size).isEqualTo(1)
    val file = fileList.files.head
    assertThat(file.pos).isEqualTo(1)
    assertThat(file.mod).isGreaterThan(0)
  }

  @Test
  def testRunDelete(): Unit = {
    env.fileOpsHelper.createFiles("output", 1)

    env.executionPlanRunner.run(ExecutionPlan(List(
      Delete(ExistingFileId(1, "output/0001.jpg", 0))
    )))

    val fileList = env.fileScanner.scan("output/*.jpg")
    assertThat(fileList.files.size).isEqualTo(0)
  }
}
