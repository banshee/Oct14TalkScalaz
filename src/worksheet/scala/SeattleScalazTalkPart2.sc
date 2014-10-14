package com.restphone.scalaztalk.worksheet

import scalaz._
import Scalaz._
import scalaz.concurrent._
import scalaz.Nondeterminism

object SamplesWithValidations {
  // stringify is just a trivial sample function
  // Point out here the the left hand side of \/ could contain
  // something like a like a field name to make error reporting easier
  val stringify: Int => Exception \/ String = (x: Int) => {
    println(s"make string! $x")
    println(s"make string! $x ${Thread.currentThread().getName()}")
    x match {
      case n if n > 1 => -\/(new Exception(s"Boom! $n"))
      case n          => \/-(n.toString)
    }
  }                                               //> stringify  : Int => scalaz.\/[Exception,String] = <function1>

  // This is more interesting.  We can take stringify and turn it into something that runs
  // in the context of a scalaz Task.
  val stringifyWrappedInTask =
    Applicative[Task].point(stringify)            //> stringifyWrappedInTask  : scalaz.concurrent.Task[Int => scalaz.\/[Exception,
                                                  //| String]] = scalaz.concurrent.Task@121c323a
  val xs = List(1, 2, 3)                          //> xs  : List[Int] = List(1, 2, 3)

  val ys = xs map { Task(_) } map { _ <*> stringifyWrappedInTask } map { Task.fork(_) }
                                                  //> ys  : List[scalaz.concurrent.Task[scalaz.\/[Exception,String]]] = List(scala
                                                  //| z.concurrent.Task@19d71b0f, scalaz.concurrent.Task@79318309, scalaz.concurre
                                                  //| nt.Task@21920208)

  val result = ys.sequence.run                    //> make string! 1
                                                  //| make string! 1 pool-1-thread-2
                                                  //| make string! 2
                                                  //| make string! 2 pool-1-thread-4
                                                  //| make string! 3
                                                  //| make string! 3 pool-1-thread-6
                                                  //| result  : List[scalaz.\/[Exception,String]] = List(\/-(1), -\/(java.lang.Exc
                                                  //| eption: Boom! 2), -\/(java.lang.Exception: Boom! 3))

  val (a :: b :: c :: tail) = ys.sequence.run     //> make string! 1
                                                  //| make string! 1 pool-1-thread-8
                                                  //| make string! 2
                                                  //| make string! 2 pool-1-thread-8
                                                  //| make string! 3
                                                  //| make string! 3 pool-1-thread-8
                                                  //| a  : scalaz.\/[Exception,String] = \/-(1)
                                                  //| b  : scalaz.\/[Exception,String] = -\/(java.lang.Exception: Boom! 2)
                                                  //| c  : scalaz.\/[Exception,String] = -\/(java.lang.Exception: Boom! 3)
                                                  //| tail  : List[scalaz.\/[Exception,String]] = List()

  // Now let's see if we got a good result.

  val tryTwo = (a |@| b) { (one, two) => s"got good result: $one $two"; 1000 }
                                                  //> tryTwo  : scalaz.\/[Exception,Int] = -\/(java.lang.Exception: Boom! 2)
  val tryThree = (a |@| b |@| c) { (one, two, three) => s"got good result: $one $two $three" }
                                                  //> tryThree  : scalaz.\/[Exception,String] = -\/(java.lang.Exception: Boom! 2)
                                                  //| 
  // Notice that both of these failed with the first exception

  // Now let's try to report all the errors
  val (d :: e :: f :: tail2) = result map { _.validation.toValidationNel }
                                                  //> d  : scalaz.ValidationNel[Exception,String] = Success(1)
                                                  //| e  : scalaz.ValidationNel[Exception,String] = Failure(NonEmptyList(java.lan
                                                  //| g.Exception: Boom! 2))
                                                  //| f  : scalaz.ValidationNel[Exception,String] = Failure(NonEmptyList(java.lan
                                                  //| g.Exception: Boom! 3))
                                                  //| tail2  : List[scalaz.ValidationNel[Exception,String]] = List()

  val tryTwoNel = (d |@| e) { (one, two) => s"got good result: $one $two" }
                                                  //> tryTwoNel  : scalaz.Validation[scalaz.NonEmptyList[Exception],String] = Fai
                                                  //| lure(NonEmptyList(java.lang.Exception: Boom! 2))
  val tryThreeNel = (d |@| e |@| f) { (one, two, three) => s"got good result: $one $two $three" }
                                                  //> tryThreeNel  : scalaz.Validation[scalaz.NonEmptyList[Exception],String] = F
                                                  //| ailure(NonEmptyList(java.lang.Exception: Boom! 2, java.lang.Exception: Boom
                                                  //| ! 3))

  // Or we could fold the entire result:
  val startValue: ValidationNel[Exception, List[String]] = List().success[Exception].toValidationNel
                                                  //> startValue  : scalaz.ValidationNel[Exception,List[String]] = Success(List()
                                                  //| )
  val resultAsValidationNel = result map { _.validation.toValidationNel }
                                                  //> resultAsValidationNel  : List[scalaz.ValidationNel[Exception,String]] = Lis
                                                  //| t(Success(1), Failure(NonEmptyList(java.lang.Exception: Boom! 2)), Failure(
                                                  //| NonEmptyList(java.lang.Exception: Boom! 3)))

  // Here's another way to sum the errors:
  resultAsValidationNel.foldLeft(startValue)((acc, r) => acc |+| r.map(List(_)))
                                                  //> res0: scalaz.ValidationNel[Exception,List[String]] = Failure(NonEmptyList(j
                                                  //| ava.lang.Exception: Boom! 2, java.lang.Exception: Boom! 3))
  // And here's what you get if they all pass:
  resultAsValidationNel.take(1).foldLeft(startValue)((acc, r) => acc |+| r.map(List(_)))
                                                  //> res1: scalaz.ValidationNel[Exception,List[String]] = Success(List(1))
}