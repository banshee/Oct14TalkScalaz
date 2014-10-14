package com.restphone

import scala.util.parsing.combinator._
import scala.PartialFunction._
import scalaz.std.option.optionSyntax._
import play.api.libs.json._
import RegexMatching._
import scalaz.std.AllInstances._
import scalaz.Tree._
import JsonScalazTree._
import scalaz._
import Scalaz._

object SimpleScalaParser extends SharedParsers {
  // a  plain identifier
  // a => b function
  // (a, b) => c tuple
  // d[e[f]] type parameter
  //
  abstract sealed class RootTypeThing
  case class TypeThing(lhs: RootTypeThing, rhs: Option[RootTypeThing]) extends RootTypeThing
  case class Tuple(items: List[RootTypeThing]) extends RootTypeThing
  case class TypeParameter(items: List[RootTypeThing]) extends RootTypeThing
  case class TypeThingIdentifier(s: String) extends RootTypeThing
  case class Argument(name: TypeThingIdentifier, ternType: TypeThing) extends RootTypeThing
  case class ArgumentList(items: List[Argument]) extends RootTypeThing
  case class FullDefinition(name: TypeThingIdentifier, arguments: List[ArgumentList], resultType: Option[RootTypeThing]) extends RootTypeThing

  def typeThing: Parser[TypeThing] = (tuple | typeParameter | scalaIdentifier) ~ ("=>" ~ typeThing).? ^^ 
  {
    case ((x: RootTypeThing) ~ (y: Option[RootTypeThing])) => TypeThing(lhs = x, rhs = y) 
    }
  def scalaIdentifier: Parser[TypeThingIdentifier] = identifier ^^ TypeThingIdentifier
  def tuple: Parser[Tuple] = "(" ~> repsep(typeThing, ",") <~ ")" ^^ Tuple
  def typeParameter: Parser[TypeParameter] = "[" ~> repsep(typeThing, ",") <~ "]" ^^ TypeParameter
  def defOrVal: Parser[String] = ("def" | "val")
  
  // f: F[A]
  def argument: Parser[Argument] = scalaIdentifier ~ (":" ~> typeThing) ^^ { case (i ~ j) => Argument(name = i, ternType = j) }

  // (fa: F[A])(f: A => B)
  def argumentList: Parser[ArgumentList] = "(" ~> repsep(argument, ",") <~ ")" ^^ { i => ArgumentList(i) }

  // map[A, B](fa: F[A])(f: A => B): F[B]
  def thingDeclaration = (scalaIdentifier ~ typeParameter.?) ~ argumentList.* ~ (":" ~> typeThing).? ^^ {
    case a ~ tp ~ args ~ resultType => FullDefinition(name = a, arguments = args, resultType = resultType)
  }

  def fullDefinition = defOrVal ~> thingDeclaration ^^ identity

  //  val r = parse(full, "def map[A, B](fa: F[A])(f: A => B): F[B]")
}
