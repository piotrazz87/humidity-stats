package com.sensorStatistics.repository

import java.nio.file.Path

import cats.effect.IO
import com.sensorStatistics.domain.SensorMeasurementsReadings

trait SensorsReportFetcher {

  def fetchDailyMeasurements(directory: Path): IO[SensorMeasurementsReadings]
}
