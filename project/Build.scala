import sbt.Keys._
import sbt._
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}
import sbtassembly.AssemblyKeys._
import sbtassembly.{MergeStrategy, PathList}

object Build extends sbt.Build {
  private[this] def default(name: String) = {
    Project(id = name, base = file(s"./$name"), settings = Seq(
      scalaVersion := "2.11.8",
      testOptions in Test += Tests.Argument("-oD"),
      libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"))
  }

  private[this] val protoSettings = PB.protobufSettings ++ Seq(
    PB.runProtoc in PB.protobufConfig := { args =>
      com.github.os72.protocjar.Protoc.runProtoc("-v300" +: args.toArray)
    },
    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-all" % "0.13.2",
      "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % (PB.scalapbVersion in PB.protobufConfig).value))

  private[this] def grpcAssembly(name: String) = {
    Seq(
      test in assembly := {},
      assemblyJarName in assembly := s"scala-grpc-test-$name.jar",
      assemblyMergeStrategy in assembly := {
        case PathList(ps @ _*) if ps.last == "io.netty.versions.properties" => MergeStrategy.discard
        case x => (assemblyMergeStrategy in assembly).value(x)
      })
  }

  lazy val proto = default("proto").settings(protoSettings)

  lazy val server = default("server").dependsOn(proto).settings(grpcAssembly("server"))

  lazy val client = default("client").dependsOn(proto, server % "test->test").settings(grpcAssembly("client"))

  lazy val root = (project in file(".")).aggregate(proto, server, client)
}
