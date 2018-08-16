import sbt._
import sbt.Keys._

object Shared {
  lazy val junit = libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
}