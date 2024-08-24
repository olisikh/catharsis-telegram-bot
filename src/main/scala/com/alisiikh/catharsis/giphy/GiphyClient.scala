package com.alisiikh.catharsis.giphy

import com.alisiikh.catharsis.AppConfig

import sttp.client3.*
import sttp.client3.circe.*
import zio.*

/** You can play with Giphy API here: https://developers.giphy.com/explorer/#explorer
  */
class GiphyClient(token: String):
  import GiphyJsonCodecs.given
  import GiphyClient.*

  private val giphyApiUri = uri"https://api.giphy.com"

  def getRandomGif(tag: String, rating: Rating = Rating.R): ZIO[SttpBackend[Task, Any], Throwable, GiphyResponse] =
    for
      backend <- ZIO.service[SttpBackend[Task, Any]]
      response <- basicRequest
        .get(uri"https://api.giphy.com/v1/gifs/random?api_key=$token&tag=$tag&rating=${rating.value}")
        .response(asJson[GiphyResponse])
        .send(backend)

      body <- ZIO
        .fromEither(response.body)
        .mapError(err => new RuntimeException("Failed to fetch random gif, error: $err"))
    yield body

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
