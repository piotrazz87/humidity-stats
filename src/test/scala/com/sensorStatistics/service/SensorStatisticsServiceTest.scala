package com.sensorStatistics.service

import java.nio.file.Paths

import cats.effect.IO
import com.sensorStatistics.UnitSpec
import com.sensorStatistics.domain.{SensorMeasurementsReadings, SensorStatisticsResult}

class SensorStatisticsServiceTest extends UnitSpec {

  private val path = Paths.get("/dir")

  "service" should "provide statistics" in {
    Given("path and readings")

    val readings = SensorMeasurementsReadings(10, fs2.Stream("s2,87", "s2,99", "s2,0"))

    val service = new SensorStatisticsService(_ => IO(readings))

    When("calculateStatisticsForReadings")
    val result = service.calculateStatistics(path)

    Then("result shold equal")
    result.unsafeRunSync() shouldEqual SensorStatisticsResult(10, 2, 0, Stream.empty)
  }
}
