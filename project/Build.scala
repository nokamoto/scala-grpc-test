import sbt.Keys._
import sbt._

object Build extends sbt.Build {
  lazy val root = (project in file(".")).settings(name := "scala-grpc-test", scalaVersion := "2.11.8")
}
