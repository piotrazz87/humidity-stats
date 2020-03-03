package com.sensorStatistics.repository

import java.nio.file.Path

import cats.effect.{Blocker, ContextShift, IO}
import com.sensorStatistics.domain.DailyMeasurementsReads
import fs2.{Stream, io, text}

import scala.concurrent.ExecutionContext.Implicits.global

class SensorMeasurementsFromDirectoryReader(pathsByDirectory: Path => IO[List[Path]]) extends SensorsReportFetcher {

  implicit private val cs: ContextShift[IO] = IO.contextShift(global)

  def fetchDailyMeasurements(directory: Path): IO[DailyMeasurementsReads] =
    for {
      paths <- pathsByDirectory(directory)
    } yield DailyMeasurementsReads(paths.size, loadMeasurementsFromFiles(paths))

  private def loadMeasurementsFromFiles(paths: List[Path]): Stream[IO, String] =
    paths.map(loadMeasurementsFromFile).reduce(_.merge(_))

  private def loadMeasurementsFromFile(path: Path): Stream[IO, String] =
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
