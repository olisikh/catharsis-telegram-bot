package com.alisiikh.catharsis.telegram.json

import com.alisiikh.catharsis.JsonCodecs
import com.alisiikh.catharsis.telegram.*
import io.circe.Decoder
import io.circe.generic.semiauto.*

trait TelegramJsonCodecs extends JsonCodecs:
  given Decoder[ChatId]                            = Decoder.decodeLong.map(ChatId.apply)
  given Decoder[Chat]                              = deriveDecoder[Chat]
  given Decoder[User]                              = deriveDecoder[User]
  given Decoder[Message]                           = deriveDecoder[Message]
  given Decoder[TelegramUpdate]                    = deriveDecoder[TelegramUpdate]
  given [A: Decoder]: Decoder[TelegramResponse[A]] = deriveDecoder[TelegramResponse[A]]
