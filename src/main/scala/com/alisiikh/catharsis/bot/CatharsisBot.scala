package com.alisiikh.catharsis.bot

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.api._
import com.alisiikh.catharsis.giphy.GiphyClient
import org.typelevel.log4cats._
import fs2._

import scala.language.postfixOps

class CatharsisBot[F[_]: Concurrent: Logger](api: StreamBotApi[F], giphy: GiphyClient[F]) {

  def stream: Stream[F, Unit] =
    api
      .pollUpdates(Offset(-1))
      .map { update =>
        update.chatId.map { chatId =>
          chatId -> update.message
        }.toSeq
      }
      .flatMap(Stream.emits)
      .evalMap {
        case (chatId, msg) =>
          giphy
            .randomGif(s"cat $msg")
            .map(gifUrl => (chatId, gifUrl))
      }
      .evalMap((api.sendAnimation _).tupled)
      .void
}
