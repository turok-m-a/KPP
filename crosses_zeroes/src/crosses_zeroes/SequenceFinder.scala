package crosses_zeroes
import scala.collection.mutable.ArrayBuffer;
class SequenceFinder {
  val path = "autogenSorted/";
  def findSequence(): Array[Int] = {
    val sorter = new ScalaSorter();
    sorter.sort(0, Constants.NUMBER_OF_REPLAYS, Constants.SORT_BY_POSITIONS);
    val filename = path + "0";
    val src = scala.io.Source.fromFile(filename);
    val firstReplay = src.map(_.toByte).toList;
    src.close();
    val listOfIterations = recursiveSearch(1, firstReplay, 0, List());
    // (empty list of replay number and repeats count)
    val maxResult = listOfIterations.max(Ordering[Int].on[(_, Int)](_._2));
    val minResult = listOfIterations.min(Ordering[Int].on[(_, Int)](_._2));
    //find maximum count of repeats
    return Array(maxResult._1, maxResult._2, minResult._1, minResult._2);
  }
  @scala.annotation.tailrec
  final def recursiveSearch(current: Int, prevReplay: List[Byte], repeat: Int,
      iterList: List[(Int, Int)]): List[(Int, Int)] = {
    if (current == Constants.NUMBER_OF_REPLAYS) { return iterList; }
    val src = scala.io.Source.fromFile(path + current.toString());
    val currentReplay = src.map(_.toByte).toList;
    src.close();
    prevReplay match {
      case `currentReplay` => return recursiveSearch(current + 1,
          currentReplay, repeat + 1, iterList);
      case _ => return recursiveSearch(current + 1, currentReplay, 0,
          (current - 1, repeat) :: iterList); //reset repeat counter
    }
  }
}