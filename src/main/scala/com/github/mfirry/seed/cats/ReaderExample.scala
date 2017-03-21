package com.example

import cats._
//import cats.std.all._

import cats.syntax.apply._
import cats.syntax.flatMap._
import cats.syntax.functor._

case class Person(name: String, lastname: String, age: Int)

/**
 * Silly example using Reader from cats.data package object
 */
object ReaderExample {
  import cats.data._

  val nameFromPerson: Reader[Person, String] = Reader(_.name)
  val lastnameFromPerson: Reader[Person, String] = Reader(_.lastname)
  val ageFromPerson: Reader[Person, Int] = Reader(_.age)

  val nameAndLastnameFromPerson: Reader[Person, (String, String)] =
    for {
      n <- nameFromPerson
      l <- lastnameFromPerson
    } yield (n, l)
}
