package com.alisiikh.catharsis.bot

import cats.Monad
import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.api._
import com.alisiikh.catharsis.giphy.GiphyClient
import org.typelevel.log4cats._
import fs2._

import scala.language.postfixOps

class CatharsisBot[F[_]: Concurrent: Logger](api: StreamBotApi[F], giphy: GiphyClient[F]) {

  def stream: Stream[F, Unit] =
    for {
      upd <- api.pollUpdates(Offset(-1))
      (chatId, text) <- Stream.fromOption {
        (upd.chatId, upd.message.flatMap(_.text))
          .mapN((chatId, text) => chatId -> text)
      }
      gifResult <- Stream.eval(giphy.randomGif(s"cat $text"))
      _ <- Stream.eval(
        gifResult.fold(
          err => Logger[F].info("sending error") *> api.sendMessage(chatId, err),
          gif => Logger[F].info("sending animation") *> api.sendAnimation(chatId, gif)
        )
      )
    } yield ()
}
