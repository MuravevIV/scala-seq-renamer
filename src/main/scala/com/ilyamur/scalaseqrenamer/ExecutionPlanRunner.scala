package com.ilyamur.scalaseqrenamer

import org.apache.commons.lang3.time.StopWatch
import org.slf4j.LoggerFactory

import java.time.Clock

class ExecutionPlanRunner(fileTools: FileTools) {

  private val LOG = LoggerFactory.getLogger(classOf[ApplicationRunner])

  def run(plan: ExecutionPlan): Unit = {
    LOG.info(s"Plan: ${plan.actions.size} files")

    val sw = StopWatch.create()
    sw.start()

    plan.actions.foreach {
      case Create(src, trg) =>
        fileTools.copy(src.path, trg.path)
      case Overwrite(src, trg) =>
        fileTools.copy(src.path, trg.path)
      case Delete(trg) =>
        fileTools.delete(trg.path)
    }

    sw.stop()
    LOG.info(s"Plan: ${sw.getTime} ms")
    
    fileTools.logReport()
  }
}
