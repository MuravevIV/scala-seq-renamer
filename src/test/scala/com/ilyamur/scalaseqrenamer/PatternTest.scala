package com.ilyamur.scalaseqrenamer

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class PatternTest {

  @Test
  def testPatternWithJumpToken(): Unit = {
    val pattern = Pattern(":4", 1, 10)

    assertThat(pattern.project(1)).isEqualTo(1)
    assertThat(pattern.project(2)).isEqualTo(1)
    assertThat(pattern.project(3)).isEqualTo(1)
    assertThat(pattern.project(4)).isEqualTo(1)
    assertThat(pattern.project(5)).isEqualTo(5)
    assertThat(pattern.project(6)).isEqualTo(5)
  }

  @Test
  def testPatternWithStartJumpToken(): Unit = {
    val pattern = Pattern("7:4", 1, 20)

    assertThat(pattern.project(1)).isEqualTo(1)
    assertThat(pattern.project(2)).isEqualTo(1)
    assertThat(pattern.project(3)).isEqualTo(3)
    assertThat(pattern.project(4)).isEqualTo(3)
    assertThat(pattern.project(5)).isEqualTo(3)
    assertThat(pattern.project(6)).isEqualTo(3)
    assertThat(pattern.project(7)).isEqualTo(7)
    assertThat(pattern.project(8)).isEqualTo(7)
    assertThat(pattern.project(9)).isEqualTo(7)
    assertThat(pattern.project(10)).isEqualTo(7)
    assertThat(pattern.project(11)).isEqualTo(11)
    assertThat(pattern.project(12)).isEqualTo(11)
  }

  @Test
  def testPatternWithStartJumpTokenWhenMinMax(): Unit = {
    val pattern = Pattern("5:4", 3, 7)

    assertThat(pattern.project(3)).isEqualTo(3)
    assertThat(pattern.project(4)).isEqualTo(3)
    assertThat(pattern.project(5)).isEqualTo(5)
    assertThat(pattern.project(6)).isEqualTo(5)
    assertThat(pattern.project(7)).isEqualTo(7)
  }
}
