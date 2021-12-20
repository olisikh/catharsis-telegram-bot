import Dependencies._

lazy val root = (project in file("."))
  .settings(
    organization := "com.alisiikh",
    name := "catharsis-telegram-bot",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "org.typelevel"  %% "cats-effect-kernel"   % catsEffectVersion,
      "org.typelevel"  %% "cats-effect-std"      % catsEffectVersion,
      "org.typelevel"  %% "cats-effect"          % catsEffectVersion,
      "org.typelevel"  %% "log4cats-slf4j"       % log4CatsVersion,
      "org.http4s"     %% "http4s-blaze-client"  % http4sVersion,
      "org.http4s"     %% "http4s-circe"         % http4sVersion,
      "co.fs2"         %% "fs2-core"             % "3.2.3",
      "io.circe"       %% "circe-core"           % circeVersion,
      "io.circe"       %% "circe-generic"        % circeVersion,
      "io.circe"       %% "circe-generic-extras" % circeVersion,
      "at.mukprojects" % "giphy4j"               % "1.0.1",
      "org.slf4j"      % "slf4j-simple"          % slf4jVersion,
      "org.specs2"     %% "specs2-core"          % specs2Version % "test"
    )
  )

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full)

enablePlugins(JavaAppPackaging)
