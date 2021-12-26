package com.alisiikh.catharsis.giphy.json

import cats.effect.Concurrent
import com.alisiikh.catharsis.giphy._
import io.circe.Decoder
import io.circe.generic.extras
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe._

trait GiphyJsonCodecs {
  implicit val giphyImageDec  = deriveDecoder[GiphyImage]
  implicit val giphyImagesDec = deriveDecoder[GiphyImages]
  implicit val giphyDataDec   = deriveDecoder[GiphyData]
  implicit val giphyMetaDec   = deriveDecoder[GiphyMeta]
  implicit val giphyRespDec   = deriveDecoder[GiphyResponse]

  implicit def decoder[F[_]: Concurrent, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
}
object GiphyJsonCodecs extends GiphyJsonCodecs
