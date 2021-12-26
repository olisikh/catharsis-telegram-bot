package com.alisiikh.catharsis.bot

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.giphy.{ GiphyClient, GiphyToken }
import com.alisiikh.catharsis.telegram.{ TelegramClient, TelegramToken }
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.http4s.blaze.client.BlazeClientBuilder

import scala.concurrent.ExecutionContext

class BotProcess[F[_]: Async: Logger](botToken: TelegramToken, giphyToken: GiphyToken) {

  def stream: Stream[F, Unit] =
    for {
      client <- BlazeClientBuilder[F](ExecutionContext.global)
        .withCheckEndpointAuthentication(false)
        .stream

      api   = new TelegramClient(botToken, client)
      giphy = new GiphyClient(giphyToken, client)
      bot   = new Bot(api, giphy)

      _ <- bot.stream
    } yield ()
}
