package com.sensorStatistics.domain

case class SensorStatisticsResult(
    filesProcessed: Int,
    successMeasurements: Int,
    failedMeasurements: Int,
    stats: Stream[SensorStatistic]
)
