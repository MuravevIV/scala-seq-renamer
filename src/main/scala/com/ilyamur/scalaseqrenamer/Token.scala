package com.ilyamur.scalaseqrenamer

sealed trait Token

case class PeriodToken(period: Int) extends Token

case class StartPeriodToken(start: Int, period: Int) extends Token
