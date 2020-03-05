name := "sensorStatistics"

version := "1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Ywarn-adapted-args",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard",
  "-Ypartial-unification",
  "-Xfatal-warnings")

fork in run := true

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "2.1.1",
  "co.fs2" %% "fs2-core" % "2.2.1",
  "co.fs2" %% "fs2-io" % "2.2.2",

  "org.scalatest" %% "scalatest" % "3.1.1" % Test
)
