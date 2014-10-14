package com.restphone.worksheet

import com.restphone._

import scalaz.Tree._
import scalaz.std.AllInstances._

import com.restphone.JsonScalazTree._

object TernParser {
  TreeTest.n.drawTree                             //> java.lang.StackOverflowError
                                                  //| 	at com.fasterxml.jackson.core.JsonStreamContext.<init>(JsonStreamContext
                                                  //| .java:43)
                                                  //| 	at com.fasterxml.jackson.core.json.JsonWriteContext.<init>(JsonWriteCont
                                                  //| ext.java:62)
                                                  //| 	at com.fasterxml.jackson.core.json.JsonWriteContext.createRootContext(Js
                                                  //| onWriteContext.java:91)
                                                  //| 	at com.fasterxml.jackson.core.base.GeneratorBase.<init>(GeneratorBase.ja
                                                  //| va:72)
                                                  //| 	at com.fasterxml.jackson.core.json.JsonGeneratorImpl.<init>(JsonGenerato
                                                  //| rImpl.java:95)
                                                  //| 	at com.fasterxml.jackson.core.json.WriterBasedJsonGenerator.<init>(Write
                                                  //| rBasedJsonGenerator.java:74)
                                                  //| 	at com.fasterxml.jackson.core.JsonFactory._createGenerator(JsonFactory.j
                                                  //| ava:1332)
                                                  //| 	at com.fasterxml.jackson.core.JsonFactory.createGenerator(JsonFactory.ja
                                                  //| va:1084)
                                                  //| 	at play.api.libs.json.JacksonJson$.stringJsonGenerator(JsValue.scala:475
                                                  //| )
                                                  //| 	at play.api.libs.json.JacksonJson$.generateFromJsValue(JsValue.scala:491
                                                  //| )
                                                  //| 	at play.api.libs.json.Json$.stringify(Json.scala:51)
                                                  //| 	at play.api.libs.json.JsValue$class.toString(JsValue.scala:80)
                                                  //| 	at play.api.libs.json.JsObject.toString(JsValue.scala:166)
                                                  //| 	at java.lang.String.valueOf(String.java:2854)
                                                  //| 	at scala.collection.mutable.StringBuilder.append(StringBuilder.scala:198
                                                  //| )
                                                  //| 	at scala.collection.TraversableOnce$$anonfun$addString$1.apply(Traversab
                                                  //| leOnce.scala:349)
                                                  //| 	at scala.collection.Iterator$class.foreach(Iterator.scala:743)
                                                  //| 	at scala.collection.AbstractIterator.foreach(Iterator.scala:1177)
                                                  //| 	at scala.collection.TraversableOnce$class.addString(TraversableOnce.scal
                                                  //| a:342)
                                                  //| 	at scala.collection.AbstractIterator.addString(Iterator.scala:1177)
                                                  //| 	at scala.collection.TraversableOnce$class.mkString(TraversableOnce.scala
                                                  //| :308)
                                                  //| 	at scala.collection.AbstractIterator.mkString(Iterator.scala:1177)
                                                  //| 	at scala.runtime.ScalaRunTime$._toString(ScalaRunTime.scala:166)
                                                  //| 	at com.restphone.JsonScalazTree$JsonNode.toString(JsUtilities.scala:22)
                                                  //| 	at com.restphone.JsonScalazTree$$anon$1.show(JsUtilities.scala:32)
                                                  //| 	at com.restphone.JsonScalazTree$$anon$1.show(JsUtilities.scala:31)
                                                  //| 	at scalaz.Show$class.shows(Show.scala:12)
                                                  //| 	at com.restphone.JsonScalazTree$$anon$1.shows(JsUtilities.scala:31)
                                                  //| 	at scalaz.Tree.draw(Tree.scala:51)
                                                  //| 	at scalaz.Tree$$anonfun$scalaz$Tree$$drawSubTrees$1$2.apply(Tree.scala:4
                                                  //| 4)
                                                  //| 	at scalaz.Tree$$anonfun$scalaz$Tree$$drawSubTrees$1$2.apply(Tree.scala:4
                                                  //| 4)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$append$1.apply(Stream.scal
                                                  //| a:249)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$append$1.apply(Stream.scal
                                                  //| a:249)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$append$1.apply(Stream.scal
                                                  //| a:249)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$append$1.apply(Stream.scal
                                                  //| a:249)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$zip$1.apply(Stream.scala:6
                                                  //| 57)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$$anonfun$map$1.apply(Stream.scala:3
                                                  //| 87)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1125)
                                                  //| 	at scala.collection.immutable.Stream$Cons.tail(Stream.scala:1115)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.apply(Stream.sc
                                                  //| ala:37)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1.foldRight(Stream.scala:42)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldRight$1.apply(Stream.
                                                  //| scala:42)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:7)
                                                  //| 	at scalaz.std.StringInstances$stringInstance$.append(String.scala:5)
                                                  //| 	at scalaz.std.StreamInstances$$anon$1$$anonfun$foldMap$2.app
                                                  //| Output exceeds cutoff limit.
}