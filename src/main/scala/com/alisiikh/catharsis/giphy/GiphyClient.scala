package com.alisiikh.catharsis.giphy

import at.mukprojects.giphy4j.Giphy
import at.mukprojects.giphy4j.entity.search.SearchRandom
import cats.Show
import cats.effect._
import cats.implicits._
import org.typelevel.log4cats.Logger

class GiphyClient[F[_]: Sync: Logger](token: GiphyToken) extends GiphyClientAlgebra[F] {

  type Error = String

  def randomGif(theme: String): F[Either[Error, String]] =
    giphyClient
      .map(_.searchRandom(theme))
      .flatMap(
        resp =>
          Sync[F].delay {
            Option(resp.getData.getUrl)
              .toRight("Something is wrong with Giphy, apologies but no cats today")
        }
      )

  private def giphyClient: F[Giphy] = Sync[F].delay(new Giphy(token.value))
}
