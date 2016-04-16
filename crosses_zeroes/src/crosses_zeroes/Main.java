package crosses_zeroes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * main class
 * @author turok-m-a
 *
 */
public class Main extends Application implements Constants {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("крестики-нолики");
    Pane root = new Pane();
    // grid MAX_FIELD_SIZE x MAX_FIELD_SIZE
    Button arrayOfButtons[][] = new Button[MAX_FIELD_SIZE][MAX_FIELD_SIZE];
    GridPane grid = new GridPane();
    grid.setTranslateX(10);
    grid.setTranslateY(10);
    GameLogic field = new GameLogic();
    for (int i = 0; i < MAX_FIELD_SIZE; i++)
      for (int j = 0; j < MAX_FIELD_SIZE; j++) {
        arrayOfButtons[i][j] = new Button();
        grid.add(arrayOfButtons[i][j], j, i);
      }
    root.getChildren().add(grid);
    ButtonController buttons = new ButtonController(arrayOfButtons, field, primaryStage);
    buttons.initButtons();

    UserInterface ui = new UserInterface(buttons, field, root, primaryStage);
    ui.init();

    Scene mainScene = new Scene(root, WINDOW_LENGTH, WINDOW_HEIGTH);
    primaryStage.setScene(mainScene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
