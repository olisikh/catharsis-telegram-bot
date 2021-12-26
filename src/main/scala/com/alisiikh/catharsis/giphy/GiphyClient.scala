package com.alisiikh.catharsis.giphy

import cats.effect.Concurrent
import com.alisiikh.catharsis.giphy.GiphyAlgebra.Rating
import com.alisiikh.catharsis.giphy.json.GiphyJsonCodecs
import org.http4s.client.Client
import org.http4s.implicits._
import org.typelevel.log4cats.Logger

/**
  * You can play with Giphy API here:
  * https://developers.giphy.com/explorer/#explorer
  */
class GiphyClient[F[_]: Concurrent: Logger](giphyToken: GiphyToken, client: Client[F])
    extends GiphyAlgebra[F]
    with GiphyJsonCodecs {

  private val giphyApiUri = uri"https://api.giphy.com"

  override def randomGif(tags: Set[String], rating: Rating = Rating.G): F[GiphyResponse] = {
    val req = giphyApiUri / "v1" / "gifs" / "random" =? Map(
      "api_key" -> List(giphyToken.value),
      "tag"     -> tags.toList,
      "rating"  -> List(rating.value)
    )

    client.expect[GiphyResponse](req)
  }
}
