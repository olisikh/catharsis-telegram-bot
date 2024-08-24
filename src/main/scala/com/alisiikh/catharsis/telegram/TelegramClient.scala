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

  def requestUpdates(
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

  def sendAnimation(chatId: ChatId, animationUrl: String): ZIO[SttpBackend[Task, Any], Throwable, Unit] =
    for
      client <- ZIO.service[SttpBackend[Task, Any]]
      resp <- basicRequest
        .post(uri"https://api.telegram.org/bot$token/sendAnimation?chat_id=$chatId&animation=$animationUrl")
        .response(asJson[Unit])
        .send(client)
    yield ()

  def sendMessage(chatId: ChatId, text: String): ZIO[SttpBackend[Task, Any], Throwable, Unit] =
    for
      client <- ZIO.service[SttpBackend[Task, Any]]
      resp <- basicRequest
        .post(uri"https://api.telegram.org/bot$token/sendMessage?chat_id=$chatId&text=$text")
        .response(asJson[Unit])
        .send(client)
    yield ()

object TelegramClient:
  def live: RLayer[AppConfig, TelegramClient] =
    ZLayer.fromZIO(
      for config <- ZIO.service[AppConfig]
      yield TelegramClient(config.telegramToken)
    )
