package com.ilyamur.scalaseqrenamer.projector

case class ParsingTree(list: List[Int])

object ParsingTree {

  def apply(s: String): ParsingTree = {
    if (s.isEmpty) {
      return ParsingTree(List.empty)
    }
    val list = s.split("-").map(numerical).toList

    ParsingTree(list)
  }

  private def numerical(s: String): Int = {
    s.toIntOption.getOrElse {
      throw new IllegalArgumentException("Not a valid integer number: " + s)
    }
  }
}
