package com.ilyamur.scalaseqrenamer.projector

object SemanticAnalyzer {

  def apply(tree: ParsingTree): ParsingTree = {
    val nums = tree.list
    if (nums != nums.sorted) {
      throw new IllegalArgumentException("Pattern is not correct (ordering): " + tree)
    }
    tree
  }
}
