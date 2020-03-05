package com.sensorStatistics.service

import java.nio.file.{Files, Paths}

import com.sensorStatistics.{TestFilesUtil, UnitSpec}

class CSVPathsFromDirectoryProviderTest extends UnitSpec {

  "provider" should "provide paths to csvs without other extensions" in {
    Given("some files in dir")
    Files.createDirectory(Paths.get("testDir"))
    TestFilesUtil.createCSV("testDir/testCsv.csv", Nil)
    TestFilesUtil.createCSV("testDir/testCsv2.csv", Nil)
    TestFilesUtil.createCSV("testDir/testCsv3.csv", Nil)
    TestFilesUtil.createCSV("testDir/testTxt.txt", Nil)

    When("loading paths")
    val provider = new CSVPathsFromDirectoryProvider()

    Then("fetch paths to csvs")
    provider
      .provide(Paths.get("testDir"))
      .unsafeRunSync()
      .map(_.toAbsolutePath) should contain only (Paths.get("testDir/testCsv.csv").toAbsolutePath,
    Paths.get("testDir/testCsv2.csv").toAbsolutePath,
    Paths.get("testDir/testCsv3.csv").toAbsolutePath)

    deleteFiles
  }

  "provider" should "raise error when directory directory not exists" in {
    new CSVPathsFromDirectoryProvider()
      .provide(Paths.get("fakeDir"))
      .attempt
      .unsafeRunSync() shouldEqual Left(UnableToFetchCSVSFromDirectory("Directory does not exist fakeDir"))
  }

  private def deleteFiles: Unit = {
    TestFilesUtil.delete("testDir/testCsv.csv")
    TestFilesUtil.delete("testDir/testCsv2.csv")
    TestFilesUtil.delete("testDir/testCsv3.csv")
    TestFilesUtil.delete("testDir/testTxt.txt")

    Files.delete(Paths.get("testDir"))
  }
}
