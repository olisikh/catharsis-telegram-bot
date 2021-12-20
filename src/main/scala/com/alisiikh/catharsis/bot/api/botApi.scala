package com.alisiikh.catharsis.bot.api

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.BotToken
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.http4s.blaze.http.Url
import org.http4s.client.Client
import com.alisiikh.catharsis.bot.json.Codecs
import org.http4s.implicits._

import scala.language.postfixOps

trait BotApi[F[_], S[_]] {
  def sendAnimation(chatId: ChatId, message: String): F[Unit]
  def sendMessage(chatId: ChatId, text: String): F[Unit]
  def pollUpdates(offset: Offset): S[BotUpdate]
}

trait StreamBotApi[F[_]] extends BotApi[F, Stream[F, *]]

class TelegramBotApi[F[_]: Concurrent: Logger](token: BotToken, client: Client[F]) extends StreamBotApi[F] {
  import TelegramBotApi._

  private val botApiUri = uri"https://api.telegram.org" / s"bot${token.value}"

  def sendAnimation(chatId: ChatId, animationUrl: Url): F[Unit] = {
    val req = botApiUri / "sendAnimation" =? Map(
      "chat_id"   -> List(chatId.value.toString),
      "animation" -> List(animationUrl)
    )

    Logger[F].info(req.toString) *>
      client.expect[Unit](req)
  }

  def sendMessage(chatId: ChatId, text: String): F[Unit] = {
    val req = botApiUri / "sendMessage" =? Map(
      "chat_id" -> List(chatId.value.toString),
      "text"    -> List(text)
    )

    client.expect[Unit](req)
  }

  def pollUpdates(offset: Offset): Stream[F, BotUpdate] =
    Stream(()).repeat
      .covary[F]
      .evalMapAccumulate(offset) { case (offset, _) => requestUpdates(offset) }
      .flatMap { case (_, response) => Stream.emits(response.result) }

  private def requestUpdates(offset: Offset): F[(Offset, BotResponse[List[BotUpdate]])] = {
    val req = botApiUri / "getUpdates" =? Map(
      "offset"          -> List(offset.value.toString),
      "timeout"         -> List("0.5"), // timeout to throttle the polling
      "allowed_updates" -> List("""["message"]""")
    )

    client
      .expect[BotResponse[List[BotUpdate]]](req)
      .map(resp => (lastOffset(resp).getOrElse(offset), resp))
      .recoverWith {
        case ex =>
          Logger[F]
            .error(ex)("Failed to poll updates")
            .as(offset -> BotResponse(ok = true, Nil))
      }
  }

  // just get the maximum id out of all received updates
  private def lastOffset(resp: BotResponse[List[BotUpdate]]): Option[Offset] = resp.result match {
    case Nil   => none
    case other => Offset(other.maxBy(_.update_id).update_id).some
  }
}

object TelegramBotApi {
  import org.http4s.circe._
  import org.http4s._
  import Codecs._

  implicit def decoder[F[_]: Concurrent]: EntityDecoder[F, BotResponse[List[BotUpdate]]] =
    jsonOf[F, BotResponse[List[BotUpdate]]]
}
