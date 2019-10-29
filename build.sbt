organization := """capegemini"""

name := "cafe-zio"

version := "0.1"

scalaVersion := "2.13.1"

lazy val zioVersion = "1.0.0-RC13"

libraryDependencies ++= Seq(
  "dev.zio"               %% "zio"               % zioVersion,
  "dev.zio"               %% "zio-test"          % zioVersion % "test",
  "dev.zio"               %% "zio-test-sbt"      % zioVersion % "test",
  "org.slf4j"              % "slf4j-log4j12"     % "1.7.26"
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-Xlint",
  "-Xverify",
  "-feature",
  "-Xlint:-unused",
  "-language:_",
  "-Ymacro-annotations"
)
