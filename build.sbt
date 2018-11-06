import Dependencies._


lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "sample-akka-cluster",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.17",
      "com.typesafe.akka" %% "akka-cluster" % "2.5.17",
      "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.17",
      "com.typesafe.akka" %% "akka-http" % "10.1.5",
      scalaTest % Test
    )
  )
