name := """covid-certificate-generator"""
organization := "fr.ngames"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.19"
