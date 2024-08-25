package com.alisiikh.catharsis.giphy

import com.alisiikh.catharsis.AppConfig
import cats.implicits.*

import sttp.client3.*
import sttp.client3.circe.*
import zio.*
import io.circe.*
import io.circe.DecodingFailure.Reason

/** You can play with Giphy API here: https://developers.giphy.com/explorer/#explorer
  */
class GiphyClient(token: String):
  import GiphyJsonCodecs.given
  import GiphyClient.*

  def getRandomGif(
      tag: String,
      rating: Rating = Rating.R
  ): ZIO[SttpBackend[Task, Any], Throwable, Option[String]] =
    for
      backend <- ZIO.service[SttpBackend[Task, Any]]
      response <- basicRequest
        .get(uri"https://api.giphy.com/v1/gifs/random?api_key=$token&tag=$tag&rating=${rating.value}")
        .response(asJson[GiphyResponse])
        .send(backend)

      url <- ZIO
        .fromEither(response.body)
        .map(_.data.images.original.url)
        .tapError(err => ZIO.logWarning(s"Failed to fetch random gif, error: $err"))
        .either
        .map(_.toOption)
    yield url

object GiphyClient:
  sealed abstract class Rating(val value: String)
  object Rating:
    case object G    extends Rating("g")
    case object PG   extends Rating("pg")
    case object PG13 extends Rating("pg-13")
    case object R    extends Rating("r")

  def live: RLayer[AppConfig, GiphyClient] =
    ZLayer.fromZIO(
      for config <- ZIO.service[AppConfig]
      yield GiphyClient(config.giphyToken)
    )
