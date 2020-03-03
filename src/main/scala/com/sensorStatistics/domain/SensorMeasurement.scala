package com.sensorStatistics.domain

case class SensorMeasurement(sensorId: String, humidity: Option[Int])

trait Measurement {
  def sensorId: String
}

case class FailedMeasurement(sensorId: String) extends Measurement
case class SuccessMeasurement(sensorId: String, humidity: Int) extends Measurement
