package com.sensorStatistics.domain

import cats.effect.IO
import fs2.Stream

case class DailyMeasurementsReads(processedFiles: Int, measurements: Stream[IO, String])
