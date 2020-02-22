package com.alisiikh.catharsis.giphy

import at.mukprojects.giphy4j.Giphy
import cats.effect.Sync
import cats.implicits._

import scala.language.higherKinds

class GiphyClient[F[_]](apiKey: String)(implicit F: Sync[F]) extends GiphyClientAlgebra[F] {

  def randomGif(theme: String): F[String] =
    giphyClient
      .map(_.searchRandom(theme))
      .map(_.getData.getImageOriginalUrl)

  private def giphyClient: F[Giphy] = F.delay(new Giphy(apiKey))
}
