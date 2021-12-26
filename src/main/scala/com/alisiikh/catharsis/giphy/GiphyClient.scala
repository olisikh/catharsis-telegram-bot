package com.alisiikh.catharsis.giphy

import cats.effect.{ Concurrent, Sync }
import cats.implicits._
import cats.effect.implicits._
import com.alisiikh.catharsis.giphy.GiphyAlgebra.Rating
import com.alisiikh.catharsis.giphy.json.GiphyJsonCodecs
import org.http4s.client.Client
import org.http4s.implicits._
import org.typelevel.log4cats.Logger

class GiphyClient[F[_]: Concurrent: Logger](giphyToken: GiphyToken, client: Client[F])
    extends GiphyAlgebra[F]
    with GiphyJsonCodecs {

  private val giphyUri = uri"https://api.giphy.com"

  override def randomGif(tags: Set[String], rating: Rating = Rating.G): F[GiphyResponse] = {
    val req = giphyUri / "v1" / "gifs" / "random" =? Map(
      "api_key" -> List(giphyToken.value),
      "tag"     -> tags.toList,
      "rating"  -> List(rating.value)
    )

    client.expect[GiphyResponse](req)
  }
}
