package com.sensorStatistics.util

object MeasurementMapper {
  private val ErrorMeasurementLabel = "NaN"

  def resolveValue(measurement: String): Option[Int] =
    if (measurement == ErrorMeasurementLabel) None else Some(measurement.toInt)

  def fromOption(measurement: Option[Int]): String = measurement.map(_.toString).getOrElse(ErrorMeasurementLabel)
}
