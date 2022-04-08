package com.ilyamur.scalaseqrenamer.projector

class Projector(pattern: String, min: Int, max: Int) {

  def project(index: Int): Int = {
    val tree = SemanticAnalyzer(ParsingTree(pattern))
    if (tree.list.isEmpty) {
      return Math.min(Math.max(min, index), max)
    }
    if (index <= min) {
      return min
    }
    if (max <= index) {
      return max
    }
    tree.list.zip(tree.list.tail).foreach { case (curr, next) =>
      if (curr <= index && index < next) {
        return curr
      }
    }
    index
  }
}
