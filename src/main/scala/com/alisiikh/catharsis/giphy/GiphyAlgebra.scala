package com.alisiikh.catharsis.giphy

import com.alisiikh.catharsis.giphy.GiphyAlgebra.Rating

trait GiphyAlgebra[F[_]] {
  def getRandomGif(tag: String, rating: Rating): F[GiphyResponse]
}
object GiphyAlgebra {
  sealed abstract class Rating(val value: String)
  object Rating {
    case object G    extends Rating("g")
    case object PG   extends Rating("pg")
    case object PG13 extends Rating("pg-13")
    case object R    extends Rating("r")
  }
}
