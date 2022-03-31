package com.ilyamur.scalaseqrenamer

import picocli.CommandLine
import picocli.CommandLine.{Command, Option}

object Application {

  def main(args: Array[String]): Unit = {
    val application = Application()
    new CommandLine(application).execute(args: _*);
  }
}

@Command(version = Array("1.0.0-SNAPSHOT"), mixinStandardHelpOptions = true)
class Application extends Runnable {

  @Option(names = Array("-i", "--input"), required = true, description = Array("Input directory"))
  private var input: String = _

  @Option(names = Array("-o", "--output"), required = true, description = Array("Output directory"))
  private var output: String = _

  @Option(names = Array("-p", "--pattern"), required = true, description = Array("Execution pattern"))
  private var pattern: String = _

  private val applicationModule: ApplicationModule = new ApplicationModule() {}

  override def run(): Unit = {
    applicationModule.applicationRunner.run(input, output, pattern)
  }
}
