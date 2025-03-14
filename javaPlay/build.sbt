name := """javaPlay"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.16"

libraryDependencies += guice
// libraryDependencies += "com.typesafe.play" %% "play" % "3.0.6"