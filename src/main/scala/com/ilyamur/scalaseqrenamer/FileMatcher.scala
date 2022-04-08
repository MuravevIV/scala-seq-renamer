package com.ilyamur.scalaseqrenamer

import io.github.azagniotov.matcher.AntPathMatcher
import org.apache.commons.lang3.StringUtils

class FileMatcher {

  private val matcher = new AntPathMatcher.Builder().build()

  def isMatch(pattern: String, path: String): Boolean = {
    matcher.isMatch(pattern, path)
  }

  def getNearestDir(pattern: String): String = {
    stringBeforeLast(stringBeforeLast(stringBeforeLast(stringBeforeLast(pattern, "*"), "?"), "/"), "\\")
  }

  private def stringBeforeLast(str: String, separator: String): String = {
    if (str.contains(separator)) StringUtils.substringBeforeLast(str, separator) else str
  }
}
