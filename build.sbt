val http4sVersion = "0.21.0"
val specs2Version = "4.8.3"
val log4CatsVersion = "1.0.1"
val slf4jVersion = "1.7.28"
val circeVersion = "0.13.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.alisiikh",
    name := "catharsis-telegram-bot",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.10",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "co.fs2" %% "fs2-core" % "2.2.2",
      "co.fs2" %% "fs2-cats" % "0.5.0",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.chrisdavenport" %% "log4cats-slf4j" % log4CatsVersion,
      "at.mukprojects" % "giphy4j" % "1.0.1",
      "org.slf4j" % "slf4j-simple" % slf4jVersion,
      "org.specs2" %% "specs2-core" % specs2Version % "test"
    )
  )

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full)
