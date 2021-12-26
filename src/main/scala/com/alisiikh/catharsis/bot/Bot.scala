package com.alisiikh.catharsis.bot

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.giphy.GiphyClient
import com.alisiikh.catharsis.telegram.{ Offset, TelegramClient }
import org.typelevel.log4cats._
import fs2._

import scala.language.postfixOps

class Bot[F[_]: Concurrent: Logger](telegramClient: TelegramClient[F], giphy: GiphyClient[F]) {

  def stream: Stream[F, Unit] =
    for {
      upd <- Stream(()).repeat
        .covary[F]
        .evalMapAccumulate(Offset(-1)) { case (offset, _) => telegramClient.requestUpdates(offset) }
        .flatMap { case (_, response) => Stream.emits(response.result) }

      (chatId, text) <- Stream.fromOption((upd.chatId, upd.message.flatMap(_.text)).tupled)

      normalizedText = if (text.startsWith("/")) text.drop(1) else text
      tags           = normalizedText.split(' ').filterNot(_.isBlank).map(_.trim).toSet

      gifResult <- Stream.eval(giphy.randomGif(Set("cat") ++ tags))
      _         <- Stream.eval(telegramClient.sendAnimation(chatId, gifResult.data.images.original.url))
    } yield ()
}
