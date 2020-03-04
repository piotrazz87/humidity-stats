package com.sensorStatistics.util

object HumidityResolver {
  private val ErrorMeasurementLabel = "NaN"

  def resolveHumidity(measurement: String): Option[Int] =
    if (measurement == ErrorMeasurementLabel) None else Some(measurement.toInt)
}
