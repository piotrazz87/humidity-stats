package com.sensorStatistics.config

import com.sensorStatistics.repository.SensorMeasurementsFromDirectoryReader
import com.sensorStatistics.service.SensorStatisticsService
import com.sensorStatistics.util.CSVPathsFromDirectoryProvider

class Module {
  lazy val csvPathsProvider = new CSVPathsFromDirectoryProvider
  lazy val measurementsReader = new SensorMeasurementsFromDirectoryReader(csvPathsProvider.provide)
  lazy val statisticsService = new SensorStatisticsService(measurementsReader.fetchDailyMeasurements)
}
