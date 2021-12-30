package com.alisiikh.catharsis

import cats.effect.Concurrent
import io.circe.Decoder.Result
import io.circe.{ Decoder, HCursor }
import org.http4s.EntityDecoder
import org.http4s.circe.*

trait JsonCodecs:
  given [A: Decoder, B: Decoder]: Decoder[(A | B)] with
    def apply(c: HCursor): Result[(A | B)] = Decoder[B].tryDecode(c).orElse(Decoder[A].tryDecode(c))

  given [F[_]: Concurrent, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
