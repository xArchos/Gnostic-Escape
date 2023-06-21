package com.example.gnosticescape_gui;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public class GUIClientCreator
{
    public Canvas canvas = null;
    public GraphicsContext gc = null;
    public Pane root = new Pane();

    private HealthBar hpBar = null;

    private Label infoLabel = null;
    private Label hpLabel = null;
    Label idLabel = new Label();

    private DayModeSwitcher dayModeSwitcher = null;

    private VBox effectsVBox = null;

    public GUIClientCreator()
    {
        createCanvas();
        createBackground();
    }

    private void createCanvas()
    {
        canvas = new Canvas(Client.START_GAMEBOARD_X * Client.TILE_X, Client.START_GAMEBOARD_Y * Client.TILE_Y);
        this.canvas.setLayoutX(Client.SCREEN_WIDTH - Client.START_GAMEBOARD_X * Client.TILE_X - 20);
        this.canvas.setLayoutY(8);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
    }

    public void moveCanvas(int x, int y)
    {
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
    }

    public void resizeCanvas(int x, int y)
    {
        canvas.setWidth(x);
        canvas.setHeight(y);
    }

    private void createBackground()
    {
        Stop[] stops = new Stop[] {new Stop(0, Color.web("0xFCEBCC")),new Stop(1,Color.web("0x730500"))};
        root.setBackground(new Background(new BackgroundFill(new LinearGradient(1,0,0,0,true, CycleMethod.NO_CYCLE,stops), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void drawGame()
    {
        try
        {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, Client.getWorldState().getGameboardY() * Client.TILE_Y, Client.getWorldState().getGameboardX() * Client.TILE_X);

            for(int i = 0; i < Client.getWorldState().getGameboardX(); i++)
            {
                for(int j = 0; j < Client.getWorldState().getGameboardY(); j++)
                {
                    Client.getWorldState().getTileByIndex(i, j).draw(gc, i, j);
                }
            }

            for(int i = 0; i < Client.getWorldState().getTeleportList().size(); i++)
            {
                Client.getWorldState().getTeleportList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getPrizeList().size(); i++)
            {
                Client.getWorldState().getPrizeList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getGateList().size(); i++)
            {
                Client.getWorldState().getGateList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getHealthPackList().size(); i++)
            {
                Client.getWorldState().getHealthPackList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getSpikeList().size(); i++)
            {
                Client.getWorldState().getSpikeList().get(i).draw(gc);
            }

            for(int i = 0; i <  Client.getWorldState().getCatapultList().size(); i++)
            {
                Client.getWorldState().getCatapultList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getPressurePlateList().size(); i++)
            {
                Client.getWorldState().getPressurePlateList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getLeverList().size(); i++)
            {
                Client.getWorldState().getLeverList().get(i).draw(gc);
            }

            //brak pitfall

            for(int i = 0; i <Client.getWorldState().getPlayerList().size(); i++)
            {
                Client.getWorldState().getPlayerList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getStoneList().size(); i++)
            {
                Client.getWorldState().getStoneList().get(i).draw(gc);
            }

            for(int i = 0; i < Client.getWorldState().getOpeningKeyList().size(); i++)
            {
                Client.getWorldState().getOpeningKeyList().get(i).draw(gc);
            }

            if(Client.getWorldState().getPlayer().getIsBlind() > 0)
            {
                gc.setLineWidth(1000);
                gc.strokeOval(Client.getWorldState().getPlayer().getCoordX() * Client.TILE_X - 600, Client.getWorldState().getPlayer().getCoordY() * Client.TILE_Y - 600, 1200, 1200);
            }
        }
        catch(NullPointerException npe)
        {
            showAlert("Wystąpił błąd.");
        }
    }

    public static void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupHPBar()
    {
        hpBar = new HealthBar(Client.getWorldState().getPlayer());
        hpBar.updateValue();

        hpBar.setMaxWidth(300);
        hpBar.setMinWidth(300);
        hpBar.setLayoutX(canvas.getLayoutX() - hpBar.getMaxWidth() - 40);
        hpBar.setLayoutY(150);
        hpBar.setMaxHeight(30);
        hpBar.setMinHeight(30);
        root.getChildren().add(hpBar);
    }

    private void setupInfoLabel()
    {
        infoLabel = new Label();
        infoLabel.setLayoutX(canvas.getLayoutX() - hpBar.getMaxWidth() - 25);
        infoLabel.setLayoutY(200);
        infoLabel.setFont(Font.font("Georgia",20.0));
        infoLabel.setStyle("-fx-text-fill: #FFFFFF;");
        root.getChildren().add(infoLabel);

        idLabel.setFont(Font.font("Georgia",35));
        idLabel.setStyle("-fx-text-fill: #ffffff");
        idLabel.setLayoutX(120);
        idLabel.setLayoutY(40);
        root.getChildren().add(idLabel);
    }

    public void setupLaterElements()
    {
        setupHPBar();
        setupInfoLabel();
        DayNightSwitchSetup();
        setupHPBar();
        setupEffectsVBox();
    }

    public void updateGUIElements()
    {
        hpBar.setPlayer(Client.getWorldState().getPlayer());
        hpBar.updateValue();

        if(hpLabel != null)
        {
            root.getChildren().remove(hpLabel);
        }

        hpLabel = hpBar.getLabel();
        setHpLabelProperties(hpLabel);
        root.getChildren().add(hpLabel);

        Label hpTextLabel = new Label();
        hpTextLabel.setText("ZDROWIE");
        hpTextLabel.setLayoutX(hpBar.getLayoutX()+hpBar.getMaxWidth()*0.44-25);
        hpTextLabel.setLayoutY(hpBar.getLayoutY()-30);
        hpTextLabel.setFont(Font.font("Georgia",20.0));
        hpTextLabel.setStyle("-fx-text-fill: #FFFFFF;");
        root.getChildren().add(hpTextLabel);

        infoLabel.setText("X: " + Client.getWorldState().getPlayer().getCoordX() + ", Y: " + Client.getWorldState().getPlayer().getCoordY()
            + "\n\nWYGRAŁO: " + Client.getWorldState().getWinPlayersNow() + "\nPRZEGRAŁO: " + Client.getWorldState().getDeadPlayersNow());
        idLabel.setText("GRACZ " + Client.getWorldState().getPlayer().getId());

        updateEffectsVBox();
    }

    private void setHpLabelProperties(Label hpLabel)
    {

        hpLabel.setLayoutX(hpBar.getLayoutX()+hpBar.getMaxWidth()*0.44-20);
        hpLabel.setLayoutY(hpBar.getLayoutY());
        if(Client.getWorldState().getPlayer().getHealth()>500)
            hpLabel.setStyle("-fx-font: 22 Georgia; -fx-text-fill: #434344");
        else
            hpLabel.setStyle("-fx-font: 22 Georgia; -fx-text-fill: #ffffff");
    }

    private void DayNightSwitchSetup()
    {
        dayModeSwitcher = new DayModeSwitcher("#fd676a","#6a0204","6a0204","#ffffff",40,20,15);
        dayModeSwitcher.setLayoutX(175);
        dayModeSwitcher.setLayoutY(105);
        root.getChildren().add(dayModeSwitcher);
    }

    private void setupEffectsVBox()
    {
        effectsVBox = new VBox();
        effectsVBox.setLayoutX(0);
        effectsVBox.setLayoutY(0);
        root.getChildren().add(effectsVBox);
    }

    private void updateEffectsVBox()
    {
        effectsVBox.getChildren().clear();

        if(Client.getWorldState().getPlayer().getIsReverted() > 0)
        {
            ImageView revertedView = new ImageView(ImagesWrapper.reverseIcon);
            revertedView.setFitHeight(80);
            revertedView.setPreserveRatio(true);
            effectsVBox.getChildren().add(revertedView);
        }
        if(Client.getWorldState().getPlayer().getIsSlow() > 0)
        {
            ImageView slowView = new ImageView(ImagesWrapper.slowIcon);
            slowView.setFitHeight(80);
            slowView.setPreserveRatio(true);
            effectsVBox.getChildren().add(slowView);
        }
        if(Client.getWorldState().getPlayer().getIsLight() > 0)
        {
            ImageView lightView = new ImageView(ImagesWrapper.lightIcon);
            lightView.setFitHeight(80);
            lightView.setPreserveRatio(true);
            effectsVBox.getChildren().add(lightView);
        }
    }
}
