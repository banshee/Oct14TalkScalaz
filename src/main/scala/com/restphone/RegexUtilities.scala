package com.restphone

import scala.util.matching.Regex

object RegexMatching {
  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }
}

