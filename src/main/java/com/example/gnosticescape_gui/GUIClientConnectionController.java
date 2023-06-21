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

import java.net.Socket;

public class GUIClientConnectionController
{
    public Pane root = new Pane();
    private TextField portField = new TextField();
    private TextField ipField = new TextField();
    private Button connectButton = new Button();

    public GUIClientConnectionController()
    {
        ipField.setPromptText("Podaj IP...");
        ipField.setMaxWidth(300);
        ipField.setMinWidth(300);
        ipField.setMaxHeight(50);
        ipField.setMinHeight(50);
        ipField.setLayoutX((Client.SCREEN_WIDTH - ipField.getMaxWidth()) / 2);
        ipField.setLayoutY((Client.SCREEN_HEIGHT - ipField.getMaxHeight()) / 2 - 150);
        ipField.setStyle("-fx-background-color: #730500; -fx-text-fill: #ffffff; -fx-font: 13 Georgia");

        portField.setPromptText("Podaj port...");
        portField.setMaxWidth(200);
        portField.setMinWidth(200);
        portField.setMaxHeight(50);
        portField.setMinHeight(50);
        portField.setLayoutX((Client.SCREEN_WIDTH - portField.getMaxWidth()) / 2);
        portField.setLayoutY(ipField.getLayoutY() + ipField.getMaxHeight());
        portField.setStyle("-fx-background-color: #730500; -fx-text-fill: #ffffff; -fx-font: 13 Georgia");

        connectButton.setText("Dołącz do gry!");
        connectButton.setMaxWidth(150);
        connectButton.setMinWidth(150);
        connectButton.setMaxHeight(30);
        connectButton.setMinHeight(30);
        connectButton.setLayoutX((Client.SCREEN_WIDTH - connectButton.getMaxWidth()) / 2);
        connectButton.setLayoutY(portField.getLayoutY() + portField.getMaxHeight());
        connectButton.setStyle("-fx-background-color: #FCEBCC; -fx-text-fill: #000000; -fx-font: 13 Georgia");

        root.getChildren().addAll(ipField, portField, connectButton);
        Stop[] stops = new Stop[] {new Stop(0, Color.web("0xFCEBCC")),new Stop(1,Color.web("0x730500"))};
        root.setBackground(new Background(new BackgroundFill(new LinearGradient(1,0,0,0,true, CycleMethod.NO_CYCLE,stops), CornerRadii.EMPTY, Insets.EMPTY)));

        connectButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                try
                {
                    Client.ADDRESS = ipField.getText();
                    Client.PORT = Integer.parseInt(portField.getText());

                    Socket socket = new Socket(Client.ADDRESS, Client.PORT);
                    Client.setSocket(socket);

                    Client.clientSetup((Stage)(((Node)actionEvent.getSource()).getScene().getWindow()));
                }
                catch(NumberFormatException nfe)
                {
                    GUICreator.showAlert("Nie udało się dołączyć do gry.");
                }
                catch(Exception e)
                {
                    GUICreator.showAlert("Wystąpił błąd połączenia się z grą!");
                    System.exit(1);
                }
            }
        });
    }
}
