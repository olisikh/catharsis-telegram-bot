package com.alisiikh.catharsis

import zio.*
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.stream.*
import cats.implicits.*
import com.alisiikh.catharsis.bot.*
import com.alisiikh.catharsis.giphy.*
import com.alisiikh.catharsis.telegram.*
import sttp.client3.*
import sttp.client3.asynchttpclient.zio.*
import zio.logging.backend.SLF4J

object App extends ZIOAppDefault:

  type AppEnv = SttpBackend[Task, Any] & TelegramClient & GiphyClient

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  def layer: TaskLayer[AppEnv] =
    AsyncHttpClientZioBackend.layer() >+> (
      AppConfig.live >>> (
        TelegramClient.live ++ GiphyClient.live
      )
    )

  override def run: Task[ExitCode] =
    Bot
      .stream()
      .run(ZSink.drain)
      .provideLayer(layer)
      .as(ExitCode.success)
