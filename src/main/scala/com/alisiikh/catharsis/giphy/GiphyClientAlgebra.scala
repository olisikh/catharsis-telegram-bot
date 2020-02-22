package com.alisiikh.catharsis.giphy

import at.mukprojects.giphy4j.Giphy
import cats.effect._
import cats.implicits._

import scala.language.higherKinds

trait GiphyClientAlgebra[F[_]] {
  def randomGif(theme: String): F[String]
}

class GiphyClient[F[_]](apiKey: String)(implicit F: Sync[F]) extends GiphyClientAlgebra[F] {

  def randomGif(theme: String): F[String] =
    giphyClient
      .map(_.searchRandom(theme))
      .map(_.getData.getImageOriginalUrl)

  private def giphyClient: F[Giphy] = F.delay(new Giphy(apiKey))
}
