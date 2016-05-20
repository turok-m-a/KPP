package crosses_zeroes;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scala.Int;
import scala.Tuple2;

/**
 * adds GUI elements to Pane
 */
public class UserInterface implements Constants {
  private ButtonController buttons;
  private GameLogic field;
  private Pane root;
  private Scene mainWindowScene;
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

    final ImageView saveImage = new ImageView(new Image("save.png"));
    final ImageView loadImage = new ImageView(new Image("load.png"));
    final ImageView newGameImage = new ImageView(new Image("newgame.png"));
    final ImageView autoGameImage = new ImageView(new Image("computer.png"));
    final ImageView optionsImage = new ImageView(new Image("options.png"));
    final ImageView sortingImage = new ImageView(new Image("sorting.png"));
    final ImageView statisticsImage = new ImageView(new Image("chart.png"));
    final ImageView sequencesImage = new ImageView(new Image("copy_file.png"));

    Label sizeLabel = new Label();
    sizeLabel.setText("Размер поля:");
    sizeLabel.setTranslateX(150);
    sizeLabel.setTranslateY(10);

    Label levelLabel = new Label();
    levelLabel.setText("Сложность:");
    levelLabel.setTranslateX(150);
    levelLabel.setTranslateY(70);

    RadioButton radioThree = new RadioButton(); //buttons for choosing grid size
    radioThree.setText("3x3");
    radioThree.setTranslateX(150);
    radioThree.setTranslateY(30);
    radioThree.setSelected(true);

    RadioButton radioFour = new RadioButton();
    radioFour.setTranslateX(150);
    radioFour.setTranslateY(50);
    radioFour.setText("4x4");

    RadioButton radioEasy = new RadioButton(); //buttons for choosing difficulty level
    radioEasy.setTranslateX(150);
    radioEasy.setTranslateY(90);
    radioEasy.setText("Низкая");

    RadioButton radioMedium = new RadioButton();
    radioMedium.setTranslateX(150);
    radioMedium.setTranslateY(110);
    radioMedium.setText("Средняя");
    radioMedium.setSelected(true);

    RadioButton radioHard = new RadioButton();
    radioHard.setTranslateX(150);
    radioHard.setTranslateY(130);
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

    Button newGameButton = new Button("Новая игра", newGameImage);
    newGameButton.setContentDisplay(ContentDisplay.LEFT);
    newGameButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        buttons.initButtons();
      }
    });
    newGameButton.setTranslateX(195);
    newGameButton.setTranslateY(50);
    newGameButton.setPrefSize(125, 30);

    Button autoGameButton = new Button("Автоигра", autoGameImage);
    autoGameButton.setContentDisplay(ContentDisplay.LEFT);
    autoGameButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        buttons.autoGame();
      }
    });
    autoGameButton.setTranslateX(195);
    autoGameButton.setTranslateY(10);
    autoGameButton.setPrefSize(125, 30);

    //add load and save buttons
    final FileChooser fileChooser = new FileChooser();
    //File gameReplayFile;
    Button loadButton = new Button("Загрузить", loadImage); // load replay
    loadButton.setTranslateX(330);
    loadButton.setTranslateY(10);
    loadButton.setPrefSize(125, 30);
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
    loadButton.setContentDisplay(ContentDisplay.LEFT);
    Button saveButton = new Button("Сохранить", saveImage); // save button
    saveButton.setTranslateX(330);
    saveButton.setTranslateY(50);
    saveButton.setPrefSize(125, 30);
    saveButton.setContentDisplay(ContentDisplay.LEFT);
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

    TextField sortTime = new TextField();
    sortTime.setTranslateY(125);
    sortTime.setTranslateX(10);
    sortTime.setPrefSize(150, 15);

    Button scalaSortButton = new Button("Сорт. Scala по поз. ходов");
    scalaSortButton.setTranslateX(10);
    scalaSortButton.setTranslateY(35);
    scalaSortButton.setPrefSize(300, 10);
    scalaSortButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ScalaSorter sorter = new ScalaSorter();
        long startTime = System.currentTimeMillis();
        sorter.sort(0, NUMBER_OF_REPLAYS,SORT_BY_POSITIONS);
        long stopTime = System.currentTimeMillis() - startTime;
        sortTime.setText(Long.toString(stopTime));
      }
    });

    Button javaSortButton = new Button("Сорт. java по поз. ходов");
    javaSortButton.setTranslateX(10);
    javaSortButton.setTranslateY(5);
    javaSortButton.setPrefSize(300, 10);
    javaSortButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        JavaSorter sorter = new JavaSorter(0,NUMBER_OF_REPLAYS);
        long startTime = System.currentTimeMillis();
        sorter.sort(SORT_BY_POSITIONS);
        long stopTime = System.currentTimeMillis() - startTime;
        sortTime.setText(Long.toString(stopTime));
      }
    });

    Button javaQuickSort = new Button("Сорт. java по числу ходов");
    javaQuickSort.setTranslateX(10);
    javaQuickSort.setTranslateY(95);
    javaQuickSort.setPrefSize(300, 10);
    javaQuickSort.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        JavaSorter sorter = new JavaSorter(0,NUMBER_OF_REPLAYS);
        long startTime = System.currentTimeMillis();
        sorter.sort(SORT_BY_TURNS_NUMBER);
        long stopTime = System.currentTimeMillis() - startTime;
        sortTime.setText(Long.toString(stopTime));
      }
    });

    Button scalaQuickSort = new Button("Сорт. Scala по числу ходов");
    scalaQuickSort.setTranslateX(10);
    scalaQuickSort.setTranslateY(65);
    scalaQuickSort.setPrefSize(300, 10);
    scalaQuickSort.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ScalaSorter sorter = new ScalaSorter();
        long startTime = System.currentTimeMillis();
        sorter.sort(0, NUMBER_OF_REPLAYS,SORT_BY_TURNS_NUMBER);
        long stopTime = System.currentTimeMillis() - startTime;
        sortTime.setText(Long.toString(stopTime));
      }
    });

    Pane sortPane = new Pane();
    ImageView sortBackground = new ImageView(new Image("gradient2.png", 500, 500,
        false, true));
    sortPane.getChildren().add(sortBackground);
    Scene sortScene = new Scene(sortPane, 320, 155);

    Button showSort = new Button("Сортировки",sortingImage); // show sort menus
    showSort.setContentDisplay(ContentDisplay.LEFT);
    showSort.setTranslateX(10);
    showSort.setTranslateY(10);
    showSort.setPrefSize(125, 30);
    showSort.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainStage.setScene(sortScene);
        mainStage.show();
      }
    });

    ImageView statisticsBackground = new ImageView(new Image("gradient2.png", 500, 500,
        false, true));
    Pane statisticsPane = new Pane();
    Scene statisticsScene = new Scene(statisticsPane, 320, 155);
    statisticsPane.getChildren().add(statisticsBackground);

    Button showStatistics = new Button("Статистика",statisticsImage); // show sort menu
    showStatistics.setContentDisplay(ContentDisplay.LEFT);
    showStatistics.setTranslateX(10);
    showStatistics.setTranslateY(45);
    showStatistics.setPrefSize(125, 30);
    showStatistics.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainStage.setScene(statisticsScene);
        mainStage.show();
      }
    });
    ImageView optionsBackground = new ImageView(new Image("gradient2.png", 500, 500,
        false, true));
    ImageView sequencesBackground = new ImageView(new Image("gradient2.png", 500, 500,
        false, true));
    Pane optionsPane = new Pane();
    Scene optionsScene = new Scene(optionsPane, 320, 155);
    optionsPane.getChildren().add(optionsBackground);
    Pane sequencesPane = new Pane();
    Scene sequencesScene = new Scene(sequencesPane, 320, 155);
    sequencesPane.getChildren().add(sequencesBackground);
    Label sequenceInfo = new Label();
    sequenceInfo.setTranslateX(10);
    sequenceInfo.setTranslateY(5);

    Button closeSort = new Button("Закрыть"); // close sort menu
    closeSort.setTranslateX(160);
    closeSort.setTranslateY(125);
    closeSort.setPrefSize(150, 15);
    closeSort.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainStage.setScene(optionsScene);
        mainStage.show();
      }
    });

    Button closeStatistics = new Button("Закрыть"); // close statistics menu
    closeStatistics.setTranslateX(160);
    closeStatistics.setTranslateY(125);
    closeStatistics.setPrefSize(150, 15);
    closeStatistics.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainStage.setScene(optionsScene);
        mainStage.show();
      }
    });

    Button closeOptions = new Button("Закрыть"); // close options menu
    closeOptions.setTranslateX(10);
    closeOptions.setTranslateY(80);
    closeOptions.setPrefSize(125, 30);
    closeOptions.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainStage.setScene(mainWindowScene);
        mainStage.show();
      }
    });

    GridPane statisticsGrid = new GridPane();
    statisticsGrid.setTranslateX(75);
    statisticsGrid.setTranslateY(25);

    Button showOptions = new Button("Опции", optionsImage); // close options menu
    showOptions.setContentDisplay(ContentDisplay.LEFT);
    showOptions.setTranslateX(195);
    showOptions.setTranslateY(90);
    showOptions.setPrefSize(125, 30);
    showOptions.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainWindowScene = mainStage.getScene();
        mainStage.setScene(optionsScene);
        mainStage.show();
      }
    });

    Label info = new Label("Популярность клеток на выбранном ходу");
    Label turnSelectInfo = new Label("Выбранный ход:");
    turnSelectInfo.setTranslateX(15);
    turnSelectInfo.setTranslateY(100);

    TextField turnSelect = new TextField("0");
    turnSelect.setTranslateX(160);
    turnSelect.setTranslateY(95);
    turnSelect.setPrefWidth(35);

    Label arrayOfLabels[][] = new Label[MAX_FIELD_SIZE][MAX_FIELD_SIZE];
    Button statistics = new Button("обновить"); // close sort menu
    statistics.setTranslateX(10);
    statistics.setTranslateY(125);
    statistics.setPrefSize(150, 15);
    statistics.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Statistics st = new Statistics();
        Integer turn = new Integer(turnSelect.getText());
        int[] statistics =  st.countStatistics(0, 50, turn);
        int currentSize = field.fieldSize;
        for (int i = 0; i < currentSize; i++)
          for (int j = 0; j < currentSize; j++) {
            arrayOfLabels[i][j].setText(Integer.toString(statistics[i*currentSize+j]));
          }
      }
    });

    Button findSequence = new Button("Повторы",sequencesImage);
    findSequence.setContentDisplay(ContentDisplay.LEFT);
    findSequence.setTranslateX(10);
    findSequence.setTranslateY(115);
    findSequence.setPrefSize(125, 30);
    findSequence.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainStage.setScene(sequencesScene);
        mainStage.show();
        SequenceFinder sf = new SequenceFinder();
        int result[] = sf.findSequence();
        sequenceInfo.setText("Наиболее часто встречающаяся\nпоследовательность: "
           + Integer.toString(result[0]) + "\nПовторов последовательности: "
           + Integer.toString(result[1]) + "\n\nНаименее часто встречающаяся\nпоследовательность: "
           + Integer.toString(result[2]) + "\nПовторов последовательности: "
           + Integer.toString(result[3]));
      }
    });

    Button closeSequences = new Button("Закрыть"); // close sequences menu
    closeSequences.setTranslateX(10);
    closeSequences.setTranslateY(120);
    closeSequences.setPrefSize(300, 30);
    closeSequences.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainStage.setScene(optionsScene);
        mainStage.show();
      }
    });

    for (int i = 0; i < MAX_FIELD_SIZE; i++)
      for (int j = 0; j < MAX_FIELD_SIZE; j++) {
        arrayOfLabels[i][j] = new Label("-");
        arrayOfLabels[i][j].setMinWidth(50);
        statisticsGrid.add(arrayOfLabels[i][j], j, i);
      }
    optionsPane.getChildren().addAll(showSort, showStatistics, radioThree, radioFour, sizeLabel,
        levelLabel, radioHard, radioMedium, radioEasy, closeOptions, findSequence);
    statisticsPane.getChildren().addAll(closeStatistics,statisticsGrid,statistics,
        turnSelect, info, turnSelectInfo);
    sortPane.getChildren().addAll(javaSortButton,scalaSortButton,closeSort,
        scalaQuickSort, javaQuickSort, sortTime, showOptions);
    root.getChildren().addAll(newGameButton, autoGameButton, loadButton, saveButton,
        showOptions);
    sequencesPane.getChildren().addAll(sequenceInfo, closeSequences);
  }
}
