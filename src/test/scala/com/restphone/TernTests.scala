package com.restphone

import org.scalatest.FunSuite
import org.scalatest.Matchers
import scalaz._
import Scalaz._

class SimpleScalaParserTests extends FunSuite with Matchers {
  //  test("a[b]") {
  //    val result = SimpleScalaParser.parseAll(SimpleScalaParser.fnName, "a[b]")
  //    result.get should be ("a[b]")
  //  }
  //
  //  test("fa: F[A]") {
  //    val result = SimpleScalaParser.parse(SimpleScalaParser.argument, "fa: F[A]")
  //    println(s"${result.get}///")
  //    result.get should === Argument(TypeThingIdentifier("fa"),TypeThing(TypeThingIdentifier("F"),None))
  //  }

  test("F[A]") {
    {
      val result = SimpleScalaParser.parseAll(SimpleScalaParser.scalaIdentifier, "F")
      result.get should ===(SimpleScalaParser.TypeThingIdentifier("F"))
    }
    {
      val result = SimpleScalaParser.parseAll(SimpleScalaParser.typeThing, "F[A]")
      result.get should ===("F[A]")
    }
  }
  //  
  //  test("(a: Int)") {
  //    val result = SimpleScalaParser.parseAll(SimpleScalaParser.argumentList, "(a: Int)")
  //    println(s"${result.get}///")
  //    result.get should === ("a: int")
  //  }
  //  
  //  test("def map[A, B](fa: F[A])(f: A => B): F[B]") {
  //    val result = SimpleScalaParser.parse(SimpleScalaParser.fullDefinition, "def map[A, B](fa: F[A])(f: A => B): F[B]")
  //    println(s"${result.get}///")
  //    result.get should === ("def map[A, B](fa: F[A])(f: A => B): F[B]")
  //  }
  //  
  //  test("argument:: F[A]") {
  //    val result = SimpleScalaParser.parse(SimpleScalaParser.argumentList, "fa: ")
  //    println(s"${result.get}///")
  //    result.get should === ("fa: F[A]")
  //  }
}
