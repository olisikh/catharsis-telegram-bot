package com.alisiikh.catharsis.bot.api

import cats.effect.{ Sync, Timer }
import cats.implicits._
import com.alisiikh.catharsis.bot.model._
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import org.http4s.blaze.http.Url
import org.http4s.client.Client
import org.http4s.{ EntityDecoder, Uri }

import scala.language.{ higherKinds, postfixOps }

trait BotApi[F[_], S[_]] {
  def sendAnimation(chatId: Long, message: String): F[Unit]
  def pollUpdates(fromOffset: Offset): S[BotUpdate]
}

trait StreamBotApi[F[_]] extends BotApi[F, Stream[F, *]]

class Http4sBotApi[F[_]: Timer](token: String, client: Client[F])(
    implicit
    F: Sync[F],
    D: EntityDecoder[F, BotResponse[List[BotUpdate]]],
    logger: Logger[F]
) extends StreamBotApi[F] {

  private val botApiUri = Uri.unsafeFromString(s"https://api.telegram.org/bot$token")

  def sendAnimation(chatId: Long, animationUrl: Url): F[Unit] = {
    val uri = botApiUri / "sendAnimation" =? Map(
      "chat_id"   -> List(chatId.toString),
      "animation" -> List(animationUrl)
    )
    client.expect[Unit](uri)
  }

  def pollUpdates(fromOffset: Offset): Stream[F, BotUpdate] =
    Stream(()).repeat
      .covary[F]
      .evalMapAccumulate(fromOffset) {
        case (offset, _) => requestUpdates(offset)
      }
      .flatMap { case (_, response) => Stream.emits(response.result) }

  private def requestUpdates(offset: Offset): F[(Offset, BotResponse[List[BotUpdate]])] = {

    val uri = botApiUri / "getUpdates" =? Map(
      "offset"          -> List((offset + 1).toString),
      "timeout"         -> List("0.5"), // timeout to throttle the polling
      "allowed_updates" -> List("""["message"]""")
    )

    client
      .expect[BotResponse[List[BotUpdate]]](uri)
      .map(resp => (lastOffset(resp).getOrElse(offset), resp))
      .recoverWith {
        case ex =>
          logger
            .error(ex)("Failed to poll updates")
            .as(offset -> BotResponse(ok = true, Nil))
      }
  }

  // just get the maximum id out of all received updates
  private def lastOffset(resp: BotResponse[List[BotUpdate]]): Option[Offset] =
    resp.result match {
      case Nil   => None
      case other => Some(other.maxBy(_.update_id).update_id)
    }
}
