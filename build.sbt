val http4sVersion     = "1.0.0-M23"
val specs2Version     = "4.13.1"
val log4CatsVersion   = "2.1.1"
val slf4jVersion      = "1.7.32"
val circeVersion      = "0.14.1"
val catsEffectVersion = "3.3.0"
val zioVersion        = "2.1.8"
val sttpVersion       = "3.9.8"

ThisBuild / scalaVersion := "3.5.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.alisiikh",
    name         := "catharsis-telegram-bot",
    version      := "1.0.0-SNAPSHOT",
    scalaVersion := "3.5.0",
    scalacOptions := Seq(
      "-new-syntax",
      "-indent"
    ),
    libraryDependencies ++= Seq(
      "dev.zio"                       %% "zio"                % zioVersion,
      "dev.zio"                       %% "zio-logging-slf4j2" % "2.3.1",
      "com.softwaremill.sttp.client3" %% "core"               % sttpVersion,
      "com.softwaremill.sttp.client3" %% "circe"              % sttpVersion,
      "com.softwaremill.sttp.client3" %% "zio"                % sttpVersion,
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % sttpVersion,
      "org.slf4j"                      % "slf4j-simple"       % slf4jVersion,
      "io.circe"                      %% "circe-core"         % circeVersion,
      "io.circe"                      %% "circe-generic"      % circeVersion,
    )
  )

enablePlugins(JavaAppPackaging)
