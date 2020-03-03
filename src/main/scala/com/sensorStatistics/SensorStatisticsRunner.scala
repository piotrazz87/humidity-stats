package com.sensorStatistics

import java.nio.file.Paths

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.sensorStatistics.config.Module
import com.sensorStatistics.domain.SensorStatisticsResult

object SensorStatisticsRunner extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    args match {
      case Nil =>
        IO(println("You have to provide path to directory!")).as(ExitCode.Error)
      case directory :: Nil =>
        val appModule = new Module

        appModule.statisticsService
          .calculateStatistics(Paths.get(directory))
          .map(printStats)
          .as(ExitCode.Success)

      case _ :: _ => IO(println("To many arguments. Please provide only path to directory")).as(ExitCode.Error)
    }

  private def printStats(result: SensorStatisticsResult): IO[Unit] =
    IO {
      println(s"Num of processed files: ${result.filesProcessed}")
      println(s"Measurements processed: ${result.successMeasurements}")
      println(s"Measurements failed ${result.failedMeasurements}")
      println("sensor-id, min, avg, max")
      result.stats.foreach(stat => println(stat.toString))
    }
}
