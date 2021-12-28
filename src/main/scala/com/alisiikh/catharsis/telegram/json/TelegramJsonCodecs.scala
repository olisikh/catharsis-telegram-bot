package com.alisiikh.catharsis.telegram.json

import com.alisiikh.catharsis.JsonCodecs
import com.alisiikh.catharsis.telegram._
import io.circe.Decoder
import io.circe.generic.semiauto._

trait TelegramJsonCodecs extends JsonCodecs:

  implicit val chatIdDec: Decoder[ChatId] = Decoder.decodeLong.map(ChatId.apply)

  implicit val chatDec: Decoder[Chat]                     = deriveDecoder[Chat]
  implicit val userDec: Decoder[User]                     = deriveDecoder[User]
  implicit val msgDec: Decoder[Message]                   = deriveDecoder[Message]
  implicit val telegramUpdateDec: Decoder[TelegramUpdate] = deriveDecoder[TelegramUpdate]

  implicit def telegramRespDec[A: Decoder]: Decoder[TelegramResponse[A]] = deriveDecoder[TelegramResponse[A]]
