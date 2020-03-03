import java.io.{BufferedWriter, FileWriter}

import scala.util.Random

object Writer extends App {
  val outputFile: BufferedWriter = new BufferedWriter(new FileWriter("/home/piotrazz/Documents/projects/akka/epam_rekrutacja/csv/new.csv"))
  (0 to 999999) foreach (i=>outputFile.write(s"s$i,${Random.nextInt(100)}\n"))
  outputFile.close()
}
