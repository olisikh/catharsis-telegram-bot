package com.alisiikh.catharsis.bot.json

import com.alisiikh.catharsis.bot.api.{ BotResponse, BotUpdate, Chat, ChatId, Message, User }
import io.circe.Decoder
import io.circe.generic.semiauto._
import io.circe.generic.extras

object Codecs {

  implicit val chatIdDec: Decoder[ChatId] = extras.semiauto.deriveUnwrappedDecoder

  implicit val chatDec: Decoder[Chat]           = deriveDecoder[Chat]
  implicit val botUserDec: Decoder[User]        = deriveDecoder[User]
  implicit val botMessageDec: Decoder[Message]  = deriveDecoder[Message]
  implicit val botUpdateDec: Decoder[BotUpdate] = deriveDecoder[BotUpdate]

  implicit def botResponseDec[A: Decoder]: Decoder[BotResponse[A]] = deriveDecoder[BotResponse[A]]
}
