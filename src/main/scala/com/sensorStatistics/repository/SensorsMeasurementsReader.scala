package com.sensorStatistics.repository

import java.nio.file.Path

import cats.effect.IO
import com.sensorStatistics.domain.SensorMeasurementsReadings

trait SensorsMeasurementsReader {

  def readMeasurementsFrom(directory: Path): IO[SensorMeasurementsReadings]
}
