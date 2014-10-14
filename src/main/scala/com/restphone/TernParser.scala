package com.restphone

import JsonScalazTree._
import RegexMatching._
import play.api.libs.json._
import scala.PartialFunction._
import scala.util.parsing.combinator._
import scalaz._
import Scalaz._
import scalaz.concurrent._

trait SharedParsers extends scala.util.parsing.combinator.RegexParsers {
  // Note that this is wrong - JavaScript identifiers are more complicated than this.
  // This is useful for the particular file I care about though, so it's good enough.
  def identifier: Parser[String] = """[a-zA-Z]([a-zA-Z0-9]|_[a-zA-Z0-9])*""".r
}

sealed abstract class TernType {
  def toScala: String = "rawTernType"
}
case object JsStringType extends TernType
case object JsNumberType extends TernType
case object JsBoolType extends TernType
case object JsQuestionmark extends TernType
case class UserType(t: String) extends TernType
case class JsConstructor(t: String) extends TernType
case class TernArray(xs: TernType) extends TernType
case class TernFunction(parameters: Seq[TernType], result: Option[TernType]) extends TernType {
  override def toScala = {
    val parameterStrings = parameters map { _.toScala }
    parameterStrings mkString ", "
  }
}
case class TernParameter(parameter: String, parameterType: Option[TernType]) extends TernType {
  override def toScala = s"$parameter: ${parameterType getOrElse "xJsAny"}"
}

object TernParser extends scala.util.parsing.combinator.RegexParsers with SharedParsers {
  def constructor: Parser[JsConstructor] = "+" ~> rep1sep(identifier, ".") ^^ { x => JsConstructor(x.mkString(".")) }
  def builtin: Parser[TernType] = ("number" ^^^ JsNumberType) | ("string" ^^^ JsStringType) | ("boolean" ^^^ JsBoolType) | ("bool" ^^^ JsBoolType) | ("?" ^^^ JsQuestionmark)
  def userType: Parser[UserType] = identifier ^^ UserType
  def array: Parser[TernArray] = "[" ~> ternType <~ "]" ^^ TernArray

  def typedParameter: Parser[TernParameter] =
    (identifier <~ "?".?) ~ (":" ~> ternType).? ^^ { case (a ~ b) => TernParameter(a, b) }
  def parameterList: Parser[Seq[TernParameter]] = repsep(typedParameter, ",")
  def functionSignature: Parser[Seq[TernParameter]] = "fn" ~> "(" ~> parameterList <~ ")"
  def function: Parser[TernFunction] = functionSignature ~ ("->" ~> ternType).? ^^ { case (fn ~ optionalResultType) => TernFunction(fn, optionalResultType) }
  def ternType: Parser[TernType] = (builtin | constructor | array | function | userType) <~ "?".?

  def parseJsString(identifier: String, s: String): ParseFailure \/ TernType = {
    val parseResult = TernParser.parseAll(TernParser.ternType, s)
    parseResult.successful match {
      case true  => \/-(parseResult.get)
      case false => -\/(ParseFailure(s"Failed to parse: $identifier / $s"))
    }
  }
}

abstract sealed class TernElement(val identifier: String)
case class TernElementString(i: String, s: String) extends TernElement(i) {
  override def toString() = s
}
case class TernElementType(i: String, t: TernType) extends TernElement(i) {
  override def toString() = t.toString
}
case class TernElementProperties(i: String) extends TernElement(i) {
  override def toString() = s"..."
}

case class ParseFailure(message: String)

object TernElement {
  // We're looking at three different kinds of json pairs -
  //
  //   * identifier => JsString containing a type signature
  //   * identifier => JsString for things like !doc
  //   * identifier => JsObject with parameters
  //
  // We're only going to actually parse the first one.
  def jsonNodeAsTernElement(jsonNode: JsonNode): ParseFailure \/ TernElement = {
    val rawValues: PartialFunction[JsonNode, TernElement] = {
      // JsObject is a record of zero or more identifier/value pairs 
      case (JsonNode(identifier, j: JsObject)) => TernElementProperties(identifier)

      // These special identifiers are always strings
      case (JsonNode(identifier @ ("!name" | "!proto" | "!doc" | "!url" | "!effects"), IsJsString(s))) => TernElementString(identifier, s)
    }

    // andThen is a standard Scala method on PartialFunction
    // \/- looks strange at first, but it's just a case class.
    // Remember that the correct answer is always the thing on the
    // right - the 'right' answer is on the right hand side.
    // There's a corresponding -\/ for failures.
    val rawValuesAsAutomaticSuccesses: PartialFunction[JsonNode, ParseFailure \/ TernElement] = rawValues andThen { \/-(_) }

    val parsedValues: PartialFunction[JsonNode, ParseFailure \/ TernElement] = {
      case (JsonNode(identifier, IsJsString(s))) => TernParser.parseJsString(identifier, s) map { TernElementType(identifier, _) }
    }
    val evaluateBothKinds: PartialFunction[JsonNode, ParseFailure \/ TernElement] = (rawValuesAsAutomaticSuccesses orElse parsedValues)

    evaluateBothKinds(jsonNode)
  }
}

object Tester {
  //  import java.nio.file.Paths
  //  val currentRelativePath = Paths.get("");
  //  val s = currentRelativePath.toAbsolutePath().toString();
  //  System.out.println("Current relative path is: " + s);
  val f = new java.io.File("/Users/james/workspace/TernParser/core.json.full")
  val s = com.google.common.io.Files.toString(f, java.nio.charset.StandardCharsets.UTF_8)
  val coreJsonAsJsObject: JsObject = play.api.libs.json.Json.parse(s).as[JsObject]
}

// !name "mylibrary",
// !type
// !define - definition of a class
// !proto - Element.prototype
// !doc - documentation
// !url - link to doc
// !effects

//object JsonConverter {
//  def asItem(js: JsObject) = {
//    val name = js \ "!name"
//    val define = js \ "!define"
//  }
//}

//{
//  "!name": "mylibrary",
//  "!define": {
//    "point": {
//      "x": "number",
//      "y": "number"
//    }
//  }
//}

object TreeTest {
  // The file we get from Autodesk is called "core.json" and has all the type
  // definitions for, surprise! the core types.
  val coreJson: play.api.libs.json.JsObject = Tester.coreJsonAsJsObject

  // The problem with something that's a play.api.libs.json.JsObject is that you
  // need to know all the various ways to navigate using its native methods.
  // So we're going to ignore virtually all of them and just turn it into a 
  // scalaz.Tree.
  val treeOfJsonNodes: scalaz.Tree[JsonNode] = pairToTree("root", coreJson)

  // That gives us a tree of json identifier/value pairs.  We want to
  // turn that into a tree of TernElements - but that transform isn't
  // guaranteed to work, so it's a ParseFailure \/ TernElement.
  val treeOfPossibleTernElementsUsingApply: Tree[ParseFailure \/ TernElement] =
    Applicative[Tree].apply(treeOfJsonNodes)(TernElement.jsonNodeAsTernElement)

  // It turns out that Scala parser combinators aren't what you'd call speedy.
  // So let's run them in parallel.

  // We have a function that does what we want, it's just that it's A => B,
  // not something involving futures.  So let's transform that function.
  // We know Task is an Applicative, so we know it has a way to take a value
  // and put it into the context of a Task.
  val parserInTask: Task[JsonNode => ParseFailure \/ TernElement] =
    Applicative[Task].point(TernElement.jsonNodeAsTernElement(_))
  val parserInTaskInTree = treeOfJsonNodes map { _ => parserInTask }

  // And we'll put the tree of JsonNodes into a [[Task]]] also
  val treeOfTaskOfJsonNodes: Tree[Task[JsonNode]] =
    Applicative[Tree].map(treeOfJsonNodes)(Applicative[Task].point(_))

  // We now have a Tree[JsonNode] and a functor Task[JsonNode => ParseFailure \/ TernElement].
  // Bind.ap takes a F[A] and a F[A => B] and returns a F[B] - that's what we want.

  // It's doing the same work as the map function on the Tree object:
  //  val treeOfPossibleTernElementsUsingMap = treeOfJsonNodes map TernElement.jsonNodeAsTernElement
}

object SimpleTask {
  val doubler = (x: Int) => x * 2
  val x = doubler(4)

  val doublerWrappedInTask = Applicative[Task].point(doubler)
  val result = Bind[Task].ap(Task(5))(doublerWrappedInTask)

  val t = Task(3) <*> doublerWrappedInTask

  val xs = List(1, 2);
  val xsWrappedInTask: List[Task[Int]] = xs map (Applicative[Task].point(_))

  val ys = xs map { Task(_) <*> doublerWrappedInTask }

  for {
    i <- ys.sequence.run
  } {
    println(s"i: $i")
  }
}

object MyApp {
  val a = TreeTest.treeOfJsonNodes.drawTree
  println(s"$a\nall done")

  val b = TreeTest.treeOfPossibleTernElementsUsingApply.drawTree
  println(s"$a\nall done")
}