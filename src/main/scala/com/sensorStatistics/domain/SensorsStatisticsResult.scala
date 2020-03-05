package com.sensorStatistics.domain

case class SensorsStatisticsResult(
    filesProcessed: Int,
    successMeasurements: Int,
    failedMeasurements: Int,
    sensorsStats: Stream[SensorStatistics]
)
