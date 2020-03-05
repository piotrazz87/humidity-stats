package com.sensorStatistics.service

import java.nio.file.Files.newDirectoryStream
import java.nio.file.{NoSuchFileException, Path}

import cats.effect.IO
import com.sensorStatistics.service.CSVPathsFromDirectoryProvider.CSVFileFormat

import scala.collection.JavaConverters._
import scala.util.Try

class CSVPathsFromDirectoryProvider {

  def provide(directory: Path): IO[List[Path]] =
    IO.fromTry({
      Try(
        newDirectoryStream(directory).asScala
          .filter(_.getFileName.toString.endsWith(CSVFileFormat))
          .map(_.toAbsolutePath)
          .toList
      ).recover({
        case e: NoSuchFileException => throw UnableToFetchCSVSFromDirectory(s"Directory does not exist ${e.getFile}")
      })
    })
}

case class UnableToFetchCSVSFromDirectory(message: String) extends RuntimeException(message)

private object CSVPathsFromDirectoryProvider {
  private val CSVFileFormat = ".csv"
}
