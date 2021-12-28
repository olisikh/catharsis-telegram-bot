package com.alisiikh.catharsis.bot

import cats.effect.*
import cats.implicits.*
import com.alisiikh.catharsis.giphy.GiphyClient
import com.alisiikh.catharsis.telegram.{ Offset, TelegramClient }
import org.typelevel.log4cats.*
import fs2.*

import scala.language.postfixOps

class Bot[F[_]](telegramClient: TelegramClient[F], giphy: GiphyClient[F])(using Concurrent[F], Logger[F]):

  private val startOffset = Offset(-1)

  def stream: Stream[F, Unit] =
    for
      upd <- Stream(()).repeat
        .covary[F]
        .evalMapAccumulate(startOffset)((offset, _) => telegramClient.requestUpdates(offset))
        .flatMap { case (_, response) => Stream.emits(response.result) }

      (chatId, text) <- Stream.fromOption((upd.chatId, upd.message.flatMap(_.text)).tupled)

      normalizedText = if text.startsWith("/") then text.drop(1) else text
      words          = normalizedText.split(' ').map(_.trim).filterNot(_.isBlank)
      tag            = if words.isEmpty then "cat" else words.mkString(" ")

      result <- Stream.eval(giphy.getRandomGif(tag))
      _      <- Stream.eval(telegramClient.sendAnimation(chatId, result.data.images.original.url))
    yield ()
