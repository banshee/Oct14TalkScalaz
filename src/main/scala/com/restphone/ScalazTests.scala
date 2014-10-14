package com.restphone

import scalaz.std.option._
import scalaz.syntax.std.function1._

//object Scalazj
//  extends scalaz.std.FunctionInstances
//  with scalaz.std.OptionInstances // Type class instances for the standard library types
//  with scalaz.syntax.std.ToAllStdOps // syntax associated with standard library types
//
//import Scalazj._

object XmlSamples2 {
  // A => B: A function
  // A => M[B]: A Kleisli arrow
  // M[A] => M[B]: A function wrapped in a monad

  // Just the function
  val double = (x: Int) => x * 2

  // ================================================================================
  // given A => B, return A => M[B]
  val doubleKleisli = double.kleisli[Option]
}
