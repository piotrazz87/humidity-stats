package com.sensorStatistics.service

import java.nio.file.Path

import cats.effect.IO
import com.sensorStatistics.domain.{DailyMeasurementsReads, DailySensorStatisticsResult}

class SensorStatisticsService(measurementsReader: Path => IO[DailyMeasurementsReads]) {

  def calculateStatistics(
      reportDirectory: Path
  ): IO[DailySensorStatisticsResult] =
    for {
      measurements <- measurementsReader(reportDirectory)
      statistics <- generateResults(measurements)
    } yield statistics

  private def generateResults(dailyMeasurements: DailyMeasurementsReads): IO[DailySensorStatisticsResult] =
    dailyMeasurements.measurements.compile
      .fold(DailySensorStatisticsAccumulator.empty)(_ processLine _)
      .map(_.asResult(dailyMeasurements.processedFiles))
}
