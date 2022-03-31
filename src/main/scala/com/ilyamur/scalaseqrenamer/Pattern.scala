package com.ilyamur.scalaseqrenamer

import com.google.common.base.Preconditions.checkArgument
import org.apache.commons.lang3.StringUtils

object Pattern {

  def apply(pattern: String, min: Int, max: Int): Pattern = {
    try {
      val parts = pattern.split(":")
      if (parts.size != 2) {
        throw new RuntimeException(s"Wrong pattern: $pattern")
      }
      if (parts(0).isEmpty) {
        val period = parts(1).toInt
        new Pattern(PeriodToken(period), min, max)
      } else {
        val start = parts(0).toInt
        val period = parts(1).toInt
        new Pattern(StartPeriodToken(start, period), min, max)
      }
    } catch {
      case _: NumberFormatException =>
        throw new RuntimeException(s"Wrong pattern: $pattern")
    }
  }

  def direct: Pattern = {
    new Pattern(PeriodToken(1), 1, Int.MaxValue)
  }
}

class Pattern private(token: Token, min: Int, max: Int) {

  def project(targetIndex: Int): Int = {
    token match {
      case PeriodToken(period) =>
        project(targetIndex, 1, period)
      case StartPeriodToken(start, period) =>
        project(targetIndex, start, period)
    }
  }

  private def project(targetIndex: Int, start: Int, period: Int): Int = {
    checkArgument(start > 0, s"Wrong staring index: $start": Any)
    checkArgument(period > 0, s"Wrong period: $period": Any)
    if (targetIndex >= max) {
      max
    } else {
      val seed = start % period
      val t = targetIndex - seed + period
      val p = targetIndex - t % period
      if (p < min) min else p
    }
  }
}
