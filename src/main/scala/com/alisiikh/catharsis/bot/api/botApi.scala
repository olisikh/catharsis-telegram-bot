package com.alisiikh.catharsis.bot.api

import cats.effect._
import cats.implicits._
import fs2.Stream
import org.typelevel.log4cats.Logger
import io.circe.Decoder
import org.http4s.blaze.http.Url
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import org.http4s.{ EntityDecoder, Uri }

import scala.language.postfixOps

trait BotApi[F[_], S[_]] {
  def sendAnimation(chatId: ChatId, message: String): F[Unit]
  def pollUpdates(offset: Offset): S[BotUpdate]
}

trait StreamBotApi[F[_]] extends BotApi[F, Stream[F, *]]

class Http4sBotApi[F[_]: Concurrent: Logger](token: String, client: Client[F]) extends StreamBotApi[F] {
  import Http4sBotApi._

  private val botApiUri = Uri.unsafeFromString(s"https://api.telegram.org/bot$token")

  def sendAnimation(chatId: ChatId, animationUrl: Url): F[Unit] = {
    val uri = botApiUri / "sendAnimation" =? Map(
      "chat_id"   -> List(chatId.value.toString),
      "animation" -> List(animationUrl)
    )
    client.expect[Unit](uri)
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

object Http4sBotApi {
  import io.circe.generic.auto._
  import org.http4s.circe._
  import org.http4s._

  implicit def decoder[F[_]: Concurrent]: EntityDecoder[F, BotResponse[List[BotUpdate]]] =
    jsonOf[F, BotResponse[List[BotUpdate]]]
}
