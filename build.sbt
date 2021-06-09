val http4sVersion     = "1.0.0-M23"
val specs2Version     = "4.12.0"
val log4CatsVersion   = "2.1.1"
val slf4jVersion      = "1.7.30"
val circeVersion      = "0.14.1"
val catsEffectVersion = "3.1.1"

lazy val root = (project in file("."))
  .settings(
    organization := "com.alisiikh",
    name := "catharsis-telegram-bot",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "org.typelevel"  %% "cats-effect-kernel"  % catsEffectVersion,
      "org.typelevel"  %% "cats-effect-std"     % catsEffectVersion,
      "org.typelevel"  %% "cats-effect"         % catsEffectVersion,
      "org.typelevel"  %% "log4cats-slf4j"      % log4CatsVersion,
      "org.http4s"     %% "http4s-blaze-client" % http4sVersion,
      "org.http4s"     %% "http4s-circe"        % http4sVersion,
      "co.fs2"         %% "fs2-core"            % "3.0.4",
      "io.circe"       %% "circe-core"          % circeVersion,
      "io.circe"       %% "circe-generic"       % circeVersion,
      "at.mukprojects" % "giphy4j"              % "1.0.1",
      "org.slf4j"      % "slf4j-simple"         % slf4jVersion,
      "org.specs2"     %% "specs2-core"         % specs2Version % "test"
    )
  )

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full)

enablePlugins(JavaAppPackaging)
