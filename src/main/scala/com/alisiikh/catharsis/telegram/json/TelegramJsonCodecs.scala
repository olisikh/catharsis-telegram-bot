package com.alisiikh.catharsis.telegram.json

import cats.effect.Concurrent
import com.alisiikh.catharsis.telegram._
import io.circe.Decoder
import io.circe.generic.extras
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

trait TelegramJsonCodecs {
  implicit val chatIdDec: Decoder[ChatId] = extras.semiauto.deriveUnwrappedDecoder

  implicit val chatDec: Decoder[Chat]                = deriveDecoder[Chat]
  implicit val botUserDec: Decoder[User]             = deriveDecoder[User]
  implicit val botMessageDec: Decoder[Message]       = deriveDecoder[Message]
  implicit val botUpdateDec: Decoder[TelegramUpdate] = deriveDecoder[TelegramUpdate]

  implicit def botResponseDec[A: Decoder]: Decoder[TelegramResponse[A]] = deriveDecoder[TelegramResponse[A]]

  implicit def decoder[F[_]: Concurrent, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
}
