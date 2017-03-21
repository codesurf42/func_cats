package com.example

import cats._
//import cats.std.all._

/**
 * Silly example using Monoid
 */
object example {

  import cats.implicits._
  val t = Monoid[String].combineAll(List("a", "b", "c"))
}
