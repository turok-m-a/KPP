package crosses_zeroes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;

/**
 * adds GUI elements to Pane
 */
public class UserInterface {
  static final int EASY = 1;
  static final int MEDIUM = 3;
  static final int HARD = 5;
  ButtonController buttons;
  GameLogic field;
  Pane root;

  UserInterface(ButtonController b, GameLogic f, Pane r) {
    buttons = b;
    field = f;
    root = r;
  }

  void init() {
    Label sizeLabel = new Label();
    sizeLabel.setText("Размер поля:");
    sizeLabel.setTranslateX(150);
    sizeLabel.setTranslateY(50);
    Label levelLabel = new Label();
    levelLabel.setText("Сложность:");
    levelLabel.setTranslateX(250);
    levelLabel.setTranslateY(50);
    RadioButton radioThree = new RadioButton(); //buttons for choosing grid size
    radioThree.setText("3x3");
    radioThree.setTranslateX(150);
    radioThree.setTranslateY(70);
    radioThree.setSelected(true);
    RadioButton radioFour = new RadioButton();
    radioFour.setTranslateX(150);
    radioFour.setTranslateY(90);
    radioFour.setText("4x4");
    RadioButton radioEasy = new RadioButton(); //buttons for choosing difficulty level
    radioEasy.setTranslateX(250);
    radioEasy.setTranslateY(70);
    radioEasy.setText("Низкая");
    RadioButton radioMedium = new RadioButton();
    radioMedium.setTranslateX(250);
    radioMedium.setTranslateY(90);
    radioMedium.setText("Средняя");
    radioMedium.setSelected(true);
    RadioButton radioHard = new RadioButton();
    radioHard.setTranslateX(250);
    radioHard.setTranslateY(110);
    radioHard.setText("Высокая");
    radioEasy.setOnAction(new EventHandler<ActionEvent>() {     //set event handlers for buttons
      public void handle(ActionEvent event) {
        field.difficultyLevel = EASY;
        radioMedium.setSelected(false);
        radioHard.setSelected(false);
      }
    });
    radioMedium.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        field.difficultyLevel = MEDIUM;
        radioEasy.setSelected(false);
        radioHard.setSelected(false);
      }
    });
    radioHard.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        field.difficultyLevel = HARD;
        radioMedium.setSelected(false);
        radioEasy.setSelected(false);
      }
    });
    radioFour.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        buttons.setFour();
        radioThree.setSelected(false);
        radioFour.setSelected(true);
      }
    });
    radioThree.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        buttons.setThree();
        radioThree.setSelected(true);
        radioFour.setSelected(false);
      }
    });
    Button newGameButton = new Button();
    newGameButton.setText("Новая игра");
    newGameButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        buttons.initButtons();
      }
    });
    newGameButton.setTranslateX(150);
    newGameButton.setTranslateY(10);
    newGameButton.setPrefSize(100, 30);
    Button autoGameButton = new Button();
    autoGameButton.setText("Автоигра");
    autoGameButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        buttons.autoGame();
      }
    });
    autoGameButton.setTranslateX(250);
    autoGameButton.setTranslateY(10);
    autoGameButton.setPrefSize(100, 30);
    root.getChildren().addAll(newGameButton, radioThree, radioFour, sizeLabel, levelLabel, radioHard,
        radioMedium);
    root.getChildren().addAll(autoGameButton, radioEasy);
  }

}
