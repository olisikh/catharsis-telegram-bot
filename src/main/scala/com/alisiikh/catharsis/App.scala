package com.alisiikh.catharsis

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.{ BotToken, TelegramBot }
import com.alisiikh.catharsis.bot.api.TelegramBotApi
import com.alisiikh.catharsis.giphy.{ GiphyClient, GiphyToken }
import fs2.Stream
import org.http4s.blaze.client.BlazeClientBuilder
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext

object App extends IOApp {

  implicit def unsafeLogger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def stream[F[_]](args: List[String]): Stream[IO, Unit] =
    for {
      botToken   <- Stream.eval(IO(BotToken(System.getenv("TELEGRAM_TOKEN"))))
      giphyToken <- Stream.eval(IO(GiphyToken(System.getenv("GIPHY_TOKEN"))))
      client <- BlazeClientBuilder[IO](ExecutionContext.global)
        .withCheckEndpointAuthentication(false)
        .stream

      api   = new TelegramBotApi(botToken, client)
      giphy = new GiphyClient[IO](giphyToken)
      bot   = new TelegramBot(api, giphy)
    } yield bot.stream

  override def run(args: List[String]): IO[ExitCode] =
    stream[IO](args).compile.drain.as(ExitCode.Success)
}
