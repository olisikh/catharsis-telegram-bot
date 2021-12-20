package com.alisiikh.catharsis.bot

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.api.TelegramBotApi
import com.alisiikh.catharsis.giphy.{ GiphyClient, GiphyToken }
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.http4s.blaze.client.BlazeClientBuilder

import scala.concurrent.ExecutionContext

class CatharsisBotProcess[F[_]: Async: Logger](botToken: BotToken, giphyToken: GiphyToken) {

  def stream: Stream[F, Unit] =
    BlazeClientBuilder[F](ExecutionContext.global)
      .withCheckEndpointAuthentication(false)
      .stream
      .flatMap { client =>
        Stream.force {
          for {
            api   <- Sync[F].delay(new TelegramBotApi(botToken, client))
            giphy <- Sync[F].delay(new GiphyClient(giphyToken))
            bot   <- Sync[F].delay(new CatharsisBot(api, giphy))
          } yield bot.stream
        }
      }
}
