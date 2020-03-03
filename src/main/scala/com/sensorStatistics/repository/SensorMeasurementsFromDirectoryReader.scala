package com.sensorStatistics.repository

import java.nio.file.Path

import cats.effect.{Blocker, ContextShift, IO}
import com.sensorStatistics.domain.SensorMeasurementsReadings
import fs2.{io, text, Stream}

import scala.concurrent.ExecutionContext.Implicits.global

class SensorMeasurementsFromDirectoryReader(providePathsToCSV: Path => IO[List[Path]])
    extends SensorsMeasurementsReader {

  implicit private val cs: ContextShift[IO] = IO.contextShift(global)

  def readMeasurementsFrom(directory: Path): IO[SensorMeasurementsReadings] =
    for {
      paths <- providePathsToCSV(directory)
    } yield SensorMeasurementsReadings(paths.size, readMeasurements(paths))

  private def readMeasurements(paths: List[Path]): Stream[IO, String] =
    paths.map(readMeasurements).reduce(_.merge(_))

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
}
