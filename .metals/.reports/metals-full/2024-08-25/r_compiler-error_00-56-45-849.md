file://<WORKSPACE>/src/main/scala/com/alisiikh/catharsis/telegram/TelegramClient.scala
### file%3A%2F%2F%2FUsers%2Folisikh%2FDevelop%2Fscala%2Fcatharsis-telegram-bot%2Fsrc%2Fmain%2Fscala%2Fcom%2Falisiikh%2Fcatharsis%2Ftelegram%2FTelegramClient.scala:11: error: `;` expected but `:` found
class TelegramClient(token: String):
                                   ^

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.12.19
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.19/scala-library-2.12.19.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/scala/com/alisiikh/catharsis/telegram/TelegramClient.scala
text:
```scala
package com.alisiikh.catharsis.telegram

import com.alisiikh.catharsis.AppConfig
import cats.implicits.*
import com.alisiikh.catharsis.telegram.json.TelegramJsonCodecs
import sttp.client3.*
import sttp.client3.circe.*
import zio.*
import java.net.URL

class TelegramClient(token: String):
  import TelegramJsonCodecs.given

  override def requestUpdates(
      offset: Offset
  ): ZIO[SttpBackend[Task, Any], Throwable, (Offset, TelegramResponse[List[TelegramUpdate]])] =
    (
      for
        client <- ZIO.service[SttpBackend[Task, Any]]
        resp <- basicRequest
          .get(uri"https://api.telegram.org/bot$token/getUpdates")
          .response(asJson[TelegramResponse[List[TelegramUpdate]]])
          .send(client)

        body <- ZIO
          .fromEither(resp.body)
          .mapError(err => new RuntimeException(s"Failed to fetch updates, error: $err"))
      yield offset -> body
    ).catchAll { case ex =>
      ZIO
        .logErrorCause("Failed to poll updates", Cause.fail(ex))
        .as(offset -> TelegramResponse(ok = true, Nil))
    }

  // just get the maximum id out of all received updates
  private def lastOffset(resp: TelegramResponse[List[TelegramUpdate]]): Option[Offset] =
    resp.result match
      case Nil   => none
      case other => Offset(other.maxBy(_.update_id).update_id).inc.some

  override def sendAnimation(chatId: ChatId, animationUrl: URL): ZIO[SttpBackend[Task, Any], Throwable, Unit] =
    for
      client <- ZIO.service[SttpBackend[Task, Any]]
      resp <- basicRequest
        .post(uri"https://api.telegram.org/bot$token/sendAnimation?chat_id=$chatId&animation=$animationUrl")
        .response(asJson[Unit])
        .send(client)
    yield ()

  override def sendMessage(chatId: ChatId, text: String): ZIO[SttpBackend[Task, Any], Throwable, Unit] =
    for
      client <- ZIO.service[SttpBackend[Task, Any]]
      resp <- basicRequest
        .post(uri"https://api.telegram.org/bot$token/sendMessage?chat_id=$chatId&text=$text")
        .response(asJson[Unit])
        .send(client)
    yield ()

object TelegramClient:
  def live: RLayer[AppConfig, TelegramClient] =
    ZLayer.fromService(config => TelegramClient(config.telegramToken))

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

file%3A%2F%2F%2FUsers%2Folisikh%2FDevelop%2Fscala%2Fcatharsis-telegram-bot%2Fsrc%2Fmain%2Fscala%2Fcom%2Falisiikh%2Fcatharsis%2Ftelegram%2FTelegramClient.scala:11: error: `;` expected but `:` found
class TelegramClient(token: String):
                                   ^