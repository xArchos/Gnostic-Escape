package com.example.gnosticescape_gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class GUIMapLoadingController {
    public Pane root = new Pane();
    private Button filenameButton = new Button();
    private final TextField filenameField = new TextField();

    public GUIMapLoadingController() {
        filenameField.setPromptText("Wprowadź nazwę pliku z mapą...");
        filenameField.setMaxWidth(400);
        filenameField.setMinWidth(400);
        filenameField.setMaxHeight(50);
        filenameField.setMinHeight(50);
        filenameField.setLayoutX((1200 - filenameField.getMaxWidth()) / 2);
        filenameField.setLayoutY((800 - filenameField.getMaxHeight()) / 2);

        filenameButton.setText("Uruchom serwer używając tej mapy!");
        filenameButton.setMinWidth(250);
        filenameButton.setMaxWidth(250);
        filenameButton.setMaxHeight(30);
        filenameButton.setMinHeight(30);
        filenameButton.setLayoutX((1200 - filenameButton.getMaxWidth()) / 2);
        filenameButton.setLayoutY(filenameField.getLayoutY() + filenameField.getMaxHeight());

        root.getChildren().addAll(filenameButton, filenameField);

        Stop[] stops = new Stop[]{new Stop(0, Color.web("0xFCEBCC")), new Stop(1, Color.web("0x3a0732"))};
        root.setBackground(new Background(new BackgroundFill(new LinearGradient(1, 0, 0, 0, true, CycleMethod.NO_CYCLE, stops), CornerRadii.EMPTY, Insets.EMPTY)));
        filenameButton.setStyle("-fx-background-color: #FCEBCC; -fx-text-fill: #000000; -fx-font: 13 Georgia");
        filenameField.setStyle("-fx-background-color: #4c0640; -fx-text-fill: #ffffff; -fx-font: 13 Georgia");
        filenameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    MapLoader mapLoader = new MapLoader();
                    SimpleGame.Map = mapLoader.loadMap(filenameField.getText());
                    SimpleGame.GAMEBOARD_X = mapLoader.getGameboardX();
                    SimpleGame.GAMEBOARD_Y = mapLoader.getGameboardY();
                    SimpleGame.SPAWN_X = mapLoader.getSpawnX();
                    SimpleGame.SPAWN_Y = mapLoader.getSpawnY();
                    SimpleGame.WINDOW_HEIGHT = SimpleGame.GAMEBOARD_Y * SimpleGame.TILE_Y + 50;

                    SimpleGame.gameSetup((Stage) (((Node) actionEvent.getSource()).getScene().getWindow()));
                } catch (Exception e) {
                    GUICreator.showAlert("Nie można załadować mapy.");
                }
            }
        });
    }
}
