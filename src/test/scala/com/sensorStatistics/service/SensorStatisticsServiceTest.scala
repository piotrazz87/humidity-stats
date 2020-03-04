package com.sensorStatistics.service

import java.nio.file.Paths

import cats.effect.IO
import com.sensorStatistics.UnitSpec
import com.sensorStatistics.domain.{SensorFailureStatistic, SensorMeasurementsReadings, SensorSuccessStatistic, SensorsStatisticsResult}

class SensorStatisticsServiceTest extends UnitSpec {

  private val path = Paths.get("/dir")

  "service" should "provide statistics with avg for one sensor " in {
    Given("path and readings")

    val readings = SensorMeasurementsReadings(10, fs2.Stream("s2,87", "s2,99", "s2,0"))

    val service = new SensorStatisticsService(_ => IO(readings))

    When("calculateStatisticsForReadings")
    val result = service.calculateStatistics(path)

    Then("result should equal")
    result
      .unsafeRunSync() shouldEqual SensorsStatisticsResult(10, 3, 0, Stream(SensorSuccessStatistic("s2", 0, 62, 99, 3)))
  }

  "service" should "provide statistics for sensor with failed measurements " in {
    Given("path and readings")

    val readings = SensorMeasurementsReadings(10, fs2.Stream("s2,NaN", "s2,NaN", "s2,NaN"))

    val service = new SensorStatisticsService(_ => IO(readings))

    When("calculateStatisticsForReadings")
    val result = service.calculateStatistics(path)

    Then("result should equal")
    result.unsafeRunSync() shouldEqual SensorsStatisticsResult(10, 0, 3, Stream(SensorFailureStatistic("s2")))
  }

  "service" should "provide statistics for sensor with mixed measurements " in {
    Given("path and readings")

    val readings = SensorMeasurementsReadings(10, fs2.Stream("s2,NaN", "s2,NaN", "s2,NaN", "s2,45", "s2,87"))

    val service = new SensorStatisticsService(_ => IO(readings))

    When("calculateStatisticsForReadings")
    val result = service.calculateStatistics(path)

    Then("result should equal")
    result.unsafeRunSync() shouldEqual SensorsStatisticsResult(
      10,
      2,
      3,
      Stream(SensorSuccessStatistic("s2", 45, 66, 87, 2))
    )
  }

  "service" should "provide statistics for many sensors with mixed measurements " in {
    Given("path and readings")

    val readings = SensorMeasurementsReadings(
      10,
      fs2.Stream(
        "s2,NaN",
        "s2,NaN",
        "s2,NaN",
        "s2,45",
        "s2,87",
        "s1,NaN",
        "s1,0",
        "s1,6",
        "s3,1",
        "s3,99",
        "s4,NaN",
        "s4,NaN",
        "s4,NaN"
      )
    )

    val service = new SensorStatisticsService(_ => IO(readings))

    When("calculateStatisticsForReadings")
    val result = service.calculateStatistics(path)

    Then("result should equal")
    val stats = result.unsafeRunSync()
    stats.filesProcessed shouldEqual 10
    stats.successMeasurements shouldEqual 6
    stats.failedMeasurements shouldEqual 7
    stats.sensorsStats should contain only (
      SensorSuccessStatistic("s1", 0, 3, 6, 2),
      SensorSuccessStatistic("s2", 45, 66, 87, 2),
      SensorSuccessStatistic("s3", 1, 50, 99, 2),
      SensorFailureStatistic("s4")
    )
  }
}
