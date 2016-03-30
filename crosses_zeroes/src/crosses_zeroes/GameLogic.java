package crosses_zeroes;

import java.io.File;
import java.util.Random;

/**
 * contains A.I. algorithms, stores current game state
 */
public class GameLogic implements Constants {
  public int fieldArray[][] = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
  public int fieldSize = DEFAULT_FIELD_SIZE;
  public int turn = 0;
  public int difficultyLevel = MEDIUM;
  public boolean autoGameEnabled = false; // A.I. vs A.I.
  /**
   * {@value this.fieldSize} grid size
   * {@value this.turn} current turn
   * {@value this.autoGameEnabled} auto game mode enabled
   * {@value this.difficultyLevel} difficulty level
   */
  //A.I. plays "zeros" , user plays "crosses"
  private int playerMark = CROSS_MARK, aIMark = ZERO_MARK;
  private Random randomGenerator = new Random();
  private GameReplay replayModule = new GameReplay();
  File gameStateFile;
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
   * checks, if it possible, that player can make a "fork" on the next turn.
   * if possible , tries to prevent "fork"
   *
   * @return number of cell to put "aIMark" if fork was prevented, or NOTHING_TO_PREVENT
   */
  int blockFork() {
    int firstAIMark, firstAIMarkRow, firstAIMarkColumn;
    for (int i = 0; i < fieldSize; i++)
      for (int j = 0; j < fieldSize; j++) {
        if (fieldArray[i][j] != EMPTY_MARK) {
          continue;
        }
        fieldArray[i][j] = playerMark; // put "x" , as if it was a player
        firstAIMark = blockPlayerWin(); // try to prevent player to win
        if (blockPlayerWin() != NO_RESULT) {
          firstAIMarkRow = firstAIMark / fieldSize;
          firstAIMarkColumn = firstAIMark % fieldSize;
          fieldArray[firstAIMarkRow][firstAIMarkColumn] = aIMark;
        } else {
          fieldArray[i][j] = EMPTY_MARK;
          continue;
        }

        if (blockPlayerWin() != NO_RESULT) { // if after preventing it still possible
          fieldArray[firstAIMarkRow][firstAIMarkColumn] = 0; // for player to win
          if (!autoGameEnabled) {// then it`s a "fork"
            fieldArray[i][j] = aIMark;
          }
          return (i * fieldSize + j);   // cell number
        }

        fieldArray[i][j] = EMPTY_MARK;
        fieldArray[firstAIMarkRow][firstAIMarkColumn] = EMPTY_MARK;
      }
    return NOTHING_TO_PREVENT;
  }

  /**
   * To check if someone wins, or to prevent player to win, it necessary to compare current grid
   * state with an array that contains win combinations (3,4 and so on in row). such 3-dim. array
   * should contain all combinations for 3x3 4x4 5x5 ... grids. This function replaces hard-coded
   * array, and can be used for any grid size
   *
   * @param combinationNumber current number of combination to check ( k < this.fieldSize*2+2 )
   * @param posY coordinate to check
   * @param posX
   * @return BELONGS_TO_WIN_COMBINATION if cell belongs to a win combination ,
   * DOESNT_BELONG_TO_WIN_COMBINATION if not
   */
  private int getWinCombination(int combinationNumber, int posY, int posX) {
    if (combinationNumber < fieldSize) { // strings
      if (posY == combinationNumber) {
        return BELONGS_TO_WIN_COMBINATION;
      }
      return DOESNT_BELONG_TO_WIN_COMBINATION;
    }
    if (combinationNumber >= fieldSize && combinationNumber < (fieldSize * 2)) { // columns
      if ((posX + fieldSize) == combinationNumber) {
        return BELONGS_TO_WIN_COMBINATION;
      }
      return DOESNT_BELONG_TO_WIN_COMBINATION;
    }
    if (combinationNumber == (fieldSize * 2) && posX == posY) { // diagonals
      return BELONGS_TO_WIN_COMBINATION;
    }
    if (combinationNumber == (fieldSize * 2 + 1) && posX == (fieldSize - posY - 1)) {
      return BELONGS_TO_WIN_COMBINATION;
    }
    return DOESNT_BELONG_TO_WIN_COMBINATION;
  }

  /**
   * Prevents player to win on the next turn
   *
   * @return number of cell to put aIMark to prevent player to win , or NOTHING_TO_PREVENT
   */
  int blockPlayerWin() {
    int equalCount = 0;
    int emptyI = 0, emptyJ = 0;
    // fieldSize * 2 for rows and columns , 2 more for diagonals
    int winCombinationsNumber = fieldSize * 2 + 2;
    for (int k = 0; k < winCombinationsNumber; k++) {
      equalCount = 0;
      for (int i = 0; i < fieldSize; i++)
        for (int j = 0; j < fieldSize; j++) {
          if (fieldArray[i][j] == playerMark
              && (getWinCombination(k, i, j) == BELONGS_TO_WIN_COMBINATION)) {
            equalCount++;  // count number of player marks , that belong to win combination
          }
          if ((fieldArray[i][j] == EMPTY_MARK)
              && (getWinCombination(k, i, j) == BELONGS_TO_WIN_COMBINATION)) {
            emptyI = i;    // coordinates of empty cell , that belongs to win combination
            emptyJ = j;
          }

          if ((fieldArray[i][j] == aIMark)
              && (getWinCombination(k, i, j) == BELONGS_TO_WIN_COMBINATION)) {
            j = fieldSize; // already non-win combination
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
          if (fieldArray[i][j] == aIMark && getWinCombination(k, i, j) == 1) {
            equalCount++;   // count number of AI marks , that belong to win combination
          }
          if ((fieldArray[i][j] == EMPTY_MARK) && (getWinCombination(k, i, j) == 1)) {
            emptyI = i;     // coordinates of empty cell , that belongs to win combination
            emptyJ = j;
          }
          if ((fieldArray[i][j] == playerMark) && (getWinCombination(k, i, j) == 1)) {
            j = fieldSize;                               // already non-win combination
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
   * if player puts "X" ("0") to border on first turn , put "0" ("x") to prevent
   * him to make "fork"
   * @param posX coordinates of "x"
   * @param posY
   * @return number of cell to put "0"
   */
  int getOppositePosition(int posX, int posY) {
    // 4 corners
    if (posX == 0 && posY == 0) {
      return (fieldSize - 1) * fieldSize + (fieldSize - 1);
    }
    if (posX == (fieldSize - 1) && posY == 0) {
      return (fieldSize - 1) * fieldSize;
    }
    if (posX == 0 && posY == (fieldSize - 1)) {
      return (fieldSize - 1);
    }
    if (posX == (fieldSize - 1) && posY == (fieldSize - 1)) {
      return 0;
    }
    // 4 sides
    if (posX == 0) {
      return posY * fieldSize + (fieldSize - 1); // left
    }
    if (posX == (fieldSize - 1)) {
      return (posY * fieldSize); // right
    }
    if (posY == 0) {
      return (fieldSize - 1) * fieldSize + posX; // top
    }
    if (posY == (fieldSize - 1)) {
      return posX; // bottom
    }
    // if player puts "x" ("0") neither to side nor to corner, put "0" ("X") to first cell
    return 0;
  }

  /**
   * reset grid state to initial
   */
  void resetLogic() {
    fieldArray = new int[fieldSize][fieldSize];
    for (int i = 0; i < fieldSize; i++)
      for (int j = 0; j < fieldSize; j++) {
        fieldArray[i][j] = EMPTY_MARK;
      }
    playerMark = CROSS_MARK;
    aIMark = ZERO_MARK;
    turn = 0;   //first turn
    autoGameEnabled = false;
    replayModule.resetReplay();
  }

  /**
   * put "x", then decide, where to put "0" (player vs A.I. mode)
   * decide where to put "x" or "0" (A.I. vs A.I. mode)
   * @param posX coordinates of "x" or "0"
   * @param posY
   * @return number of cell to put "0" , "-1" if game is over
   */
  int checkCell(int posX, int posY) {
    fieldArray[posY][posX] = playerMark;
    if (!autoGameEnabled ) {            // no need to add, in autogame mode it was
    replayModule.addCurrentState(this); // added on previous turn .
    //add new state after each turn
    turn++;// count turns for two players. in autogame mode this function is called once for each
    }      // turn of zeros and crosses. when autogame mode disabled this function is called once
    // for each pair of turns
    int row, column;
    if (checkWin() != GAME_IS_NOT_OVER) {   // if player wins on this turn
      replayModule.addCurrentState(this);
      turn ++;
      return NO_RESULT;            // do nothing
    }
    turn++;

    if (turn == FIRST_TURN && difficultyLevel > EASY) {
      int cellNumber = getOppositePosition(posX, posY); // if player puts mark to corner or to
      row = cellNumber / fieldSize;                       // side, put to opposite corner (side)
      column = cellNumber % fieldSize;
      if (!autoGameEnabled) {
        fieldArray[row][column] = aIMark;
      }
      replayModule.addCurrentState(this);
      return cellNumber;
    }

    int cellNumber;
    if (turn >= THIRD_TURN) {                    // check, if A.I. can win right on this turn
      cellNumber = checkAIWinPossibility(0);
      if (cellNumber != NO_POSSIBILITY) {
        row = cellNumber / fieldSize;
        column = cellNumber % fieldSize;
        if (!autoGameEnabled) {
          fieldArray[row][column] = aIMark;
        }
        replayModule.addCurrentState(this);
        return cellNumber;
      }
    }

    cellNumber = blockPlayerWin();            // prevent player to win if he can put 3-rd (4-th)
    if (cellNumber != NOTHING_TO_PREVENT) {   // in row on next turn
      row = cellNumber / fieldSize;
      column = cellNumber % fieldSize;
      if (!autoGameEnabled) {
        fieldArray[row][column] = aIMark;
      }
      replayModule.addCurrentState(this);
      return cellNumber;
    }

    if (difficultyLevel > MEDIUM) {      // prevent player to make fork
      cellNumber = blockFork();          // if such possibility exists
      if (cellNumber != NOTHING_TO_PREVENT) {
        replayModule.addCurrentState(this);
        return cellNumber;
      }
    }

    // check, if A.I. can put mark in row to win on next turns
    cellNumber = checkAIWinPossibility(1);
    if (cellNumber != NO_RESULT) {
      row = cellNumber / fieldSize;
      column = cellNumber % fieldSize;
      if (!autoGameEnabled) {
        fieldArray[row][column] = aIMark;
      }
      replayModule.addCurrentState(this);
      return cellNumber;
    }

    // if still not decided where to put "0"
    int random = randomGenerator.nextInt(fieldSize * fieldSize);
    int direction = 1;                                 // increment value
    for (int shift = 0;; shift += direction) {         // select random cell and find
      cellNumber = shift + random;                     // free cell after selected
      if (cellNumber > (fieldSize * fieldSize - 1)) {
        shift = 0;                                     // if not found, use
        direction = -1;                                // opposite search direction
        continue;
      }
      row = cellNumber / fieldSize;
      column = cellNumber % fieldSize;
      if (fieldArray[row][column] == EMPTY_MARK) {
        if (!autoGameEnabled) {
          fieldArray[row][column] = aIMark;
        }
        break;
      }
    }
    replayModule.addCurrentState(this);
    return (row * fieldSize + column);                        // cell number
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
          if ((fieldArray[i][j] == playerMark)
              && (getWinCombination(k, i, j) == BELONGS_TO_WIN_COMBINATION)) {
            playerequalCount++; // compare "X" marks with win combinations
          }
          if ((fieldArray[i][j] == aIMark)
              && (getWinCombination(k, i, j) == BELONGS_TO_WIN_COMBINATION)) {
            equalCount++; // compare "0" marks with win combinations
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
      for (int j = 0; j < fieldSize; j++) {
        if (fieldArray[i][j] != EMPTY_MARK) {
          equalCount++;
        }
      }
    if (equalCount == (fieldSize * fieldSize)) {
      return DRAW; // if all cell are occupied and no win combination match nobody wins
    }
    return GAME_IS_NOT_OVER;
  }
  /**
   * saves replay for this field
   * @param replayFile file to save
   */
  void saveReplay(File replayFile){
    replayModule.saveGame(replayFile, this);
  }
  /**
   * loads next turn from replay
   * @return true on success, false if next turn doesn`t exist
   */
  boolean loadTurnFromReplay(){
    //if (replayModule.currentReplayableTurn >= turn) {
    if (replayModule.gameStates.size() <= replayModule.currentReplayableTurn) {
      return false;
    }
    fieldArray = replayModule.getNextState();
    return true;
  }
  /**
   * loads replay for this field
   * @param replayFile file to load
   */
  void loadReplay(File replayFile){
    replayModule.loadGame(replayFile, this);
  }
}
