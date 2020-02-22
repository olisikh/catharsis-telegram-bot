package com.alisiikh.catharsis.giphy

import scala.language.higherKinds

trait GiphyClientAlgebra[F[_]] {
  def randomGif(theme: String): F[String]
}
