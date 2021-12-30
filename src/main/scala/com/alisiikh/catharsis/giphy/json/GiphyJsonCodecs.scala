package com.alisiikh.catharsis.giphy.json

import com.alisiikh.catharsis.JsonCodecs
import com.alisiikh.catharsis.giphy.*
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

trait GiphyJsonCodecs extends JsonCodecs:
  given Decoder[GiphyImage]    = deriveDecoder[GiphyImage]
  given Decoder[GiphyImages]   = deriveDecoder[GiphyImages]
  given Decoder[GiphyData]     = deriveDecoder[GiphyData]
  given Decoder[GiphyMeta]     = deriveDecoder[GiphyMeta]
  given Decoder[GiphyResponse] = deriveDecoder[GiphyResponse]
object GiphyJsonCodecs extends GiphyJsonCodecs
