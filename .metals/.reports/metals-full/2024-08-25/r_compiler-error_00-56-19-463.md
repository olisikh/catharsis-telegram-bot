file://<WORKSPACE>/src/main/scala/com/alisiikh/catharsis/App.scala
### file%3A%2F%2F%2FUsers%2Folisikh%2FDevelop%2Fscala%2Fcatharsis-telegram-bot%2Fsrc%2Fmain%2Fscala%2Fcom%2Falisiikh%2Fcatharsis%2FApp.scala:1: error: illegal start of definition `val`
val http4sVersion     = "1.0.0-M23"
^

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.12.19
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.19/scala-library-2.12.19.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/scala/com/alisiikh/catharsis/App.scala
text:
```scala
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
      "dev.zio"                       %% "zio"                           % zioVersion,
      "dev.zio"                       %% "zio-logging-slf4j2"            % "2.3.1",
      "com.softwaremill.sttp.client3" %% "core"                          % sttpVersion,
      "com.softwaremill.sttp.client3" %% "circe"                         % sttpVersion,
      "com.softwaremill.sttp.client3" %% "zio"                           % sttpVersion,
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % "3.1.2",
      "org.slf4j"                      % "slf4j-simple"                  % slf4jVersion,
      "io.circe"                      %% "circe-core"                    % circeVersion,
      "io.circe"                      %% "circe-generic"                 % circeVersion
    )
  )

enablePlugins(JavaAppPackaging)

```



#### Error stacktrace:

```
scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4109)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$statSeq$1(ScalametaParser.scala:4096)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$statSeq$1$adapted(ScalametaParser.scala:4096)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$listBy(ScalametaParser.scala:562)
	scala.meta.internal.parsers.ScalametaParser.statSeq(ScalametaParser.scala:4096)
	scala.meta.internal.parsers.ScalametaParser.bracelessPackageStats$1(ScalametaParser.scala:4285)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$source$1(ScalametaParser.scala:4288)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:325)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:369)
	scala.meta.internal.parsers.ScalametaParser.source(ScalametaParser.scala:4264)
	scala.meta.internal.parsers.ScalametaParser.entrypointSource(ScalametaParser.scala:4291)
	scala.meta.internal.parsers.ScalametaParser.parseSourceImpl(ScalametaParser.scala:119)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$parseSource$1(ScalametaParser.scala:116)
	scala.meta.internal.parsers.ScalametaParser.parseRuleAfterBOF(ScalametaParser.scala:58)
	scala.meta.internal.parsers.ScalametaParser.parseRule(ScalametaParser.scala:53)
	scala.meta.internal.parsers.ScalametaParser.parseSource(ScalametaParser.scala:116)
	scala.meta.parsers.Parse$.$anonfun$parseSource$1(Parse.scala:30)
	scala.meta.parsers.Parse$$anon$1.apply(Parse.scala:37)
	scala.meta.parsers.Api$XtensionParseDialectInput.parse(Api.scala:22)
	scala.meta.internal.semanticdb.scalac.ParseOps$XtensionCompilationUnitSource.toSource(ParseOps.scala:15)
	scala.meta.internal.semanticdb.scalac.TextDocumentOps$XtensionCompilationUnitDocument.toTextDocument(TextDocumentOps.scala:161)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:54)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticdbTextDocument$1(ScalaPresentationCompiler.scala:469)
```
#### Short summary: 

file%3A%2F%2F%2FUsers%2Folisikh%2FDevelop%2Fscala%2Fcatharsis-telegram-bot%2Fsrc%2Fmain%2Fscala%2Fcom%2Falisiikh%2Fcatharsis%2FApp.scala:1: error: illegal start of definition `val`
val http4sVersion     = "1.0.0-M23"
^