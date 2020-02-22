package com.alisiikh.catharsis.bot

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.api._
import com.alisiikh.catharsis.giphy.GiphyClient
import io.chrisdavenport.log4cats._
import fs2._

import scala.language.{ higherKinds, postfixOps }

class CatharsisBot[F[_]: Timer](api: StreamBotApi[F], giphy: GiphyClient[F])(
    implicit
    F: Sync[F],
    logger: Logger[F]
) {
  private def pollUpdates: Stream[F, (ChatId, String)] =
    for {
      update <- api.pollUpdates(0)
      chatIdAndMsg <- Stream.emits(
        update.message.flatMap(m => m.text.map(m.chat.id -> _)).toSeq
      )
    } yield chatIdAndMsg

  def stream: Stream[F, Unit] =
    pollUpdates
      .evalMap {
        case (chatId, msg) =>
          giphy
            .randomGif(s"cat $msg")
            .map(gifUrl => (chatId, gifUrl))
      }
      .evalMap((api.sendAnimation _).tupled)
      .map(_ => ())
}
