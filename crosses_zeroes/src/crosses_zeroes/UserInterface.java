package crosses_zeroes;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * adds GUI elements to Pane
 */
public class UserInterface implements Constants {
  private ButtonController buttons;
  private GameLogic field;
  private Pane root;
  File gameReplayFile;
  Stage mainStage;
  UserInterface(ButtonController tempButtons, GameLogic tempField, Pane tempRoot, Stage tempStage) {
    buttons = tempButtons;
    field = tempField;
    root = tempRoot;
    mainStage = tempStage;
  }

  void init() {
    ToggleGroup switchDifficulty = new ToggleGroup();
    ToggleGroup switchFieldSize = new ToggleGroup();

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

    switchDifficulty.getToggles().addAll(radioMedium,radioEasy,radioHard);
    switchFieldSize.getToggles().addAll(radioFour,radioThree);

    radioEasy.setOnAction(new EventHandler<ActionEvent>() {  //set event handlers for buttons
      public void handle(ActionEvent event) {
        field.difficultyLevel = EASY;
      }
    });

    radioMedium.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        field.difficultyLevel = MEDIUM;
      }
    });

    radioHard.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        field.difficultyLevel = HARD;
      }
    });

    radioFour.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        buttons.setFour();
      }
    });

    radioThree.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        buttons.setThree();
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

    //add load and save buttons
    final FileChooser fileChooser = new FileChooser();
    //File gameReplayFile;
    Button loadButton = new Button("Загрузить"); // load replay
    loadButton.setTranslateX(350);
    loadButton.setTranslateY(10);
    loadButton.setPrefSize(100, 30);
    loadButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          gameReplayFile = fileChooser.showOpenDialog(mainStage);
          buttons.showReplay(gameReplayFile);
        } catch (NullPointerException e) {
          System.out.println("file not selected!"); // user clicked "cancel"
        };
      }
    });
    Button saveButton = new Button("Сохранить"); // save button
    saveButton.setTranslateX(350);
    saveButton.setTranslateY(50);
    saveButton.setPrefSize(100, 30);
    saveButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          gameReplayFile = fileChooser.showSaveDialog(mainStage);
          field.saveReplay(gameReplayFile);
        } catch (NullPointerException e) {
          System.out.println("file not selected!"); // user clicked "cancel"
        };
      }
    });

    root.getChildren().addAll(newGameButton, radioThree, radioFour, sizeLabel,
        levelLabel, radioHard, radioMedium);
    root.getChildren().addAll(autoGameButton, radioEasy, loadButton, saveButton);
  }
}
