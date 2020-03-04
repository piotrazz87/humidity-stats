import java.io.{BufferedWriter, FileWriter}

import scala.util.Random

object Writer extends App {
  val outputFile: BufferedWriter = new BufferedWriter(new FileWriter("/home/piotrazz/Documents/projects/akka/epam_rekrutacja/csv/new5.csv"))
  (0 to 99999999) foreach (_=>outputFile.write(s"s${10+Random.nextInt(100)},${Random.nextInt(100)}\n"))
  outputFile.close()
}
