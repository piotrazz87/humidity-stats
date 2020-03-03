package com.sensorStatistics.domain

import com.sensorStatistics.util.MeasurementMapper.fromOption

case class SensorStatistic(id: String, min: Option[Int], avg: Option[Int], max: Option[Int])
    extends Ordered[SensorStatistic] {

  override def toString: String = s"$id, ${fromOption(min)}, ${fromOption(avg)}, ${fromOption(max)}"

  override def compare(that: SensorStatistic): Int = if (this.avg.getOrElse(0) <= that.avg.getOrElse(0)) 1 else -1

}
