package com.alisiikh.catharsis.bot

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.api._
import com.alisiikh.catharsis.giphy.GiphyClient
import io.chrisdavenport.log4cats._
import fs2._

import scala.language.postfixOps

class CatharsisBot[F[_]: Async: Temporal: Logger](api: StreamBotApi[F], giphy: GiphyClient[F]) {

  def stream: Stream[F, Unit] =
    api
      .pollUpdates(0)
      .map(_.chatId.toSeq)
      .flatMap(Stream.emits)
      .evalMap { chatId =>
        giphy
          .randomGif("cat")
          .map(gifUrl => (chatId, gifUrl))
      }
      .evalMap((api.sendAnimation _).tupled)
      .map(_ => ())
}
