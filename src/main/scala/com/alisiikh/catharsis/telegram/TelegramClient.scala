package com.alisiikh.catharsis.telegram

import cats.implicits._
import cats.effect.{ Concurrent, Sync }
import cats.effect.implicits._
import com.alisiikh.catharsis.telegram.json.TelegramJsonCodecs
import org.http4s.blaze.http.Url
import org.http4s.client.Client
import org.http4s.implicits._
import org.typelevel.log4cats.Logger

class TelegramClient[F[_]: Concurrent: Logger](token: TelegramToken, client: Client[F])
    extends TelegramBotAlgebra[F]
    with TelegramJsonCodecs {

  private val botApiUri = uri"https://api.telegram.org" / s"bot${token.value}"

  override def requestUpdates(offset: Offset): F[(Offset, TelegramResponse[List[TelegramUpdate]])] = {
    val req = botApiUri / "getUpdates" =? Map(
      "offset"          -> List(offset.value.toString),
      "timeout"         -> List("0.5"), // timeout to throttle the polling
      "allowed_updates" -> List("""["message"]""")
    )

    client
      .expect[TelegramResponse[List[TelegramUpdate]]](req)
      .map(resp => (lastOffset(resp).getOrElse(offset), resp))
      .recoverWith {
        case ex =>
          Logger[F]
            .error(ex)("Failed to poll updates")
            .as(offset -> TelegramResponse(ok = true, Nil))
      }
  }

  // just get the maximum id out of all received updates
  private def lastOffset(resp: TelegramResponse[List[TelegramUpdate]]): Option[Offset] =
    resp.result match {
      case Nil   => none
      case other => Offset(other.maxBy(_.update_id).update_id).inc.some
    }

  override def sendAnimation(chatId: ChatId, animationUrl: Url): F[Unit] = {
    val req = botApiUri / "sendAnimation" =? Map(
      "chat_id"   -> List(chatId.value.toString),
      "animation" -> List(animationUrl)
    )

    client.expect[Unit](req)
  }

  override def sendMessage(chatId: ChatId, text: String): F[Unit] = {
    val req = botApiUri / "sendMessage" =? Map(
      "chat_id" -> List(chatId.value.toString),
      "text"    -> List(text)
    )

    client.expect[Unit](req)
  }
}
