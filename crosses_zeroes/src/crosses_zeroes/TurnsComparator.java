package crosses_zeroes;

public class TurnsComparator implements Constants {
  public static int compare(byte[] firstGame, byte[] secondGame) { // compare number of turns
    if (firstGame[TURNS_NUMBER_OFFSET] < secondGame[TURNS_NUMBER_OFFSET]) {
      return -1;
    }
    if (firstGame[TURNS_NUMBER_OFFSET] > secondGame[TURNS_NUMBER_OFFSET]) {
      return 1;
    }
    return 0;
  }
}
