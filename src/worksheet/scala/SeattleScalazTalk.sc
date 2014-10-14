package com.restphone.scalaztalk.worksheet

import scalaz._
import Scalaz._
import scalaz.concurrent._
import scalaz.Nondeterminism

object SamplesWithIntegers {
  // stringify is just a trivial sample function
  val stringify = (x: Int) => {
    println(s"make string! $x ${Thread.currentThread().getName()}")
    (x, x.toString())
  }                                               //> stringify  : Int => (Int, String) = <function1>

  // This is more interesting.  We can take stringify and turn it into something that runs
  // in the context of a scalaz Task.
  val stringifyWrappedInTask =
    Applicative[Task].point(stringify)            //> stringifyWrappedInTask  : scalaz.concurrent.Task[Int => (Int, String)] = sca
                                                  //| laz.concurrent.Task@4d0e7233
                                                  
  // But how do we use that thing wrapped in a Task?
  // Just wrap its arguments in Tasks also, then use the
  // [[ap]] function on Bind.  Here's how to call it on the value
  // 5:
  val result1 = Bind[Task].ap(Task(5))(stringifyWrappedInTask)
                                                  //> result1  : scalaz.concurrent.Task[(Int, String)] = scalaz.concurrent.Task@15
                                                  //| 2b4053

  // That can get tedious, so scalaz provides an operator to do the same thing:
  val result2 = Task(5) <*> stringifyWrappedInTask//> result2  : scalaz.concurrent.Task[(Int, String)] = scalaz.concurrent.Task@29
                                                  //| 989e7c

  // And the results are identical:
  result1.run                                     //> make string! 5 pool-1-thread-1
                                                  //| res0: (Int, String) = (5,5)
  result2.run                                     //> make string! 5 pool-1-thread-2
                                                  //| res1: (Int, String) = (5,5)

  // Let's move up to collections of values and how to apply functions in contexts to those collections
  val xs = List(1, 2) map { Task (_) }            //> xs  : List[scalaz.concurrent.Task[Int]] = List(scalaz.concurrent.Task@7d9d0
                                                  //| 67f, scalaz.concurrent.Task@5015bc46)

  // Compare this to result2 - it's similar, we're just mapping over the values in a collection,
  // putting the values into a context
  val ys = xs map { _ <*> stringifyWrappedInTask } map { Task.fork(_) }
                                                  //> ys  : List[scalaz.concurrent.Task[(Int, String)]] = List(scalaz.concurrent.
                                                  //| Task@a470f4f, scalaz.concurrent.Task@3f3e10ce)

  // Let's get the value out by just transforming the shape from List[Task[String]] to Task[List[String]]
  val ysAsSingleTask = ys.sequence                //> ysAsSingleTask  : scalaz.concurrent.Task[List[(Int, String)]] = scalaz.conc
                                                  //| urrent.Task@70fa8cbc

  val ysResult = ysAsSingleTask.run               //> make string! 1 pool-1-thread-4
                                                  //| make string! 2 pool-1-thread-6
                                                  //| ysResult  : List[(Int, String)] = List((1,1), (2,2))
}