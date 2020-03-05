package com.sensorStatistics.service

import com.sensorStatistics.UnitSpec
import com.sensorStatistics.domain.ProperSensorStatistics

class SensorStatisticsCalculatorTest extends UnitSpec {

  "calculator" should "calculate new statistics with new min and avg" in {
    Given("statistics and measurement")
    val currentSensorStatistics = ProperSensorStatistics("s1", 2, 10, 99, 1)
    val newMeasurement = 1

    When("calculating new stats")
    val newStats = SensorStatisticsCalculator.recalculateSensorStatistics(currentSensorStatistics, newMeasurement)

    Then("result should be")
    newStats shouldEqual ProperSensorStatistics("s1", 1, 5.5, 99, 2)
  }

  "calculator" should "calculate new statistics with new high and avg" in {
    Given("statistics and measurement")
    val currentSensorStatistics = ProperSensorStatistics("s1", 2, 10, 90, 1)
    val newMeasurement = 95

    When("calculating new stats")
    val newStats = SensorStatisticsCalculator.recalculateSensorStatistics(currentSensorStatistics, newMeasurement)

    Then("result should be")
    newStats shouldEqual ProperSensorStatistics("s1", 2, 52.5, 95, 2)
  }
}
