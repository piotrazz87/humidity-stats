package com.sensorStatistics.util

import java.nio.file.Files.newDirectoryStream
import java.nio.file.Path

import cats.effect.IO
import com.sensorStatistics.util.CSVPathsFromDirectoryProvider.CSVFormatPostfix

import scala.collection.JavaConverters._

class CSVPathsFromDirectoryProvider {

  def provide(directory: Path): IO[List[Path]] = IO {
    newDirectoryStream(directory).asScala
      .filter(_.getFileName.toString.endsWith(CSVFormatPostfix))
      .map(_.toAbsolutePath)
      .toList
  }
}

private object CSVPathsFromDirectoryProvider {
  private val CSVFormatPostfix = ".csv"
}
