name := """data-security-lab1"""

version := "1.0"

scalaVersion := "2.12.1"

// logging
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.8"
libraryDependencies += "com.typesafe.scala-logging" % "scala-logging_2.12" % "3.5.0"


// test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"
