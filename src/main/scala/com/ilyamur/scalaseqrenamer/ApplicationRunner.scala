package com.ilyamur.scalaseqrenamer

class ApplicationRunner(executionPlanCreator: ExecutionPlanCreator,
                        executionPlanRunner: ExecutionPlanRunner) {

  def run(input: String, output: String, pattern: String): Unit = {
    val plan = executionPlanCreator.create(input, output, Some(pattern))
    executionPlanRunner.run(plan)
  }
}
