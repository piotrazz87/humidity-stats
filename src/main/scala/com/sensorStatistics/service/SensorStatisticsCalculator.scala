package com.sensorStatistics.service

import com.sensorStatistics.domain.ProperSensorStatistics

object SensorStatisticsCalculator {

  def recalculateSensorStatistics(
      currentSensorStatistics: ProperSensorStatistics,
      measurement: Int
  ): ProperSensorStatistics = {
    val newMeasurementsQty = increment(currentSensorStatistics.qtyProcessed)
    val newMin = if (currentSensorStatistics.min < measurement) currentSensorStatistics.min else measurement
    val newMax = if (currentSensorStatistics.max > measurement) currentSensorStatistics.max else measurement
    val newAvg = (currentSensorStatistics.avg * currentSensorStatistics.qtyProcessed + measurement) / newMeasurementsQty

    ProperSensorStatistics(currentSensorStatistics.sensorId, newMin, newAvg, newMax, newMeasurementsQty)
  }

  private def increment(qty: Int): Int = qty + 1
}
