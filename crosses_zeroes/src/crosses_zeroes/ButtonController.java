package crosses_zeroes;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * controls array of buttons
 *
 */
public class ButtonController implements Constants {
  int fieldSize = 3;
  Image imageZero;
  Image imageCross;
  Image imageEmpty;
  Button[][] arrayOfButtons;
  GameLogic field;
  Stage stage;
  int cellNumber, result;
  boolean makeMoveAsZero;

  ButtonController(Button[][] b, GameLogic l, Stage s) {
    arrayOfButtons= b;
    field = l;
    stage = s;
    try {
      imageZero = new Image(getClass().getResourceAsStream("zero.png"));   // pictures of marks
      imageCross = new Image(getClass().getResourceAsStream("cross.png"));
      imageEmpty = new Image(getClass().getResourceAsStream("empty.png"));
    } catch (NullPointerException o) {  // if file not found
      System.out.println("error:");
      System.out.println("one of these files is missing: zero.png cross.png empty.png");
      System.exit(1);
    }
  }
  /**
   * Resets buttons and field to initial state
   */
  void resetButtons() {
    for (int i = 0; i < fieldSize; i++)
      for (int j = 0; j < fieldSize; j++) {
        arrayOfButtons[i][j].setGraphic(new ImageView(imageEmpty));
      }
    field.resetLogic();
  }

  /**
   * Blocks buttons, so user can`t put "x" after the game is over
   */
  void blockButtons() {
    for (int i = 0; i < fieldSize; i++)
      for (int j = 0; j < fieldSize; j++) {
        arrayOfButtons[i][j].setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {}
        });
      }
  }
  /**
   * sets grid size to 4x4
   */
  void setFour() {
    fieldSize = 4;
    field.fieldSize = 4;
    initButtons();
  }
  /**
   * sets grid size to 3x3
   */
  void setThree() {
    fieldSize = 3;
    field.fieldSize = 3;
    initButtons();
  }
  /**
   * resets buttons and sets new event handler
   */
  void initButtons() {
    field.resetLogic();
    resetButtons();
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++) {
        int finalI = i;    //make i and j effectively final             
        int finalJ = j;
        if (i >= fieldSize || j >= fieldSize) {
          arrayOfButtons[i][j].setVisible(false);   // hide buttons for 3x3 grid size
        } else {
          arrayOfButtons[i][j].setVisible(true);    // show buttons for 4x4 grid size
        }
        
        arrayOfButtons[i][j].setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            makeMove(finalI, finalJ);                       // make move when user clicks grid cell
          }
        });
      }
  }
  /**
   * makes one turn in auto game mode
   * 
   * @return STOP_AUTO_GAME if game is over
   */
  private int autoGameTurn() {

    if (!makeMoveAsZero) {                        // AI plays crosses
      if (field.checkWin() != GAME_IS_NOT_OVER) {
        return 1;
      }
      field.switchMode(CROSS_MARK);
      if (result != NO_RESULT) {
        cellNumber = field.checkCell(result % field.fieldSize, result / field.fieldSize);   //get cell number
        if (cellNumber != NO_RESULT) {                                                      //for next turn
          arrayOfButtons[cellNumber / field.fieldSize][cellNumber % field.fieldSize]        //set button picture
              .setGraphic(new ImageView(imageCross));
        }
      } else {
        return 1;
      }
      makeMoveAsZero = true;
    } else {                           // AI plays zeros
      if (field.checkWin() >= 0) {
        return STOP_AUTO_GAME;
      }
      field.switchMode(ZERO_MARK);
      if (cellNumber != NO_RESULT) {
        result = field.checkCell(cellNumber % field.fieldSize, cellNumber / field.fieldSize); //get cell number
        if (result != NO_RESULT) {                                                            //for next turn
          arrayOfButtons[result / field.fieldSize][result % field.fieldSize]                  //set button picture
              .setGraphic(new ImageView(imageZero));
        }
      } else {
        return STOP_AUTO_GAME;
      }
      makeMoveAsZero = false;
    }
    
    try {
      Thread.sleep(500);     // make a pause so user is able to see sequence of turns
    } catch (InterruptedException e) {
    }
    return 0;
  }
  /**
   * animation timer is used to update GUI during auto game
   */
  protected AnimationTimer at = new AnimationTimer() {
    @Override
    public void handle(long now) {
      if (autoGameTurn() == STOP_AUTO_GAME) {
        at.stop();
        field.switchMode(ZERO_MARK);
        makeMove(0, 0);                 // call this function to show message
        field.autoGameEnabled = false;
      }
    }
  };
  /**
   * launch auto game (A.I. vs A.I.)
   */
  void autoGame() {
    field.autoGameEnabled = true;
    if (field.checkWin() != GAME_IS_NOT_OVER) {
      return;
    }
    Random randomGenerator = new Random();
    makeMoveAsZero = false;
    // get cell for first turn
    cellNumber = randomGenerator.nextInt(field.fieldSize * field.fieldSize); 
    // first turn
    result = field.checkCell(cellNumber % field.fieldSize, cellNumber / field.fieldSize);
    arrayOfButtons[cellNumber / field.fieldSize][cellNumber % field.fieldSize].setGraphic(new ImageView(imageCross));
    arrayOfButtons[result / field.fieldSize][result % field.fieldSize].setGraphic(new ImageView(imageZero));
    at.start(); // subsequent turns
  }
  
  /**
   * calls field.getcell(j,i)
   * @param i "y" coordinate
   * @param j "x" coordinate 
   * 
   * receives number of cell, that A.I. selected to make turn 
   * if game is over, shows message to user and calls this.blockButtons()
   */
  void makeMove(int i, int j) {       // make one move and get cell that A.I. selected to make turn
    int result = NO_RESULT;
    if (!field.autoGameEnabled) {     // for autogame mode it is necessary to use Animation Timer       
      if (field.getCell(j, i) == EMPTY_MARK) {
        arrayOfButtons[i][j].setGraphic(new ImageView(imageCross));
      } else {
        return;
      }
      result = field.checkCell(j, i); // result = cell that A.I. selected to make turn 
    }
    
    int gameState = field.checkWin();
    if (gameState != GAME_IS_NOT_OVER) {      //create scene to display message to user
      blockButtons();
      Pane messagePane = new Pane();
      Scene previousScene = stage.getScene(); // save current scene
      Scene messageScene = new Scene(messagePane, 300, 110);
      
      Label winMessage = new Label();
      
      Button closeButton = new Button();
      closeButton.setTranslateY(50);
      closeButton.setTranslateX(110);
      closeButton.setText("Закрыть");
      
      winMessage.setTranslateX(100);
      winMessage.setTranslateY(10);
      
      messagePane.getChildren().add(closeButton);
      messagePane.getChildren().add(winMessage);
      
      closeButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          stage.setScene(previousScene);      // restore previous scene
        }
      });
      
      switch (gameState) {
        case DRAW: {
          winMessage.setText("Ничья!");
        }
          break;
        case CROSSES_WIN: {
          winMessage.setText("Победа крестиков!");
        }
          break;
        case ZEROS_WIN: {
          winMessage.setText("Победа ноликов!");
        }
          break;
        default:
          break;
      }
      stage.setScene(messageScene);                 // show scene with message
      stage.show();
    }
    
    if (result != NO_RESULT && (!field.autoGameEnabled)) { // for autogame mode it 
      i = result / fieldSize;                              // is necessary to use
      j = result % fieldSize;                              // Animation Timer
      arrayOfButtons[i][j].setGraphic(new ImageView(imageZero));
    }
  }
  
}
