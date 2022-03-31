package com.ilyamur.scalaseqrenamer

class ExecutionPlanRunner(fileTools: FileTools) {

  def run(plan: ExecutionPlan): Unit = {
    plan.actions.foreach {
      case Create(src, trg) =>
        fileTools.copy(src.path, trg.path)
      case Overwrite(src, trg) =>
        fileTools.copy(src.path, trg.path)
      case Delete(trg) =>
        fileTools.delete(trg.path)
    }
  }
}
