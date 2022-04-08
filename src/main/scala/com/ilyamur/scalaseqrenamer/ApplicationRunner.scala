package com.ilyamur.scalaseqrenamer

import org.slf4j.LoggerFactory

import java.time.Clock

class ApplicationRunner(executionPlanCreator: ExecutionPlanCreator,
                        executionPlanRunner: ExecutionPlanRunner) {

  def run(input: String, output: String, pattern: String): Unit = {
    val plan = executionPlanCreator.create(input, output, Some(pattern))
    executionPlanRunner.run(plan)
  }
}
