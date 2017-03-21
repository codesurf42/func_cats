name := """cats-seed"""

version := "0.0.2"

scalaVersion := "2.12.1"

val catsV = "0.9.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % catsV
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
