package com.alisiikh.catharsis.bot

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.api.Http4sBotApi
import com.alisiikh.catharsis.giphy.GiphyClient
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.http4s.blaze.client.BlazeClientBuilder
import com.alisiikh.catharsis.bot.json.Codecs

import scala.concurrent.ExecutionContext

class CatharsisBotProcess[F[_]: Async: Logger](token: String, giphyApiKey: String) {

  def stream: Stream[F, Unit] =
    BlazeClientBuilder[F](ExecutionContext.global)
      .withCheckEndpointAuthentication(false)
      .stream
      .flatMap { client =>
        Stream.force {
          for {
            api   <- Sync[F].delay(new Http4sBotApi(token, client))
            giphy <- Sync[F].delay(new GiphyClient(giphyApiKey))
            bot   <- Sync[F].delay(new CatharsisBot(api, giphy))
          } yield bot.stream
        }
      }
}
