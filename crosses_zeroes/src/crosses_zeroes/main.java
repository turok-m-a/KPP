package crosses_zeroes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("крестики-нолики");
    Pane root = new Pane();
    Button msbtn[][] = new Button[4][4];
    logic ffield = new logic();
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++) {
        msbtn[i][j] = new Button();
        msbtn[i][j].setTranslateX(j * 30);
        msbtn[i][j].setTranslateY(i * 30);
        root.getChildren().add(msbtn[i][j]);
      }
    buttonmgr buttons = new buttonmgr(msbtn, ffield, primaryStage);
    buttons.initbuttons();
    userinterface ui = new userinterface(buttons, ffield, root);
    ui.init();
    Scene main_scene = new Scene(root, 350, 130);
    primaryStage.setScene(main_scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
