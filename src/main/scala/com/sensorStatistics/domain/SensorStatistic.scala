package com.sensorStatistics.domain

sealed trait SensorStatistic {
  def sensorId: String
  def avgHumidity: Option[Double]
}

final case class FailureStats(sensorId: String) extends SensorStatistic {
  override def toString: String = s"$sensorId, NaN, NaN, NaN"
  override def avgHumidity: Option[Double] = None
}

final case class SuccessStats(sensorId: String, min: Int, avg: Double, max: Int, qtyProcessed: Int)
    extends SensorStatistic {
  override def toString: String = s"$sensorId, $min, ${avg.toInt}, $max"
  override def avgHumidity: Option[Double] = Some(avg)
}

object SensorStatistic {
  val ordering: Ordering[SensorStatistic] = Ordering.by(_.avgHumidity)
  implicit def orderByReverse: Ordering[SensorStatistic] = ordering.reverse
}
