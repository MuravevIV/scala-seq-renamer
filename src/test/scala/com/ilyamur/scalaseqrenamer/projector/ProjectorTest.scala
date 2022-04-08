package com.ilyamur.scalaseqrenamer.projector

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class ProjectorTest {

  @Test
  def testWhenEmptyString(): Unit = {
    val p = new Projector("", 5, 10)

    assertThat(p.project(4)).isEqualTo(5)
    assertThat(p.project(5)).isEqualTo(5)
    assertThat(p.project(6)).isEqualTo(6)
    assertThat(p.project(9)).isEqualTo(9)
    assertThat(p.project(10)).isEqualTo(10)
    assertThat(p.project(11)).isEqualTo(10)
  }

  @Test
  def testWhenSingleNum(): Unit = {
    val p = new Projector("6", 5, 10)

    assertThat(p.project(4)).isEqualTo(5)
    assertThat(p.project(5)).isEqualTo(5)
    assertThat(p.project(6)).isEqualTo(6)
    assertThat(p.project(9)).isEqualTo(9)
    assertThat(p.project(10)).isEqualTo(10)
    assertThat(p.project(11)).isEqualTo(10)
  }

  @Test
  def testWhenDashNumList(): Unit = {
    val p = new Projector("6-8", 5, 10)

    assertThat(p.project(4)).isEqualTo(5)
    assertThat(p.project(5)).isEqualTo(5)
    assertThat(p.project(6)).isEqualTo(6)
    assertThat(p.project(7)).isEqualTo(6)
    assertThat(p.project(8)).isEqualTo(8)
    assertThat(p.project(9)).isEqualTo(9)
    assertThat(p.project(10)).isEqualTo(10)
    assertThat(p.project(11)).isEqualTo(10)
  }
}
