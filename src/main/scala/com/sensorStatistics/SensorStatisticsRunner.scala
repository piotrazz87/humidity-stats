package com.sensorStatistics

import java.nio.file.Paths

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.sensorStatistics.config.Module
import com.sensorStatistics.domain.SensorsStatisticsResult
import com.typesafe.scalalogging.LazyLogging

object SensorStatisticsRunner extends IOApp with LazyLogging {

  override def run(args: List[String]): IO[ExitCode] =
    args match {
      case Nil =>
        IO(println("You have to provide path to directory!")).as(ExitCode.Error)
      case directory :: Nil =>
        println("Starting processing csv files from given directory...")
        val appModule = new Module

        (for {
          statistics <- appModule.statisticsService.calculateStatistics(Paths.get(directory)).attempt
          _ <- statistics match {
            case Left(error)  => IO(println(s"Unable to process files : ${error.getMessage}"))
            case Right(stats) => printStats(stats)
          }
        } yield ()).as(ExitCode.Success)

      case _ :: _ => IO(println("To many arguments. Please provide only path to directory")).as(ExitCode.Error)
    }

  private def printStats(result: SensorsStatisticsResult): IO[Unit] =
    IO {
      println(s"Num of processed files: ${result.filesProcessed}")
      println(s"Measurements processed: ${result.successMeasurements}")
      println(s"Measurements failed ${result.failedMeasurements}")
      println("sensor-id, min, avg, max")

      result.sensorsStats.foreach(sensorStatistic => println(sensorStatistic.toString))
    }
}
