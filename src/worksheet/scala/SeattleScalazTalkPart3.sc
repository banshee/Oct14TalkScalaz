package com.restphone.scalaztalk.worksheet

import scalaz._
import Scalaz._
import scalaz.concurrent._
import scalaz.Nondeterminism

object SamplesWithFile {
  val filenames = List("/tmp/a.txt", "/tmp/b.txt", "asdfasdf")
                                                  //> filenames  : List[String] = List(/tmp/a.txt, /tmp/b.txt, asdfasdf)
  val fileTasks = filenames map { filename => Task(new java.io.File(filename)) }
                                                  //> fileTasks  : List[scalaz.concurrent.Task[java.io.File]] = List(scalaz.concur
                                                  //| rent.Task@30339b07, scalaz.concurrent.Task@c07014a, scalaz.concurrent.Task@1
                                                  //| fe5a771)
  val countLines =
    ((f: java.io.File) =>
      com.google.common.io.Files.readLines(f, java.nio.charset.StandardCharsets.UTF_8).size()).point[Task]
                                                  //> countLines  : scalaz.concurrent.Task[java.io.File => Int] = scalaz.concurren
                                                  //| t.Task@781128cc
  val fileStatTasks = fileTasks map { _ <*> countLines }
                                                  //> fileStatTasks  : List[scalaz.concurrent.Task[Int]] = List(scalaz.concurrent.
                                                  //| Task@4960e09f, scalaz.concurrent.Task@773b0c5b, scalaz.concurrent.Task@45a86
                                                  //| 784)

  val protectedFileStatTasks = fileStatTasks map { _.attemptRun }
                                                  //> protectedFileStatTasks  : List[scalaz.\/[Throwable,Int]] = List(\/-(1), \/-(
                                                  //| 2), -\/(java.io.FileNotFoundException: asdfasdf (No such file or directory))
                                                  //| )

  val emptyResult: ValidationNel[Throwable, Int] = 0.success.toValidationNel
                                                  //> emptyResult  : scalaz.ValidationNel[Throwable,Int] = Success(0)
  // Look, you can add these together
  emptyResult |+| emptyResult                     //> res0: scalaz.ValidationNel[Throwable,Int] = Success(0)

  // convert the results to a ValidationNel -
  val resultsNel = protectedFileStatTasks map { _.validation.toValidationNel }
                                                  //> resultsNel  : List[scalaz.ValidationNel[Throwable,Int]] = List(Success(1), S
                                                  //| uccess(2), Failure(NonEmptyList(java.io.FileNotFoundException: asdfasdf (No 
                                                  //| such file or directory))))
  resultsNel.foldLeft(emptyResult) { (acc, v) => acc |+| v }
                                                  //> res1: scalaz.ValidationNel[Throwable,Int] = Failure(NonEmptyList(java.io.Fil
                                                  //| eNotFoundException: asdfasdf (No such file or directory)))
  resultsNel map { _ | 0 }                        //> res2: List[Int] = List(1, 2, 0)
}