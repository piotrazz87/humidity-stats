package com.sensorStatistics.domain

sealed trait SensorStatistics {
  def sensorId: String
  def avgHumidity: Option[Double]
}

object SensorStatistics {
  val ordering: Ordering[SensorStatistics] = Ordering.by(_.avgHumidity)
  implicit def orderByReverse: Ordering[SensorStatistics] = ordering.reverse
}

final case class BrokenSensorStatistics(sensorId: String) extends SensorStatistics {
  override def toString: String = s"$sensorId, NaN, NaN, NaN"
  override def avgHumidity: Option[Double] = None
}

final case class ProperSensorStatistics(sensorId: String, min: Int, avg: Double, max: Int, qtyProcessed: Int)
    extends SensorStatistics {
  override def toString: String = s"$sensorId, $min, ${avg.toInt}, $max"
  override def avgHumidity: Option[Double] = Some(avg)
}

object ProperSensorStatistics {
  def initiate(sensorId: String, measurement: Int) =
    ProperSensorStatistics(sensorId, measurement, measurement, measurement, 1)
}
