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
public class main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("крестики-нолики");
    Pane root = new Pane();
    Button arrayOfButtons[][] = new Button[4][4];   // grid 4x4
    GameLogic field = new GameLogic();
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++) {
        arrayOfButtons[i][j] = new Button();
        arrayOfButtons[i][j].setTranslateX(j * 30); // make a grid
        arrayOfButtons[i][j].setTranslateY(i * 30);
        root.getChildren().add(arrayOfButtons[i][j]);
      }
    ButtonController buttons = new ButtonController(arrayOfButtons, field, primaryStage);
    buttons.initButtons();
    UserInterface ui = new UserInterface(buttons, field, root); 
    ui.init();
    Scene mainScene = new Scene(root, 350, 130);
    primaryStage.setScene(mainScene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
