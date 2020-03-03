package com.sensorStatistics

import java.nio.file.Paths

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.sensorStatistics.config.Module
import com.sensorStatistics.domain.DailySensorStatisticsResult

object SensorStatisticsRunner extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    args match {
      case Nil =>
        IO(println("You have to provide path to directory!")).as(ExitCode.Error)
      case directory :: Nil =>
        val appModule = new Module
        (for {
          stats <- appModule.statisticsService.calculateStatistics(Paths.get(directory))
          _ <- printStats(stats)
        } yield stats)
          .as(ExitCode.Success)

      case _ :: _ => IO(println("To many arguments. Please provide only path to directory")).as(ExitCode.Error)
    }

  private def printStats(stats: DailySensorStatisticsResult): IO[Unit] =
    IO {
      println(s"Num of processed files: ${stats.filesProcessed}")
      println(s"Measurements processed: ${stats.measurementsProcessed}")
      println(s"Measurements failed ${stats.measurementsFailed}")
      println("sensor-id, min, avg, max")
      stats.stats.foreach(s => println(s.toString))
    }
}
