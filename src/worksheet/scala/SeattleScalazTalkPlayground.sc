package com.restphone.scalaztalk.worksheet

import scalaz._
import Scalaz._
import scalaz.concurrent._
import scalaz.Nondeterminism

object Playground {
  val stringify = (x: Int) => x.toString
  val options = List(1.some, 2.some)
  val op = stringify.point[Option]
  1.some <*> op
  1.some <**> op
}