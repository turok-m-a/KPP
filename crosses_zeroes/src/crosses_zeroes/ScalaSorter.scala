package crosses_zeroes
import scala.io.Source;
import java.nio.file.StandardOpenOption;
import java.nio.file.{Files, Paths}
import scala.collection.mutable.ArrayBuffer;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.File;
class ScalaSorter extends Constants {
  var path="autogen100k/";
  var pathSorted = "autogenSorted/";
  var listOfFiles = ArrayBuffer[Array[Byte]]();
  def sort(firstNumber: Int, lastNumber: Int, sortMode: Int) = {
    for (i <- firstNumber.to(lastNumber)) {
      var filename = path + i.toString();
      val src = scala.io.Source.fromFile(filename);
      listOfFiles += src.map(_.toByte).toArray;
      src.close();
      if (i % 50000 == 0) System.gc();
    }
    var sortedList = ArrayBuffer[Array[Byte]]();
    if (sortMode == Constants.SORT_BY_POSITIONS) {
      sortedList = listOfFiles.sortWith(isBetter(_, _, Constants.REPLAY_HEADER_OFFSET));
    } else {
      sortedList = listOfFiles.sortWith(isLessTurns(_, _));
    }

    for (i <- firstNumber.to(lastNumber)) {
      var outputFile = new File(pathSorted + i.toString());
      var outputStream = (new FileOutputStream(outputFile));
      outputStream.write(sortedList(i));
      outputStream.close();
    }
    var outputFile = new File("best");
    var outputStream = (new FileOutputStream(outputFile));
    outputStream.write(sortedList(firstNumber));
    outputStream.close();

    outputFile = new File("worst");
    outputStream = (new FileOutputStream(outputFile));
    outputStream.write(sortedList(lastNumber));
    outputStream.close();
  }

  def isBetter(firstGame: Array[Byte], secondGame: Array[Byte], stepNumber: Int): Boolean = {
    var firstTurnNumber = firstGame(Constants.TURNS_NUMBER_OFFSET); //read turns number
    var secondTurnNumber = secondGame(Constants.TURNS_NUMBER_OFFSET);
    var firstFieldSize = firstGame(7);
    var secondFieldSize = secondGame(7);
    if ((firstTurnNumber*(firstFieldSize*firstFieldSize)) <= stepNumber) return false; //end comparison
    if (firstGame(stepNumber).>(secondGame(stepNumber))) return true;
    if (firstGame(stepNumber).<(secondGame(stepNumber))) return false;
    return isBetter(firstGame, secondGame, stepNumber + 1); //next cell to compare
  }

  def isLessTurns(firstGame: Array[Byte], secondGame: Array[Byte]): Boolean = {
    var firstTurnNumber = firstGame(Constants.TURNS_NUMBER_OFFSET); //read turns number
    var secondTurnNumber = secondGame(Constants.TURNS_NUMBER_OFFSET);
    if (firstTurnNumber < secondTurnNumber) return true;
    return false;
  }
}