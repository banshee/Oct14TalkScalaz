package com.restphone

import scala.util.parsing.combinator._
import scala.PartialFunction._
import scalaz.std.option.optionSyntax._
import play.api.libs.json._
import scalaz.std.AllInstances._
import scalaz._
import scalaz.Tree._
import Scalaz._

import scala.util.parsing.combinator._
import scala.PartialFunction._
import scalaz.std.option.optionSyntax._
import play.api.libs.json._

object IsJsString {
  def unapply(j: JsString): Option[String] = {
    Some(j.as[String])
  }
}

object JsonScalazTree {
  case class JsonNode(s: String, v: JsValue)
  
  def pairToTree(jsObjectProperty: (String, JsValue)): scalaz.Tree[JsonNode] = {
    jsObjectProperty match {
      case (s, j: JsObject) => node(JsonNode(s, j), j.fields.toStream map pairToTree)
      case (s, j: JsValue)  => leaf(JsonNode(s, j))
    }
  }
  
  implicit val showJsonNode: scalaz.Show[JsonNode] = new scalaz.Show[JsonNode] {
    override def show(n: JsonNode) = n.toString
  }
  
  implicit val showTernElement: scalaz.Show[TernElement] = new scalaz.Show[TernElement] {
    override def show(n: TernElement) = n.toString
  }
  
  implicit def showDisjunction[A, B]: scalaz.Show[A \/ B] = new scalaz.Show[A \/ B] {
    override def show(x: A \/ B) = x match {
      case \/-(success) => s"Success: $success"
      case -\/(failure) => s"Failure: $failure"
    }
  }
}