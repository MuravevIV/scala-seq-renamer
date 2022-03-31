package com.ilyamur.scalaseqrenamer

import com.google.common.jimfs.{Configuration, Jimfs}
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.{assertEquals, assertTrue}
import org.junit.jupiter.api.Test

import java.nio.file.FileSystem

class FileScannerIT {

  object env extends ApplicationModule {

    import com.softwaremill.macwire.*

    override lazy val fileSystem: FileSystem = Jimfs.newFileSystem(Configuration.windows)
    lazy val fileOpsHelper: FileOpsHelper = wire[FileOpsHelper]
  }

  @Test
  def testScan(): Unit = {
    env.fileOpsHelper.createFiles("input", 2)

    val fileListId = env.fileScanner.scan("input/*.jpg")

    assertThat(fileListId.files.size).isEqualTo(2)

    val item = fileListId.files.head

    assertThat(item.pos).isEqualTo(1)
    assertThat(item.path).isEqualTo("input/0001.jpg")
    assertThat(item.mod).isGreaterThan(0)
  }
}
