package com.sensorStatistics.service

import java.nio.file.Path

import cats.effect.IO
import com.sensorStatistics.domain.{SensorMeasurementsReadings, SensorStatisticsResult}

class SensorStatisticsService(readMeasurements: Path => IO[SensorMeasurementsReadings]) {

  def calculateStatistics(reportDirectory: Path): IO[SensorStatisticsResult] =
    for {
      measurements: SensorMeasurementsReadings <- readMeasurements(reportDirectory)
      statistics <- generateResults(measurements)
    } yield statistics

  private def generateResults(dailyMeasurements: SensorMeasurementsReadings): IO[SensorStatisticsResult] =
    dailyMeasurements.measurements
      .compile
      .fold(SensorStatisticsAccumulator.empty)(_ processLine _)
      .map(_.asResult(dailyMeasurements.processedFiles))
}
