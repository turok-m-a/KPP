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
public class buttonmgr {


  int fieldsize = 3;
  Image imageZero;
  Image imageCross;
  Image imageEmpty;
  Button[][] ms;
  logic field;
  Stage stage;
  int coord, result;
  boolean flag;

  buttonmgr(Button[][] msbtn, logic ffield, Stage sstage) {
    try {
      imageZero = new Image(getClass().getResourceAsStream("zero.png"));
      imageCross = new Image(getClass().getResourceAsStream("cross.png"));
      imageEmpty = new Image(getClass().getResourceAsStream("empty.png"));
    } catch (NullPointerException o) {
      System.out.println("error:");
      System.out.println("one of these files is missing: zero.png cross.png empty.png");
      System.exit(1);
    }
    ms = msbtn;
    field = ffield;
    stage = sstage;
  }
  /**
   * Resets buttons and field to initial state
   */
  void resetbuttons() {
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++)
        ms[i][j].setGraphic(new ImageView(imageEmpty));
    field.resetlogic();
  }

  /**
   * Blocks buttons, so user can`t put "x" after the game is over
   */
  void blockbuttons() {
    for (int i = 0; i < fieldsize; i++)
      for (int j = 0; j < fieldsize; j++)
        ms[i][j].setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {}
        });;
  }
  /**
   * sets grid size to 4x4
   */
  void setfour() {
    fieldsize = 4;
    field.fieldsize = 4;
    initbuttons();
  }
  /**
   * sets grid size to 3x3
   */
  void setthree() {
    fieldsize = 3;
    field.fieldsize = 3;
    initbuttons();
  }
  /**
   * resets buttons and sets new event handler
   */
  void initbuttons() {
    field.resetlogic();
    resetbuttons();
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++) {
        int ii = i;
        int jj = j;
        if (i >= fieldsize || j >= fieldsize) {
          ms[i][j].setVisible(false);
        } else {
          ms[i][j].setVisible(true);
        }
        ms[i][j].setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            logicfunc(ii, jj);
          }
        });
      }
  }
  /**
   * makes one turn in auto game mode
   * 
   * @return "1" if game is over
   */
  private int autogameturn() {

    if (!flag) {
      if (field.checkwin() >= 0)
        return 1;
      field.switchlogic(1);
      if (result >= 0) {
        coord = field.checkcell(result % field.fieldsize, result / field.fieldsize);
        if (coord >= 0) {
          ms[coord / field.fieldsize][coord % field.fieldsize]
              .setGraphic(new ImageView(imageCross));
        }
      } else
        return 1;
      flag = true;
    } else {
      if (field.checkwin() >= 0)
        return 1;
      field.switchlogic(0);
      if (coord >= 0) {
        result = field.checkcell(coord % field.fieldsize, coord / field.fieldsize);
        if (result >= 0) {
          ms[result / field.fieldsize][result % field.fieldsize]
              .setGraphic(new ImageView(imageZero));
        }
      } else
        return 1;
      flag = false;
    }
    try {
      Thread.sleep(500);
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
      if (autogameturn() == 1) {
        at.stop();
        field.switchlogic(0);
        logicfunc(0, 0);
        field.autogame = 0;
      }
    }
  };
  /**
   * launch auto game (A.I. vs A.I.)
   */
  void autogame() {
    field.autogame = 1;
    if (field.checkwin() >= 0)
      return;
    Random rd = new Random();
    flag = false;
    coord = rd.nextInt(field.fieldsize * field.fieldsize);
    result = field.checkcell(coord % field.fieldsize, coord / field.fieldsize);
    ms[coord / field.fieldsize][coord % field.fieldsize].setGraphic(new ImageView(imageCross));
    ms[result / field.fieldsize][result % field.fieldsize].setGraphic(new ImageView(imageZero));
    at.start();
  }
  /**
   * calls field.getcell(j,i)
   * @param i "y" coordinate
   * @param j "x" coordinate 
   * 
   * receives number of cell, that A.I. selected to make turn 
   * if game is over, shows message to user and calls this.blockbuttons()
   */
  void logicfunc(int i, int j) {
    int result = -1;
    if (field.autogame == 0) {
      if (field.getcell(j, i) == 0)
        ms[i][j].setGraphic(new ImageView(imageCross));
      else
        return;
      result = field.checkcell(j, i);
    }
    int endgame = field.checkwin();
    if (endgame >= 0) {
      blockbuttons();
      Pane message_pane = new Pane();
      Scene prev_scene = stage.getScene();
      Scene message_scene = new Scene(message_pane, 300, 110);
      Label win_message = new Label("123456789");
      Button close_button = new Button();
      close_button.setTranslateY(50);
      close_button.setTranslateX(110);
      win_message.setTranslateX(100);
      win_message.setTranslateY(10);
      message_pane.getChildren().add(close_button);
      message_pane.getChildren().add(win_message);
      close_button.setText("Закрыть");
      close_button.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          stage.setScene(prev_scene);
        }
      });
      switch (endgame) {
        case 0: {
          win_message.setText("Ничья!");
        }
          break;
        case 1: {
          win_message.setText("Победа крестиков!");
        }
          break;
        case 2: {
          win_message.setText("Победа ноликов!");
        }
          break;
        default:
          break;
      }
      stage.setScene(message_scene);
      stage.show();
    }
    if (result >= 0 && field.autogame == 0) {
      i = result / fieldsize;
      j = result % fieldsize;
      ms[i][j].setGraphic(new ImageView(imageZero));
    }
  }
}
