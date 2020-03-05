package com.sensorStatistics.service

import com.sensorStatistics.domain.{
  BrokenSensorStatistics,
  ProperSensorStatistics,
  SensorStatistics,
  SensorsStatisticsResult
}
import com.sensorStatistics.service.SensorStatisticsCalculator.recalculateSensorStatistics
import com.sensorStatistics.util.HumidityResolver.resolveHumidity

final class SensorStatisticsAccumulator private (success: Int, failed: Int, stats: Map[String, SensorStatistics]) {

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

  private def resolveSuccessStatsToUpdate(measurement: Int, sensorId: String): ProperSensorStatistics =
    stats
      .get(sensorId)
      .map {
        case currentStatistics: ProperSensorStatistics => recalculateSensorStatistics(currentStatistics, measurement)
        case _: BrokenSensorStatistics                 => ProperSensorStatistics.initiate(sensorId, measurement)
      }
      .getOrElse(ProperSensorStatistics.initiate(sensorId, measurement))

  private def resolveUpdatedStatsWithFailure(sensorId: String): Map[String, SensorStatistics] =
    stats.get(sensorId) match {
      case Some(_) => stats
      case None    => stats.updated(sensorId, BrokenSensorStatistics(sensorId))
    }
}

object SensorStatisticsAccumulator {
  def empty: SensorStatisticsAccumulator = new SensorStatisticsAccumulator(0, 0, Map())
}
