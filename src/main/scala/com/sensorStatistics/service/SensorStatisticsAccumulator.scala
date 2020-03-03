package com.sensorStatistics.service

import com.sensorStatistics.domain.{DailySensorStatisticsResult, SensorMeasurement, SensorStatistic}
import com.sensorStatistics.util.MeasurementMapper

final class DailySensorStatisticsAccumulator(
    measurementsProcessed: Int,
    measurementsFailed: Int,
    stats: Map[String, SensorStatistic]
) {

  //TODO:refactor
  def processLine(line: String): DailySensorStatisticsAccumulator = {
    val measurement = readMeasurement(line)
    measurement.humidity
      .map(humidity => {
        val updatedSensorStats = stats
          .get(measurement.id)
          .map(resolveStats(humidity, _))
          .getOrElse(resolveStats(humidity, SensorStatistic(measurement.id, None, None, None)))

        new DailySensorStatisticsAccumulator(
          measurementsProcessed + 1,
          measurementsFailed,
          stats.updated(updatedSensorStats.id, updatedSensorStats)
        )
      })
      .getOrElse(
        new DailySensorStatisticsAccumulator(
          measurementsProcessed,
          measurementsFailed + 1,
          stats.get(measurement.id) match {
            case None    => stats.updated(measurement.id, SensorStatistic(measurement.id, None, None, None))
            case Some(_) => stats
          }
        )
      )
  }

  def asResult(filesProcessed: Int): DailySensorStatisticsResult =
    DailySensorStatisticsResult(filesProcessed, measurementsProcessed, measurementsFailed, stats.values.toVector.sorted)

  private def readMeasurement(line: String): SensorMeasurement = {
    val Array(sensorId, measurement) = line.trim.split(",")
    SensorMeasurement(sensorId, MeasurementMapper.toOption(measurement))
  }

  private def resolveStats(measurement: Int, sensorStatistic: SensorStatistic): SensorStatistic = {
    val min = sensorStatistic.min.filter(_ < measurement).orElse(Some(measurement))
    val max = sensorStatistic.max.filter(_ > measurement).orElse(Some(measurement))
    val avg = sensorStatistic.avg.map(lastAvg => (lastAvg + measurement) / 2).orElse(Some(measurement))

    SensorStatistic(sensorStatistic.id, min, avg, max)
  }
}

object DailySensorStatisticsAccumulator {
  def empty: DailySensorStatisticsAccumulator = new DailySensorStatisticsAccumulator(0, 0, Map())
}
