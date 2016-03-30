package crosses_zeroes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.LinkedList;

/**
 * reads and writes game state from and to file
 *
 */
class GameReplay implements Constants {
  int numberOfTurns = 0;
  int currentReplayableTurn;
  LinkedList<int[][]> gameStates = new LinkedList<>();
  /**
   *   reads game states from list and saves to file
   *   @param gameStateFile file to save
   *   @param field field to save. it`s necessary to get info about field (size, number of turns)
   */
  void saveGame(File gameStateFile, GameLogic field) { // read from list to file
    try (DataOutputStream gameStateStream =
        new DataOutputStream(new FileOutputStream(gameStateFile))) {
      gameStateStream.writeInt(field.turn);
      gameStateStream.writeInt(field.fieldSize);
      // get state for turns from first to last
      for (int currentTurn = 0; currentTurn < gameStates.size(); currentTurn++) {
        int[][] bufferArray;
        bufferArray = getState(currentTurn);
        for (int i = 0; i < field.fieldSize; i++)
          for (int j = 0; j < field.fieldSize; j++) {
            gameStateStream.writeInt(bufferArray[i][j]);
          }
      }
      gameStateStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("error: cannot save to file");
      return;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("error: IO error");
      return;
    }
  }
/**
 * add current field state to list
 * @param field field to add
 */
  void addCurrentState(GameLogic field) { // add state to list
    int[][] tempState = new int[field.fieldSize][field.fieldSize];
    for (int i = 0; i < field.fieldSize; i++)
      for (int j = 0; j < field.fieldSize; j++) {
        tempState[i][j] = field.fieldArray[i][j];
      } // System.arraycopy doesn`t work here, because size of 2-dim array is changable
    gameStates.add(tempState);
  }
/**
 * get field state from list
 * @param stateIndex number of state
 * @return state (2 dim. array)
 */
  int[][] getState(int stateIndex) {
    return gameStates.get(stateIndex);
  }
/**
 * delete all states from list
 */
  void resetReplay() {
    gameStates.clear();
  }
/**
 * read replay from file, set up field (size, number of turns to replay), copy states to list
 * @param gameStateFile file to read
 * @param field field to set up
 */
  void loadGame(File gameStateFile, GameLogic field) { // read to list from file
    try (DataInputStream gameStateStream =
        new DataInputStream(new FileInputStream(gameStateFile))) {
      gameStates.clear();
      currentReplayableTurn = 0;
      field.turn = gameStateStream.readInt();
      field.fieldSize = gameStateStream.readInt();
      if (field.fieldSize > MAX_FIELD_SIZE || field.turn > (MAX_FIELD_SIZE * MAX_FIELD_SIZE)) {
        throw new IOException(); // file is corrupted
      }
      // get state for turns from first to last
      for (int currentTurn = 0; currentTurn < field.turn; currentTurn++) {
        int[][] bufferArray = new int[field.fieldSize][field.fieldSize];
        for (int i = 0; i < field.fieldSize; i++)
          for (int j = 0; j < field.fieldSize; j++) {
            bufferArray[i][j] = gameStateStream.readInt();
          }
        gameStates.add(bufferArray);    // add one more state
      }
    } catch (FileNotFoundException e) {
      System.out.println("error: cannot open file");
      return;
    } catch (IOException e) {
      System.out.println("error: IO error, it is possible that the file is corrupted");
      field.fieldSize = DEFAULT_FIELD_SIZE; // file is corrupted, fall back to initial state
      field.resetLogic();
      resetReplay();
      return;
    }
  }
/**
 * load next state from list to field
 * @return game state (2 dim array)
 */
  int[][] getNextState() {
    currentReplayableTurn++;
    return gameStates.get(currentReplayableTurn - 1);
  }
}
