package com.alisiikh.catharsis.giphy

import at.mukprojects.giphy4j.Giphy
import cats.effect._
import cats.implicits._

class GiphyClient[F[_]: Async](apiKey: String) extends GiphyClientAlgebra[F] {

  def randomGif(theme: String): F[String] =
    giphyClient
      .map(_.searchRandom(theme))
      .map(_.getData.getImageOriginalUrl)

  private def giphyClient: F[Giphy] = Sync[F].delay(new Giphy(apiKey))
}
