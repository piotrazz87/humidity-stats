package com.sensorStatistics.repository

import java.nio.file.Path

import cats.effect.{Blocker, ContextShift, IO}
import com.sensorStatistics.domain.SensorMeasurementsReadings
import fs2.Stream.raiseError
import fs2.{io, text, Stream}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class SensorMeasurementsFromDirectoryReader(providePathsToCSV: Path => IO[List[Path]])
    extends SensorsMeasurementsReader {

  implicit private val cs: ContextShift[IO] = IO.contextShift(global)

  def readMeasurementsFrom(directory: Path): IO[SensorMeasurementsReadings] =
    for {
      paths <- providePathsToCSV(directory)
      measurements <- readMeasurements(paths)
    } yield SensorMeasurementsReadings(paths.size, measurements)

  private def readMeasurements(paths: List[Path]): IO[Stream[IO, String]] =
    IO.fromTry(Try(paths.map(readMeasurements).reduce(_.merge(_))))

  private def readMeasurements(path: Path): Stream[IO, String] =
    Stream
      .resource(Blocker[IO])
      .flatMap { blocker =>
        io.file
          .readAll[IO](path, blocker, 8096)
          .through(text.utf8Decode)
          .through(text.lines)
          .tail
          .filter(_.nonEmpty)
      }
      .handleErrorWith(exception => raiseError[IO](StreamingFileException(exception.getMessage)))
}
case class StreamingFileException(message: String)
    extends RuntimeException(s"There was a problem while processing file: $message")
