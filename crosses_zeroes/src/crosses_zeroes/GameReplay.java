package crosses_zeroes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * reads and writes game state from and to file
 *
 */
class GameReplay implements Constants {
  int numberOfTurns = 0;
  int winner;
  int currentReplayableTurn;
  LinkedList<int[][]> gameStates = new LinkedList<>();

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

  void addCurrentState(GameLogic field) {   // add state to list
    int[][] tempState = new int[field.fieldSize][field.fieldSize];
    for (int i = 0; i < field.fieldSize; i++)
      for (int j = 0; j < field.fieldSize; j++) {
        tempState[i][j] = field.fieldArray[i][j];
      } // System.arraycopy doesn`t work here, because size of 2-dim array is changable
    gameStates.add(tempState);
  }

  int[][] getState(int stateIndex) {
    return gameStates.get(stateIndex);
  }

  void resetReplay() {
    gameStates.clear();
  }

  void loadGame(File gameStateFile, GameLogic field) { // read to list from file
    try (DataInputStream gameStateStream =
        new DataInputStream(new FileInputStream(gameStateFile))) {
      gameStates.clear();
      currentReplayableTurn = 0;
      field.turn = gameStateStream.readInt();
      field.fieldSize = gameStateStream.readInt();
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
      e.printStackTrace();
      return;
    } catch (EOFException e) {
      return;
    } catch (IOException e) {
      System.out.println("error: IO error");
      e.printStackTrace();
      return;
    }
  }

  int[][] getNextState(GameLogic field) {
    currentReplayableTurn++;
    return gameStates.get(currentReplayableTurn - 1);
  }
}
