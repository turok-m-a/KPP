package crosses_zeroes
import scala.collection.mutable.ArrayBuffer;
class Statistics {
  var path = "autogen100k/";
  var listOfFiles = ArrayBuffer[Array[Byte]]();
  var statistics = Array.fill(Constants.MAX_FIELD_SIZE * Constants.MAX_FIELD_SIZE)(0);
  def countStatistics(firstNumber: Int, lastNumber: Int, selectedStep: Int): Array[Int] = {
    for (i <- firstNumber.to(lastNumber)) {
      var filename = path + i.toString();
      val src = scala.io.Source.fromFile(filename);
      //listOfFiles += src.map(_.toByte).toArray;
      //analyzeReplay(listOfFiles(i), selectedStep);
      analyzeReplay(src.map(_.toByte).toArray, selectedStep);
      src.close();
      if (i % 50000 == 0) System.gc();
    }
    return statistics;
  }
  def fieldSize(replay: Array[Byte]): Byte = {
    replay(7)
  }
  def analyzeReplay(replay: Array[Byte], selectedStep: Int) = {
    val steps = replay(3);
    for (i <- 0.to(steps - 1)) {
      if (selectedStep >= steps || selectedStep <= 0) {} //this replay contains less steps than
      else {                                             //required or wrong turn number
        val oneMove = getMove(replay, selectedStep);
        val cellNumber = oneMove.indexOf(1);
        if (cellNumber >= 0) {
          statistics(cellNumber) = statistics(cellNumber) + 1;
        }
      }
    }
  }
  def getMove(replay: Array[Byte], stepNumber: Int): Array[Byte] = { //difference between 2 states
    val size = replay(7); // info from replay file
    val steps = replay(3);
    if (stepNumber == 0) {
      return replay.drop(Constants.REPLAY_HEADER_OFFSET).dropRight((steps - 1) * size * size);
    } else {
      var difference = replay.drop(Constants.REPLAY_HEADER_OFFSET + (stepNumber - 1) * size * size)
        .dropRight((steps - stepNumber - 1) * size * size);
      val difOffset = size * size;
      for (i <- 0.to(difOffset - 1)) {
        difference(i) = difference(i + difOffset).-(difference(i)).toByte;
        if (difference(i) == 2) {
          difference(i) = 1;
        }
      }
      return difference.dropRight(difOffset);
    }
  }
}