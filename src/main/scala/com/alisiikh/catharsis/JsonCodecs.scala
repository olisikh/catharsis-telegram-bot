package com.alisiikh.catharsis

import cats.effect.Concurrent
import io.circe.Decoder
import org.http4s.EntityDecoder
import org.http4s.circe.*

trait JsonCodecs:
  given [F[_]: Concurrent, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
