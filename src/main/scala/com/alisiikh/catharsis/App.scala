package com.alisiikh.catharsis

import cats.effect.*
import cats.implicits.*
import com.alisiikh.catharsis.bot.BotProcess
import com.alisiikh.catharsis.giphy.GiphyTokens
import com.alisiikh.catharsis.telegram.TelegramTokens
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object App extends IOApp:
  given Logger[IO] = Slf4jLogger.getLogger[IO]

  def stream[F[_]](args: List[String]): Stream[IO, Unit] =
    for
      telegramToken <- Stream.eval(IO(TelegramTokens.make(System.getenv("TELEGRAM_TOKEN"))))
      giphyToken    <- Stream.eval(IO(GiphyTokens.make(System.getenv("GIPHY_TOKEN"))))
      _             <- new BotProcess[IO](telegramToken, giphyToken).stream
    yield ()

  override def run(args: List[String]): IO[ExitCode] =
    stream[IO](args).compile.drain.as(ExitCode.Success)
