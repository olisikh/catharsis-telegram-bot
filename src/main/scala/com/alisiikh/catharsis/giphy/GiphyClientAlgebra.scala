package com.alisiikh.catharsis.giphy

trait GiphyClientAlgebra[F[_]] {
  def randomGif(theme: String): F[String]
}
