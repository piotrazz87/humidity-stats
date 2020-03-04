package com.sensorStatistics.service

import java.nio.file.{Files, Paths}

import com.sensorStatistics.{TestFilesUtil, UnitSpec}

class CSVPathsFromDirectoryProviderTest extends UnitSpec {

  "provider" should "provide paths to csvs without other extensions" in {
    Files.createDirectory(Paths.get("testDir"))
    TestFilesUtil.createCSV("testDir/testCsv.csv", Nil)
    TestFilesUtil.createCSV("testDir/testCsv2.csv", Nil)
    TestFilesUtil.createCSV("testDir/testCsv3.csv", Nil)
    TestFilesUtil.createCSV("testDir/testTxt.txt", Nil)

    val provider = new CSVPathsFromDirectoryProvider()

    provider
      .provide(Paths.get("testDir"))
      .unsafeRunSync()
      .map(_.toAbsolutePath) should contain only (Paths.get("testDir/testCsv.csv").toAbsolutePath,
    Paths.get("testDir/testCsv2.csv").toAbsolutePath,
    Paths.get("testDir/testCsv3.csv").toAbsolutePath)

    deleteFiles
  }

  private def deleteFiles = {
    TestFilesUtil.delete("testDir/testCsv.csv")
    TestFilesUtil.delete("testDir/testCsv2.csv")
    TestFilesUtil.delete("testDir/testCsv3.csv")
    TestFilesUtil.delete("testDir/testTxt.txt")

    Files.delete(Paths.get("testDir"))
  }
}
