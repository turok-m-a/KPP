package crosses_zeroes;

import java.util.Random;

/**
 * contains A.I. algorithms and stores current game state
 */
public class logic {
  private int fieldarray[][] = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
  /**
   * {@value this.fieldsize} grid size 
   * {@value this.turn} current turn 
   * {@value this.autogame} auto game mode enabled 
   * {@value this.level} difficulty level
   */
  int fieldsize = 3;
  private int turn = 0;
  int level = 3;// difficulty level
  int autogame = 0; // A.I. vs A.I.
  private int cross = 1, zero = 2;
  private Random randomgen = new Random();

  /**
   * sets if A.I. puts "0" or "x" to grid
   * 
   * @param mode "0" for "cross" , "1" for "zero"
   */
  void switchlogic(int mode) {
    if (mode == 0) {
      cross = 1;
      zero = 2;
    } else {
      cross = 2;
      zero = 1;
    }
  }

  /**
   * checks, if it possible, that player can make a "fork" on the next turn if possible , tries to
   * prevent "fork"
   * 
   * @return number of cell to put "zero" , "-1" if there is no fork to prevent
   */

  int blockfork() {
    int firstzero, ii, jj;
    for (int i = 0; i < fieldsize; i++)
      for (int j = 0; j < fieldsize; j++) {
        if (fieldarray[i][j] != 0)
          continue;
        fieldarray[i][j] = cross; // put "x" , as if it was a player
        firstzero = blockplayerwin(); // try to prevent player to win
        if (blockplayerwin() >= 0) {
          ii = firstzero / fieldsize;
          jj = firstzero % fieldsize;
          fieldarray[ii][jj] = zero;
        } else {
          fieldarray[i][j] = 0;
          continue;
        }
        if (blockplayerwin() >= 0) { // if after preventing it still possible
          fieldarray[ii][jj] = 0; // for player to win
          if (autogame == 0) // then it`s a "fork"
            fieldarray[i][j] = zero;
          return (i * fieldsize + j);
        }
        fieldarray[i][j] = 0;
        fieldarray[ii][jj] = 0;
      }
    return -1;
  }

  /**
   * To check if someone wins, or to prevent player to win, it necessary to compare current grid
   * state with an array that contains win combinations (3, 4 and so on in row). such 3-dim. array
   * should contain all combinations for 3x3 4x4 5x5 ... grids. This function replaces hard-coded
   * array, and can be used for any grid size
   * 
   * @param k current number of combination to check ( k < this.fieldsize*2+2 )
   * @param y coordinate to check
   * @param x
   * @return "1" if cell belongs to a win combination , "0" if not
   */
  private int getwincombination(int k, int y, int x) {

    if (k < fieldsize) // strings
    {
      if (y == k)
        return 1;
      return 0;
    }
    if (k >= fieldsize && k < (fieldsize * 2)) {
      if ((x + fieldsize) == k)
        return 1;
      return 0;
    } // columns
    if (k == (fieldsize * 2) && x == y)
      return 1; // diagonals
    if (k == (fieldsize * 2 + 1) && x == (fieldsize - y - 1))
      return 1;
    return 0;
  }

  /**
   * Prevents player to win on the next turn
   * 
   * @return number of cell to put "0" to prevent player to win , "-1" if nothing to prevent
   */
  int blockplayerwin() {
    int equalcount = 0;
    int empty_i = 0, empty_j = 0;
    int wincombinationsnumber = fieldsize * 2 + 2;
    for (int k = 0; k < wincombinationsnumber; k++) {
      equalcount = 0;
      for (int i = 0; i < fieldsize; i++)
        for (int j = 0; j < fieldsize; j++) {
          if (fieldarray[i][j] == cross && (getwincombination(k, i, j) == 1))
            equalcount++;
          if ((fieldarray[i][j] == 0) && (getwincombination(k, i, j) == 1)) {
            empty_i = i;
            empty_j = j;
          }
          if ((fieldarray[i][j] == zero) && (getwincombination(k, i, j) == 1)) { // already non-win
                                                                                 // combination
            j = fieldsize;
            i = fieldsize; // checking next
            equalcount = 0;
          }
        }
      if (equalcount == (fieldsize - 1)) {
        return (empty_i * fieldsize + empty_j);
      }
    }
    return -1;
  }

  /**
   * checks, if A.I. has a possibility to put 3 or 4 in row
   * 
   * @param mode "0" to check if it possible to win on current turn 
   *             "1" to check if it possible to put 3 or 4 in row
   * @return number of cell if it possible , "-1" if not
   */
  int checkaiwinpossibility(int mode) {
    int equalcount = 0;
    int empty_i = 0, empty_j = 0;
    int wincombinationsnum = fieldsize * 2 + 2;
    for (int k = 0; k < wincombinationsnum; k++) {
      equalcount = 0;
      for (int i = 0; i < fieldsize; i++)
        for (int j = 0; j < fieldsize; j++) {
          if (fieldarray[i][j] == zero && getwincombination(k, i, j) == 1)
            equalcount++;
          if ((fieldarray[i][j] == 0) && (getwincombination(k, i, j) == 1)) {
            empty_i = i;
            empty_j = j;
          }
          if ((fieldarray[i][j] == cross) && (getwincombination(k, i, j) == 1)) { // already non-win
                                                                                  // combination
            j = fieldsize;
            i = fieldsize; // check next
            equalcount = 0;
          }
        }
      if ((equalcount == (fieldsize - 1) && mode == 0)
          || (equalcount >= (fieldsize - 2) && mode == 1)) {
        return (empty_i * fieldsize + empty_j);
      }
    }
    return -1;
  }

  /**
   * if player puts "x" to border on first turn , put "0" to prevent him to make a "fork"
   * 
   * @param x coordinates of "x"
   * @param y
   * @return number of cell to put "0"
   */
  int getoppositeposition(int x, int y) {
    // 4 corners
    if (x == 0 && y == 0)
      return (fieldsize - 1) * fieldsize + (fieldsize - 1);
    if (x == (fieldsize - 1) && y == 0)
      return (fieldsize - 1) * fieldsize;
    if (x == 0 && y == (fieldsize - 1))
      return (fieldsize - 1);
    if (x == (fieldsize - 1) && y == (fieldsize - 1))
      return 0;
    // 4 sides
    if (x == 0)
      return y * fieldsize + (fieldsize - 1); // left
    if (x == (fieldsize - 1))
      return (y * fieldsize); // right
    if (y == 0)
      return (fieldsize - 1) * fieldsize + x; // top
    if (y == (fieldsize - 1))
      return x; // bottom
    return 0; // somewhere else
  }

  /**
   * reset grid state to initial
   */
  void resetlogic() {
    for (int i = 0; i < fieldsize; i++)
      for (int j = 0; j < fieldsize; j++)
        fieldarray[i][j] = 0;
    cross = 1;
    zero = 2;
    turn = 0;
    autogame = 0;
  }

  /**
   * put "x", then decide, where to put "0"
   * 
   * @param x coordinates of "x"
   * @param y
   * @return number of cell to put "0" , "-1" if game is over
   */
  int checkcell(int x, int y) {
    fieldarray[y][x] = cross;
    int i, j;
    if (checkwin() >= 0) // if player wins on this turn
      return -1; // do nothing
    turn++;
    if (turn == 1 && level > 1) {
      int coord = getoppositeposition(x, y);
      i = coord / fieldsize;
      j = coord % fieldsize;
      if (autogame == 0)
        fieldarray[i][j] = zero;
      return coord;
    }
    int coord;
    if (turn >= 3) {
      coord = checkaiwinpossibility(0);
      if (coord >= 0) {
        i = coord / fieldsize;
        j = coord % fieldsize;
        if (autogame == 0)
          fieldarray[i][j] = zero;
        return coord;
      }
    }

    coord = blockplayerwin();
    if (coord >= 0) {
      i = coord / fieldsize;
      j = coord % fieldsize;
      if (autogame == 0)
        fieldarray[i][j] = zero;
      return coord;
    }
    if (level > 3) {
      coord = blockfork();
      if (coord != -1) {
        return coord;
      }
    }
    coord = checkaiwinpossibility(1);
    if (coord >= 0) {
      i = coord / fieldsize;
      j = coord % fieldsize;
      if (autogame == 0)
        fieldarray[i][j] = zero;
      return coord;
    }
    int rd = randomgen.nextInt(fieldsize * fieldsize); // if still not decided where to put "0"
    int direction = 1;
    for (int shift = 0;; shift += direction) {
      int num = shift + rd;
      if (num > (fieldsize * fieldsize - 1)) {
        shift = 0;
        direction = -1;
        continue;
      }
      i = num / fieldsize;
      j = num % fieldsize;
      if (fieldarray[i][j] == 0) {
        if (autogame == 0)
          fieldarray[i][j] = zero;
        break;
      }
    }

    return (i * fieldsize + j);
  }

  int getcell(int x, int y) {
    return fieldarray[y][x];
  }

  /**
   * check if someone wins
   * 
   * @return "2" - "zeroes" win, "1" - "crosses" win, "0" - draw, "-1" - game is not over
   */
  int checkwin() {
    int equalcount = 0, playerequalcount = 0;
    int wincombinationsnum = fieldsize * 2 + 2;
    for (int k = 0; k < wincombinationsnum; k++) {
      equalcount = 0;
      playerequalcount = 0;
      for (int i = 0; i < fieldsize; i++) {
        for (int j = 0; j < fieldsize; j++) {
          if ((fieldarray[i][j] == cross) && (getwincombination(k, i, j) == 1)) {
            playerequalcount++;
          }
          if ((fieldarray[i][j] == zero) && (getwincombination(k, i, j) == 1)) {
            equalcount++;
          }
        }
        if (equalcount == fieldsize) {
          return 2;
        }
        if (playerequalcount == fieldsize) {
          return 1;
        }
      }
    }
    equalcount = 0;
    for (int i = 0; i < fieldsize; i++)
      for (int j = 0; j < fieldsize; j++)
        if (fieldarray[i][j] != 0)
          equalcount++;
    if (equalcount == (fieldsize * fieldsize))
      return 0;
    return -1;
  }
}


