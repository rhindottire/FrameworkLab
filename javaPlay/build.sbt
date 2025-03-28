name := """javaPlay"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.16"

// libraryDependencies += "com.typesafe.play" %% "play" % "3.0.6"

libraryDependencies ++= Seq(
  guice,
  javaJdbc,
  "com.mysql" % "mysql-connector-j" % "8.0.33"
)