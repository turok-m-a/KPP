package crosses_zeroes;

import java.util.Random;

/**
 * contains A.I. algorithms, stores current game state
 */
public class GameLogic implements Constants {
  private int fieldArray[][] = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
  int fieldSize = 3;
  private int turn = 0;
  int difficultyLevel = MEDIUM;
  boolean autoGameEnabled = false; // A.I. vs A.I.
  /**
   * {@value this.fieldSize} grid size 
   * {@value this.turn} current turn 
   * {@value this.autoGameEnabled} auto game mode enabled 
   * {@value this.difficultyLevel} difficulty level
   */
  private int playerMark = CROSS_MARK, aIMark = ZERO_MARK; // A.I. plays "zeros" , user plays "crosses"
  private Random randomGenerator = new Random();

  /**
   * sets if A.I. puts "0" or "x" to grid
   * 
   * @param mode "0" A.I. plays "crosses" , "1" A.I. plays "zeros"
   */
  void switchMode(int mode) {
    if (mode == ZERO_MARK) {  //A.I. plays "zeros"
      playerMark = CROSS_MARK;
      aIMark = ZERO_MARK;
    } else {                  //A.I. plays "crosses"
      playerMark = ZERO_MARK;
      aIMark = CROSS_MARK;
    }
  }

  /**
   * checks, if it possible, that player can make a "fork" on the next turn if possible , tries to
   * prevent "fork"
   * 
   * @return number of cell to put "aIMark" if fork was prevented, or NOTHING_TO_PREVENT
   */

  int blockFork() {
    int firstAIMark, firstAIMarkRow, firstAIMarkColumn;
    for (int i = 0; i < fieldSize; i++)
      for (int j = 0; j < fieldSize; j++) {
        if (fieldArray[i][j] != 0)
          continue;
        fieldArray[i][j] = playerMark; // put "x" , as if it was a player
        firstAIMark = blockPlayerWin(); // try to prevent player to win
        if (blockPlayerWin() >= 0) {
          firstAIMarkRow = firstAIMark / fieldSize; 
          firstAIMarkColumn = firstAIMark % fieldSize;
          fieldArray[firstAIMarkRow][firstAIMarkColumn] = aIMark;
        } else {
          fieldArray[i][j] = 0;
          continue;
        }
        
        if (blockPlayerWin() >= 0) { // if after preventing it still possible
          fieldArray[firstAIMarkRow][firstAIMarkColumn] = 0; // for player to win
          if (!autoGameEnabled) // then it`s a "fork"
            fieldArray[i][j] = aIMark;
          return (i * fieldSize + j);   // cell number
        }
        
        fieldArray[i][j] = 0;
        fieldArray[firstAIMarkRow][firstAIMarkColumn] = 0;
      }
    return NOTHING_TO_PREVENT;
  }

  /**
   * To check if someone wins, or to prevent player to win, it necessary to compare current grid
   * state with an array that contains win combinations (3, 4 and so on in row). such 3-dim. array
   * should contain all combinations for 3x3 4x4 5x5 ... grids. This function replaces hard-coded
   * array, and can be used for any grid size
   * 
   * @param k current number of combination to check ( k < this.fieldSize*2+2 )
   * @param y coordinate to check
   * @param x
   * @return "1" if cell belongs to a win combination , "0" if not
   */
  private int getWinCombination(int k, int y, int x) {

    if (k < fieldSize) { // strings
      if (y == k) {
        return 1;
      }
      return 0;
    }
    if (k >= fieldSize && k < (fieldSize * 2)) { // columns
      if ((x + fieldSize) == k) {
        return 1;
      }
      return 0;
    } 
    if (k == (fieldSize * 2) && x == y) { // diagonals
      return 1;
    }
    if (k == (fieldSize * 2 + 1) && x == (fieldSize - y - 1)) {
      return 1;
    }
    return 0;
  }

  /**
   * Prevents player to win on the next turn
   * 
   * @return number of cell to put aIMark to prevent player to win , or NOTHING_TO_PREVENT
   */
  int blockPlayerWin() {
    int equalCount = 0;
    int emptyI = 0, emptyJ = 0;
    int winCombinationsNumber = fieldSize * 2 + 2;
    
    for (int k = 0; k < winCombinationsNumber; k++) {
      equalCount = 0;
      for (int i = 0; i < fieldSize; i++)
        for (int j = 0; j < fieldSize; j++) {
          if (fieldArray[i][j] == playerMark && (getWinCombination(k, i, j) == 1)) 
            equalCount++;  // count number of player marks , that belong to win combination
          
          if ((fieldArray[i][j] == 0) && (getWinCombination(k, i, j) == 1)) {
            emptyI = i;    // coordinates of empty cell , that belongs to win combination 
            emptyJ = j;
          }
          
          if ((fieldArray[i][j] == aIMark) && (getWinCombination(k, i, j) == 1)) { // already non-win 
            j = fieldSize;                                                         // combination
            i = fieldSize; // check next combination
            equalCount = 0;
          }
        }
      if (equalCount == (fieldSize - 1)) {
        return (emptyI * fieldSize + emptyJ);   // cell number
      }
    }
    return NOTHING_TO_PREVENT;
  }

  /**
   * checks, if A.I. has a possibility to put 3 or 4 in row
   * 
   * @param mode "0" to check if it possible to win on current turn 
   *             "1" to check if it possible to put 3 or 4 in row
   * @return number of cell if it possible , or NO_POSSIBILITY
   */
  int checkAIWinPossibility(int mode) {
    int equalCount = 0;
    int emptyI = 0, emptyJ = 0;
    int winCombinationsNum = fieldSize * 2 + 2;
    for (int k = 0; k < winCombinationsNum; k++) {
      equalCount = 0;
      for (int i = 0; i < fieldSize; i++)
        for (int j = 0; j < fieldSize; j++) {
          if (fieldArray[i][j] == aIMark && getWinCombination(k, i, j) == 1)
            equalCount++;   // count number of AI marks , that belong to win combination
          if ((fieldArray[i][j] == 0) && (getWinCombination(k, i, j) == 1)) {
            emptyI = i;     // coordinates of empty cell , that belongs to win combination 
            emptyJ = j;
          }
          if ((fieldArray[i][j] == playerMark) && (getWinCombination(k, i, j) == 1)) { // already non-win 
            j = fieldSize;                                                             // combination
            i = fieldSize; // check next   
            equalCount = 0;
          }
        }
      if ((equalCount == (fieldSize - 1) && mode == 0)
          || (equalCount >= (fieldSize - 2) && mode == 1)) {
        return (emptyI * fieldSize + emptyJ);   // number of cell to put in row
      }
    }
    return NO_POSSIBILITY;
  }

  /**
   * if player puts "X" ("0") to border on first turn , put "0" ("x") to prevent him to make "fork"
   * 
   * @param x coordinates of "x"
   * @param y
   * @return number of cell to put "0"
   */
  int getOppositePosition(int x, int y) {
    // 4 corners
    if (x == 0 && y == 0)
      return (fieldSize - 1) * fieldSize + (fieldSize - 1);
    if (x == (fieldSize - 1) && y == 0)
      return (fieldSize - 1) * fieldSize;
    if (x == 0 && y == (fieldSize - 1))
      return (fieldSize - 1);
    if (x == (fieldSize - 1) && y == (fieldSize - 1))
      return 0;
    // 4 sides
    if (x == 0)
      return y * fieldSize + (fieldSize - 1); // left
    if (x == (fieldSize - 1))
      return (y * fieldSize); // right
    if (y == 0)
      return (fieldSize - 1) * fieldSize + x; // top
    if (y == (fieldSize - 1))
      return x; // bottom
    return 0; // if player puts "x" ("0") neither to side nor to corner, put "0" ("X") to first cell
  }

  /**
   * reset grid state to initial
   */
  void resetLogic() {
    for (int i = 0; i < fieldSize; i++)
      for (int j = 0; j < fieldSize; j++)
        fieldArray[i][j] = 0;
    playerMark = CROSS_MARK;
    aIMark = ZERO_MARK;
    turn = 0;   //first turn
    autoGameEnabled = false;
  }

  /**
   * put "x", then decide, where to put "0"
   * 
   * @param x coordinates of "x"
   * @param y
   * @return number of cell to put "0" , "-1" if game is over
   */
  int checkCell(int x, int y) {
    fieldArray[y][x] = playerMark;
    int i, j;
    if (checkWin() != GAME_IS_NOT_OVER)    // if player wins on this turn
      return -1;            // do nothing
    turn++;
    
    if (turn == 1 && difficultyLevel > EASY) {
      int cellNumber = getOppositePosition(x, y);    // if player puts mark to corner or to side
      i = cellNumber / fieldSize;                    // put to opposite corner (side)
      j = cellNumber % fieldSize;
      if (!autoGameEnabled)
        fieldArray[i][j] = aIMark;
      return cellNumber;
    }
    
    int cellNumber;
    if (turn >= 3) {                    // check, if A.I. can win right on this turn
      cellNumber = checkAIWinPossibility(0);
      if (cellNumber != NO_POSSIBILITY) {
        i = cellNumber / fieldSize;
        j = cellNumber % fieldSize;
        if (!autoGameEnabled)
          fieldArray[i][j] = aIMark;
        return cellNumber;
      }
    }

    cellNumber = blockPlayerWin();            // prevent player to win if he can put 3-rd (4-th)
    if (cellNumber != NOTHING_TO_PREVENT) {   // in row on next turn
      i = cellNumber / fieldSize;
      j = cellNumber % fieldSize;
      if (!autoGameEnabled)
        fieldArray[i][j] = aIMark;
      return cellNumber;
    }
    
    if (difficultyLevel > MEDIUM) {      // prevent player to make fork
      cellNumber = blockFork();          // if such possibility exists
      if (cellNumber != NOTHING_TO_PREVENT) {
        return cellNumber;
      }
    }
    
    cellNumber = checkAIWinPossibility(1);   // check, if A.I. can put mark in row to win on next turns
    if (cellNumber != NO_RESULT) {
      i = cellNumber / fieldSize;
      j = cellNumber % fieldSize;
      if (!autoGameEnabled)
        fieldArray[i][j] = aIMark;
      return cellNumber;
    }
    
    int random = randomGenerator.nextInt(fieldSize * fieldSize); // if still not decided where to put "0"
    int direction = 1;                                 // increment value
    for (int shift = 0;; shift += direction) {         // select random cell and find
      cellNumber = shift + random;                            // free cell after selected
      if (cellNumber > (fieldSize * fieldSize - 1)) {         
        shift = 0;                                     // if not found, use
        direction = -1;                                // opposite search direction
        continue;
      }
      i = cellNumber / fieldSize;
      j = cellNumber % fieldSize;
      if (fieldArray[i][j] == 0) {
        if (!autoGameEnabled)
          fieldArray[i][j] = aIMark;
        break;
      }
    }

    return (i * fieldSize + j);                        // cell number
  }

  int getCell(int x, int y) {
    return fieldArray[y][x];
  }

  /**
   * check if someone wins
   * 
   * @return ZEROS_WIN , CROSSES_win , DRAW , GAME_IS_NOT_OVER
   */
  int checkWin() {
    int equalCount = 0, playerequalCount = 0;
    int winCombinationsNum = fieldSize * 2 + 2;
    for (int k = 0; k < winCombinationsNum; k++) {
      equalCount = 0;
      playerequalCount = 0;
      for (int i = 0; i < fieldSize; i++) {
        for (int j = 0; j < fieldSize; j++) {
          if ((fieldArray[i][j] == playerMark) && (getWinCombination(k, i, j) == 1)) { // compare "X" marks
            playerequalCount++;                                                        // with win combinations
          }
          if ((fieldArray[i][j] == aIMark) && (getWinCombination(k, i, j) == 1)) {     // compare "0" marks
            equalCount++;                                                              // with win combinations
          }
        }
        if (equalCount == fieldSize) {      // if 3 or 4 in row
          return ZEROS_WIN;
        }
        if (playerequalCount == fieldSize) {
          return CROSSES_WIN;
        }
      }
    }
    equalCount = 0;
    for (int i = 0; i < fieldSize; i++)     // check if all cells are occupied with "X" and "0"
      for (int j = 0; j < fieldSize; j++)
        if (fieldArray[i][j] != 0)
          equalCount++;
    if (equalCount == (fieldSize * fieldSize))  // if all cell are occupied and no win combination match
      return DRAW;                              // nobody wins
    return GAME_IS_NOT_OVER;
  }
}


