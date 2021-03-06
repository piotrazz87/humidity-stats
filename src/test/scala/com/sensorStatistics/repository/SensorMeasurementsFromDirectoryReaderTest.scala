package com.sensorStatistics.repository

import java.nio.file.Paths

import cats.effect.IO
import com.sensorStatistics.{TestFilesUtil, UnitSpec}

class SensorMeasurementsFromDirectoryReaderTest extends UnitSpec {

  "reader" should "read csv s from paths" in {
    Given("some files to process")
    TestFilesUtil.createCSV("testCsv.csv", List("sensor_id,min,max,avg", "s1,89", "s2,43"))

    val reader = new SensorMeasurementsFromDirectoryReader(_ => IO(List(Paths.get("testCsv.csv"))))

    When("loading measurements")
    val measurements = reader.readMeasurementsFrom(Paths.get("testCsv.csv"))

    Then("measurements should be loaded")
    measurements.unsafeRunSync().processedFiles shouldEqual 1
    measurements.unsafeRunSync().measurements.compile.toList.unsafeRunSync() should contain only ("s1,89 ", "s2,43 ")
    TestFilesUtil.delete("testCsv.csv")
  }

  "reader" should "read csv s from multiple paths" in {
    Given("some files to process")
    TestFilesUtil.createCSV("testCsv.csv", List("sensor_id,min,max,avg", "s1,89", "s2,43"))
    TestFilesUtil.createCSV("testCsv2.csv", List("sensor_id,min,max,avg", "s6,21", "s5,54"))

    val reader =
      new SensorMeasurementsFromDirectoryReader(_ => IO(List(Paths.get("testCsv.csv"), Paths.get("testCsv2.csv"))))

    When("loading measurements")
    val measurements = reader.readMeasurementsFrom(Paths.get("someDir"))

    Then("measurements should be loaded")
    measurements.unsafeRunSync().processedFiles shouldEqual 2
    measurements.unsafeRunSync().measurements.compile.toList.unsafeRunSync() should contain only (
      "s1,89 ",
      "s2,43 ",
      "s6,21 ",
      "s5,54 "
    )

    TestFilesUtil.delete("testCsv.csv")
    TestFilesUtil.delete("testCsv2.csv")
  }

  "reader" should "raise error when problem with file" in {
    Given("some files to process")

    val reader = new SensorMeasurementsFromDirectoryReader(_ => IO(List(Paths.get("testCsv.csv"))))

    When("loading measurements")
    val measurements = reader.readMeasurementsFrom(Paths.get("testCsv.csv"))

    Then("measurements should be loaded")
    measurements.unsafeRunSync().measurements.compile.toList.attempt.unsafeRunSync() shouldEqual Left(
      StreamingFileException("testCsv.csv")
    )
  }
}
