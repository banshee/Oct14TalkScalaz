package com.restphone.worksheet

object Tern2 {
  val q = com.restphone.Tester.q                  //> got parsed: {"!name":"mylibrary","!define":{"point":{"x":"number","y":"number
                                                  //| "}}}
                                                  //| add to element: None {"!name":"mylibrary","!define":{"point":{"x":"number","y
                                                  //| ":"number"}}} [TernElement identifier="None" type="None"]" children=List() cl
                                                  //| ass play.api.libs.json.JsObject
                                                  //| match pair: None {"!name":"mylibrary","!define":{"point":{"x":"number","y":"n
                                                  //| umber"}}}
                                                  //| use None for identifier
                                                  //| add to element: Some(!name) "mylibrary" [TernElement identifier="None" type="
                                                  //| None"]" children=List() class play.api.libs.json.JsString
                                                  //| match pair: Some(!name) "mylibrary"
                                                  //| after adding to element: [TernElement identifier="None" type="None"]" childre
                                                  //| n=List()
                                                  //| add to element: Some(!define) {"point":{"x":"number","y":"number"}} [TernElem
                                                  //| ent identifier="None" type="None"]" children=List() class play.api.libs.json.
                                                  //| JsObject
                                                  //| match pair: Some(!define) {"point":{"x":"number","y":"number"}}
                                                  //| add to element: Some(point) {"x":"number","y":"number"} [TernElement identifi
                                                  //| er="None" type="None"]" children=List() class play.api.libs.json.JsObject
                                                  //| match pair: Some(point) {"x":"number","y":"number"}
                                                  //| use Some(point) for identifier
                                                  //| add to element: Some(x) "number" [TernElement identifier="Some(point)" type="
                                                  //| None"]" children=List() class play.api.libs.json.JsString
                                                  //| match pair: Some(x) "number"
                                                  //| parsing: Some(x) number
                                                  //| after adding to element: [TernElement identifier="Some(x)" type="Some(JsNumbe
                                                  //| rType)"]" children=List()
                                                  //| add to element: Some(y) "number" [TernElement identifier="Some(x)" type="Some
                                                  //| (JsNumberType)"]" children=List() class play.api.libs.json.JsString
                                                  //| match pair: Some(y) "number"
                                                  //| parsing: Some(y) number
                                                  //| after adding to element: [TernElement identifier="Some(y)" type="Some(JsNumbe
                                                  //| rType)"]" children=List()
                                                  //| after adding to element: [TernElement identifier="None" type="None"]" childre
                                                  //| n=List([TernElement identifier="Some(y)" type="Some(JsNumberType)"]" children
                                                  //| =List())
                                                  //| after adding to element: [TernElement identifier="None" type="None"]" childre
                                                  //| n=List([TernElement identifier="None" type="None"]" children=List([TernElemen
                                                  //| t identifier="Some(y)" type="Some(JsNumberType)"]" children=List()))
                                                  //| after adding to element: [TernElement identifier="None" type="None"]" childre
                                                  //| n=List([TernElement identifier="None" type="None"]" children=List([TernElemen
                                                  //| t identifier="None" type="None"]" children=List([TernElement identifier="Some
                                                  //| (y)" type="Some(JsNumberType)"]" children=List())))
                                                  //| q  : com.restphone.TernElement = [TernElement identifier="None" type="None"]"
                                                  //|  children=List([TernElement identifier="None" type="None"]" children=List([Te
                                                  //| rnElement identifier="None" type="None"]" children=List([TernElement identifi
                                                  //| er="Some(y)" type="Some(JsNumberType)"]" children=List())))
  q.toSeq.mkString("\n")                          //> res0: String = [TernElement identifier="None" type="None"]" children=List([T
                                                  //| ernElement identifier="None" type="None"]" children=List([TernElement identi
                                                  //| fier="None" type="None"]" children=List([TernElement identifier="Some(y)" ty
                                                  //| pe="Some(JsNumberType)"]" children=List())))
                                                  //| [TernElement identifier="None" type="None"]" children=List([TernElement iden
                                                  //| tifier="None" type="None"]" children=List([TernElement identifier="Some(y)" 
                                                  //| type="Some(JsNumberType)"]" children=List()))
                                                  //| [TernElement identifier="None" type="None"]" children=List([TernElement iden
                                                  //| tifier="Some(y)" type="Some(JsNumberType)"]" children=List())
                                                  //| [TernElement identifier="Some(y)" type="Some(JsNumberType)"]" children=List(
                                                  //| )
  q.toSeq flatMap { _.toScala }                   //> res1: List[String] = List("", "", def y(): JsNumberType = ???)
}