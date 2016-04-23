package crosses_zeroes;

public class ReplayComparator {
  final static int REPLAY_HEADER_OFFSET = 8;

  public static int compare(byte[] firstGame, byte[] secondGame) { // only two parameters are
    return recursiveCompare(firstGame, secondGame, REPLAY_HEADER_OFFSET);// allowed for comparator
  }

  private static int recursiveCompare(byte[] firstGame, byte[] secondGame, int step) {
    if (firstGame.length <= step) { //end of comparison
      return 0;
    }
    if (firstGame[step] > secondGame[step]) {//compare cells
      return -1;
    }
    if (firstGame[step] < secondGame[step]) {
      return 1;
    }
    return recursiveCompare(firstGame, secondGame, step + 1); //next cell
  }

}
