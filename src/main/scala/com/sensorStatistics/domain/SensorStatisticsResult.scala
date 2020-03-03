package com.sensorStatistics.domain

case class DailySensorStatisticsResult(
    filesProcessed: Int,
    measurementsProcessed: Int,
    measurementsFailed: Int,
    stats: Vector[SensorStatistic]
)
