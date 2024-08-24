package com.alisiikh.catharsis.bot

import sttp.client3.*
import zio.*
import zio.stream.*
import cats.implicits.*
import com.alisiikh.catharsis.giphy.*
import com.alisiikh.catharsis.telegram.*

object Bot:

  // TODO: rewrite to make it more ZStream-ideomatic
  def stream(
      startOffset: Offset = Offset(-1)
  ): ZStream[SttpBackend[Task, Any] & TelegramClient & GiphyClient, Throwable, Unit] =
    for
      telegramClient <- ZStream.service[TelegramClient]
      giphyClient    <- ZStream.service[GiphyClient]

      resp <- ZStream(())
        .repeat(Schedule.spaced(1.second))
        .mapAccumZIO(startOffset)((offset, _) => telegramClient.requestUpdates(offset))

      update  <- ZStream.fromIterable(resp.result)
      message <- ZStream.fromIterable(update.message)

      chatId = message.chat.id
      text   = message.text.getOrElse("")

      normalizedText = if text.startsWith("/") then text.drop(1) else text
      words          = normalizedText.split(' ').map(_.trim).filterNot(_.isBlank)
      tag            = if words.isEmpty then "cat" else words.mkString(" ")

      result <- ZStream.fromZIO(giphyClient.getRandomGif(tag))
      _      <- ZStream.fromZIO(telegramClient.sendAnimation(chatId, result.data.images.original.url))
    yield ()
