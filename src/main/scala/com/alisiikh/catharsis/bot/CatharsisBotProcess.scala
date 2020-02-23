package com.alisiikh.catharsis.bot

import cats.effect.{ ConcurrentEffect, Timer }
import cats.implicits._
import com.alisiikh.catharsis.bot.api.Http4sBotApi
import com.alisiikh.catharsis.bot.model.{ BotResponse, BotUpdate }
import com.alisiikh.catharsis.giphy.GiphyClient
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

class CatharsisBotProcess[F[_]: Logger: Timer](token: String, giphyApiKey: String)(implicit F: ConcurrentEffect[F]) {

  implicit val decoder: EntityDecoder[F, BotResponse[List[BotUpdate]]] =
    jsonOf[F, BotResponse[List[BotUpdate]]]

  def stream: Stream[F, Unit] =
    BlazeClientBuilder[F](ExecutionContext.global).withDefaultSslContext.stream
      .flatMap { client =>
        Stream.force {
          for {
            api   <- F.delay(new Http4sBotApi(token, client))
            giphy <- F.delay(new GiphyClient(giphyApiKey))
            bot   <- F.delay(new CatharsisBot(api, giphy))
          } yield bot.stream
        }
      }
}
