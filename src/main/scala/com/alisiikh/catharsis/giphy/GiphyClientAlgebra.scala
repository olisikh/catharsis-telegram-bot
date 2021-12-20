package com.alisiikh.catharsis.giphy

trait GiphyClientAlgebra[F[_]] {
  type Error

  def randomGif(theme: String): F[Either[Error, String]]
}
