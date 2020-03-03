package com.sensorStatistics.service

import com.sensorStatistics.domain.{
  FailureStats,
  SensorMeasurement,
  SensorStatistic,
  SensorStatisticsResult,
  SuccessStats
}
import com.sensorStatistics.util.MeasurementMapper.resolveValue

final class SensorStatisticsAccumulator(success: Int, failed: Int, stats: Map[String, SensorStatistic]) {

  def processLine(line: String): SensorStatisticsAccumulator = {
    val measurement = readMeasurement(line)
    measurement.humidity
      .map { humidity =>
        val updatedSensorStats = resolveStatsToUpdate(humidity, measurement.sensorId)
        new SensorStatisticsAccumulator(success + 1, failed, stats.updated(measurement.sensorId, updatedSensorStats))
      }
      .getOrElse(new SensorStatisticsAccumulator(success, failed + 1, resolveStatsToUpdate(measurement.sensorId)))
  }

  def asResult(filesProcessed: Int): SensorStatisticsResult =
    SensorStatisticsResult(filesProcessed, success, failed, stats.values.toStream.sorted)

  private def readMeasurement(line: String): SensorMeasurement = {
    val Array(sensorId, measurement) = line.trim.split(",")
    SensorMeasurement(sensorId, resolveValue(measurement))
  }

  private def resolveStatsToUpdate(sensorId: String): Map[String, SensorStatistic] =
    stats.get(sensorId) match {
      case Some(_) => stats
      case None    => stats.updated(sensorId, FailureStats(sensorId))
    }

  private def resolveStatsToUpdate(humidity: Int, sensorId: String): SuccessStats =
    calculateSensorStatistics(humidity, sensorId,stats.getOrElse(sensorId, None))

  private def calculateSensorStatistics(
      measurement: Int,
      sensorId: String,
      sensorStatistic: Option[SuccessStats]
  ): SuccessStats =
    sensorStatistic
      .map(stats => {
        val min = if (stats.min < measurement) stats.min else measurement
        val max: Int = if (stats.max > measurement) stats.max else measurement
        val avg: Int = (stats.avg + measurement) / 2

        SuccessStats(stats.sensorId, min, avg, max)
      })
      .getOrElse(SuccessStats(sensorId, measurement, measurement, measurement))

}

object SensorStatisticsAccumulator {
  def empty: SensorStatisticsAccumulator = new SensorStatisticsAccumulator(0, 0, Map())
}
