package com.sensorStatistics.service

import com.sensorStatistics.domain.{FailureStats, SensorStatistic, SensorsStatisticsResult, SuccessStats}
import com.sensorStatistics.util.HumidityResolver.resolveHumidity

final class SensorStatisticsAccumulator(success: Int, failed: Int, stats: Map[String, SensorStatistic]) {

  def processLine(line: String): SensorStatisticsAccumulator = {
    val Array(sensorId, measurement) = line.trim.split(",")

    resolveHumidity(measurement)
      .map { humidity =>
        val updatedSensorStats = resolveSuccessStatsToUpdate(humidity, sensorId)
        new SensorStatisticsAccumulator(success + 1, failed, stats.updated(sensorId, updatedSensorStats))
      }
      .getOrElse(new SensorStatisticsAccumulator(success, failed + 1, resolveUpdatedStatsWithFailure(sensorId)))
  }

  def asResult(filesProcessed: Int): SensorsStatisticsResult =
    SensorsStatisticsResult(filesProcessed, success, failed, stats.toStream.map(_._2).sorted)

  private def resolveSuccessStatsToUpdate(measurement: Int, sensorId: String): SuccessStats =
    stats
      .get(sensorId)
      .map {
        case SuccessStats(_, min, avg, max, qty) =>
          val newMin = if (min < measurement) min else measurement
          val newMax = if (max > measurement) max else measurement
          val newAvg = (avg * qty + measurement) / (qty + 1)

          SuccessStats(sensorId, newMin, newAvg, newMax, qty + 1)
        case FailureStats(_) => SuccessStats(sensorId, measurement, measurement, measurement, 1)
      }
      .getOrElse(SuccessStats(sensorId, measurement, measurement, measurement, 1))

  private def resolveUpdatedStatsWithFailure(sensorId: String): Map[String, SensorStatistic] =
    stats.get(sensorId) match {
      case Some(_) => stats
      case None    => stats.updated(sensorId, FailureStats(sensorId))
    }
}

object SensorStatisticsAccumulator {
  def empty: SensorStatisticsAccumulator = new SensorStatisticsAccumulator(0, 0, Map())
}
