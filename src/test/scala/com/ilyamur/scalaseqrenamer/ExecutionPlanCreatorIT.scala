package com.ilyamur.scalaseqrenamer

import com.google.common.jimfs.{Configuration, Jimfs}
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.{BeforeAll, BeforeEach, Test}

import java.nio.file.FileSystem
import scala.jdk.CollectionConverters.*
import scala.reflect.ClassTag

class ExecutionPlanCreatorIT {

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
  def testCreateWhenNoInputFilesAndOutputFiles(): Unit = {
    val plan = env.executionPlanCreator.create("input/*.jpg", "output/*.jpg")

    assertThat(plan.actions.size).isEqualTo(0)
  }

  @Test
  def testCreateWhenNoInputFilesAndOutputFilesAndPattern(): Unit = {
    val plan = env.executionPlanCreator.create("input/*.jpg", "output/*.jpg", Some(":1"))

    assertThat(plan.actions.size).isEqualTo(0)
  }

  @Test
  def testCreateWhenOnlyInputFile(): Unit = {
    env.fileOpsHelper.createFiles("input", 1)

    val plan = env.executionPlanCreator.create("input/*.jpg", "output/*.jpg")

    assertThat(plan.actions.size).isEqualTo(1)
    verifyCreate(plan, 0, 1 -> 1)
  }

  @Test
  def testCreateWhenOutputFileExists(): Unit = {
    env.fileOpsHelper.createFiles("input", 1)
    env.fileOpsHelper.createFiles("output", 1)

    val plan = env.executionPlanCreator.create("input/*.jpg", "output/*.jpg")

    assertThat(plan.actions.size).isEqualTo(1)
    verifyOverwrite(plan, 0, 1 -> 1)
  }

  @Test
  def testCreateWhenInputFileNotExists(): Unit = {
    env.fileOpsHelper.createFiles("output", 1)

    val plan = env.executionPlanCreator.create("input/*.jpg", "output/*.jpg")
    verifyDelete(plan, 0, 1)
  }

  // ========

  @Test
  def testCreateWhenPattern(): Unit = {
    env.fileOpsHelper.createFiles("input", 3)

    val plan = env.executionPlanCreator.create("input/*.jpg", "output/*.jpg", Some(":2"))

    assertThat(plan.actions.size).isEqualTo(3)
    verifyCreate(plan, 0, 1 -> 1)
    verifyCreate(plan, 1, 1 -> 2)
    verifyCreate(plan, 2, 3 -> 3)
  }

  // ========

  private def verifyDelete(plan: ExecutionPlan, index: Int, trgPos: Int): Unit = {
    val delete = plan.actions(index).as[Delete]
    assertThat(delete.trg.pos).isEqualTo(trgPos)
  }

  private def verifyOverwrite(plan: ExecutionPlan, index: Int, direction: (Int, Int)): Unit = {
    val a = plan.actions(index).as[Overwrite]
    assertThat(a.src.pos).isEqualTo(direction._1)
    assertThat(a.trg.pos).isEqualTo(direction._2)
  }

  private def verifyCreate(plan: ExecutionPlan, index: Int, direction: (Int, Int)): Unit = {
    val a = plan.actions(index).as[Create]
    assertThat(a.src.pos).isEqualTo(direction._1)
    assertThat(a.trg.pos).isEqualTo(direction._2)
  }

  extension (a: Any) {
    def as[T: ClassTag]: T = {
      val classTag = implicitly[ClassTag[T]]
      assertThat(a.getClass).isEqualTo(classTag.runtimeClass)
      a.asInstanceOf[T]
    }
  }
}
