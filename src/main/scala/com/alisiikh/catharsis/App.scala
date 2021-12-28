package com.alisiikh.catharsis

import cats.effect.*
import cats.implicits.*
import com.alisiikh.catharsis.bot.BotProcess
import com.alisiikh.catharsis.giphy.GiphyToken
import com.alisiikh.catharsis.telegram.TelegramToken
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object App extends IOApp:
  implicit def unsafeLogger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def stream[F[_]](args: List[String]): Stream[IO, Unit] =
    for
      telegramToken <- Stream.eval(IO(TelegramToken(System.getenv("TELEGRAM_TOKEN"))))
      giphyToken    <- Stream.eval(IO(GiphyToken(System.getenv("GIPHY_TOKEN"))))
      _             <- new BotProcess[IO](telegramToken, giphyToken).stream
    yield ()

  override def run(args: List[String]): IO[ExitCode] =
    stream[IO](args).compile.drain.as(ExitCode.Success)
