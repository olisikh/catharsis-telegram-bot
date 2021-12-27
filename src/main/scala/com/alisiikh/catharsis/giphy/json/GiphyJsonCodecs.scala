package com.alisiikh.catharsis.giphy.json

import com.alisiikh.catharsis.JsonCodecs
import com.alisiikh.catharsis.giphy._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

trait GiphyJsonCodecs extends JsonCodecs {
  implicit val giphyImageDec: Decoder[GiphyImage]   = deriveDecoder[GiphyImage]
  implicit val giphyImagesDec: Decoder[GiphyImages] = deriveDecoder[GiphyImages]
  implicit val giphyDataDec: Decoder[GiphyData]     = deriveDecoder[GiphyData]
  implicit val giphyMetaDec: Decoder[GiphyMeta]     = deriveDecoder[GiphyMeta]
  implicit val giphyRespDec: Decoder[GiphyResponse] = deriveDecoder[GiphyResponse]
}
object GiphyJsonCodecs extends GiphyJsonCodecs
