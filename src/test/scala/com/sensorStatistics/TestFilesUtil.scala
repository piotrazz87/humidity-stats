package com.sensorStatistics

import java.io.{BufferedWriter, File, FileWriter}

object TestFilesUtil {

  def createCSV(fileName: String, lines: List[String]): Unit = {
    val outputFile: BufferedWriter = new BufferedWriter(new FileWriter(fileName))
    lines foreach (line => outputFile.write(s"$line \n"))
    outputFile.close()
  }

  def delete(path: String): Boolean = new File(path).delete()
}
