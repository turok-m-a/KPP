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
public class userinterface {
  buttonmgr buttons;
  logic field;
  Pane root;

  userinterface(buttonmgr buttonss, logic fieldd, Pane roott) {
    buttons = buttonss;
    field = fieldd;
    root = roott;
  }

  void init() {
    Label sizelabel = new Label();
    sizelabel.setText("Размер поля:");
    sizelabel.setTranslateX(150);
    sizelabel.setTranslateY(50);
    Label levellabel = new Label();
    levellabel.setText("Сложность:");
    levellabel.setTranslateX(250);
    levellabel.setTranslateY(50);
    RadioButton radiothree = new RadioButton();
    radiothree.setText("3x3");
    radiothree.setTranslateX(150);
    radiothree.setTranslateY(70);
    radiothree.setSelected(true);
    RadioButton radiofour = new RadioButton();
    radiofour.setTranslateX(150);
    radiofour.setTranslateY(90);
    radiofour.setText("4x4");
    RadioButton radioeasy = new RadioButton();
    radioeasy.setTranslateX(250);
    radioeasy.setTranslateY(70);
    radioeasy.setText("Низкая");
    RadioButton radiomedium = new RadioButton();
    radiomedium.setTranslateX(250);
    radiomedium.setTranslateY(90);
    radiomedium.setText("Средняя");
    radiomedium.setSelected(true);
    RadioButton radiohard = new RadioButton();
    radiohard.setTranslateX(250);
    radiohard.setTranslateY(110);
    radiohard.setText("Высокая");
    radioeasy.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        field.level = 1;
        radiomedium.setSelected(false);
        radiohard.setSelected(false);
      }
    });
    radiomedium.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        field.level = 3;
        radioeasy.setSelected(false);
        radiohard.setSelected(false);
      }
    });
    radiohard.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        field.level = 5;
        radiomedium.setSelected(false);
        radioeasy.setSelected(false);
      }
    });
    radiofour.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        buttons.setfour();
        radiothree.setSelected(false);
        radiofour.setSelected(true);
      }
    });
    radiothree.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        buttons.setthree();
        radiothree.setSelected(true);
        radiofour.setSelected(false);
      }
    });
    Button btn = new Button();
    btn.setText("Новая игра");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        buttons.initbuttons();
      }
    });
    btn.setTranslateX(150);
    btn.setTranslateY(10);
    btn.setPrefSize(100, 30);
    Button auto = new Button();
    auto.setText("Автоигра");
    auto.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        buttons.autogame();
      }
    });
    auto.setTranslateX(250);
    auto.setTranslateY(10);
    auto.setPrefSize(100, 30);
    root.getChildren().addAll(btn, radiothree, radiofour, sizelabel, levellabel, radiohard,
        radiomedium);
    root.getChildren().addAll(auto, radioeasy);
  }

}
