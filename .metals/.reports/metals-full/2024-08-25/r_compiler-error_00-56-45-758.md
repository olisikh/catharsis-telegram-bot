file://<WORKSPACE>/src/main/scala/com/alisiikh/catharsis/giphy/GiphyClient.scala
### file%3A%2F%2F%2FUsers%2Folisikh%2FDevelop%2Fscala%2Fcatharsis-telegram-bot%2Fsrc%2Fmain%2Fscala%2Fcom%2Falisiikh%2Fcatharsis%2Fgiphy%2FGiphyClient.scala:11: error: `;` expected but `:` found
class GiphyClient(token: String):
                                ^

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.12.19
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.19/scala-library-2.12.19.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/scala/com/alisiikh/catharsis/giphy/GiphyClient.scala
text:
```scala
package com.alisiikh.catharsis.giphy

import com.alisiikh.catharsis.AppConfig

import sttp.client3.*
import sttp.client3.circe.*
import zio.*

/** You can play with Giphy API here: https://developers.giphy.com/explorer/#explorer
  */
class GiphyClient(token: String):
  import GiphyJsonCodecs.given
  import GiphyClient.*

  private val giphyApiUri = uri"https://api.giphy.com"

  override def getRandomGif(
      tag: String,
      rating: Rating = Rating.R
  ): ZIO[SttpBackend[Task, Any], Throwable, GiphyResponse] =
    for
      backend <- ZIO.service[SttpBackend[Task, Any]]
      response <- basicRequest
        .get(uri"https://api.giphy.com/v1/gifs/random?api_key=$token&tag=$tag&rating=${rating.value}")
        .response(asJson[GiphyResponse])
        .send(backend)

      body <- ZIO
        .fromEither(response.body)
        .mapError(err => new RuntimeException("Failed to fetch random gif, error: $err"))
    yield body

object GiphyClient:
  sealed abstract class Rating(val value: String)
  object Rating:
    case object G    extends Rating("g")
    case object PG   extends Rating("pg")
    case object PG13 extends Rating("pg-13")
    case object R    extends Rating("r")

  def live: RLayer[AppConfig, GiphyClient] =
    ZLayer.fromZIO(ZIO.serviceWithZIO[AppConfig](config => GiphyClient(config.giphyToken)))

```



#### Error stacktrace:

```
scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.ScalametaParser.syntaxErrorExpected(ScalametaParser.scala:394)
	scala.meta.internal.parsers.ScalametaParser.acceptStatSep(ScalametaParser.scala:450)
	scala.meta.internal.parsers.ScalametaParser.acceptStatSepOpt(ScalametaParser.scala:452)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4107)
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

file%3A%2F%2F%2FUsers%2Folisikh%2FDevelop%2Fscala%2Fcatharsis-telegram-bot%2Fsrc%2Fmain%2Fscala%2Fcom%2Falisiikh%2Fcatharsis%2Fgiphy%2FGiphyClient.scala:11: error: `;` expected but `:` found
class GiphyClient(token: String):
                                ^