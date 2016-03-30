package crosses_zeroes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    GameLogic field = new GameLogic();
    for (int i = 0; i < MAX_FIELD_SIZE; i++)
      for (int j = 0; j < MAX_FIELD_SIZE; j++) {
        arrayOfButtons[i][j] = new Button();
        arrayOfButtons[i][j].setTranslateX(j * 30); // make a grid
        arrayOfButtons[i][j].setTranslateY(i * 30);
        root.getChildren().add(arrayOfButtons[i][j]);
      }

    ButtonController buttons = new ButtonController(arrayOfButtons, field, primaryStage);
    buttons.initButtons();

    UserInterface ui = new UserInterface(buttons, field, root, primaryStage);
    ui.init();

    Scene mainScene = new Scene(root, 450, 130);
    primaryStage.setScene(mainScene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
