package com.alisiikh.catharsis

import zio.*
import cats.implicits.*

case class AppConfig(
    telegramToken: String,
    giphyToken: String
)

object AppConfig:
  def live: TaskLayer[AppConfig] =
    ZLayer.fromZIO(
      for
        telegramToken <- System.env("TELEGRAM_TOKEN")
        giphyToken    <- System.env("GIPHY_TOKEN")

        config <- ZIO
          .fromOption(
            for
              telegramToken <- telegramToken
              giphyToken    <- giphyToken
            yield AppConfig(telegramToken, giphyToken)
          )
          .orElseFail(new RuntimeException("TELEGRAM_TOKEN or GIPHY_TOKEN is not set"))
      yield config
    )
