package com.alisiikh.catharsis

import cats.effect._
import cats.implicits._
import com.alisiikh.catharsis.bot.CatharsisBotProcess
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.language.higherKinds

object App extends IOApp {

  implicit def unsafeLogger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def stream[F[_]](args: List[String]): Stream[IO, Unit] =
    for {
      token      <- Stream.eval(IO(System.getenv("TELEGRAM_TOKEN")))
      giphyToken <- Stream.eval(IO(System.getenv("GIPHY_TOKEN")))
      _          <- new CatharsisBotProcess[IO](token, giphyToken).stream
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    stream[IO](args).compile.drain.as(ExitCode.Success)
}
