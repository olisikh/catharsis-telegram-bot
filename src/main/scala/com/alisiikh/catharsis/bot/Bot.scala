package com.alisiikh.catharsis.bot

import sttp.client3.*
import zio.*
import zio.stream.*
import cats.implicits.*
import com.alisiikh.catharsis.giphy.*
import com.alisiikh.catharsis.telegram.*

object Bot:

  def stream(
      interval: Duration = 5.seconds,
      startOffset: Offset = Offset(-1)
  ): ZStream[SttpBackend[Task, Any] & TelegramClient & GiphyClient, Throwable, Unit] =
    for
      telegramClient <- ZStream.service[TelegramClient]
      giphyClient    <- ZStream.service[GiphyClient]

      resp <- ZStream(())
        .repeat(Schedule.spaced(interval))
        .mapAccumZIO(startOffset)((offset, _) =>
          ZIO.logInfo(s"Current offset is: $offset") *>
            telegramClient.requestUpdates(offset)
        )

      update  <- ZStream.fromIterable(resp.result)
      message <- ZStream.fromIterable(update.message)

      chatId = message.chat.id
      tags   = message.tokenized.mkString(" ")

      url <- ZStream.fromZIO(giphyClient.getRandomGif(tags)).collectSome
      _   <- ZStream.fromZIO(telegramClient.sendAnimation(chatId, url))
    yield ()
