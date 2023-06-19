package com.example.gnosticescape_gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

public class GUICreator
{
   public Canvas canvas;
   public GraphicsContext gc;
   public Pane root;
   private TableView playersTable = new TableView();
   private ComboBox spellMenu = new ComboBox();
   private ComboBox spellPlayerMenu = new ComboBox();
   private Button spellButton = new Button();
   private ManaBar manaBar = null;
   private Label manaValueLabel = null;

   private Label manaLabel=null;
   private Button teleportButton = new Button();
   private TextField yTeleportField = new TextField();
   private TextField xTeleportField = new TextField();
   private ComboBox teleportPlayerMenu = new ComboBox();

   GUICreator()
   {
      createCanvas();
      setupPlayersTable();
      setupSpellMenu();
      setupManaBar();
      setupTeleportGUI();
      setupDemiurgAvatar();
   }

   private void createCanvas()
   {
      this.canvas = new Canvas(SimpleGame.GAMEBOARD_X * SimpleGame.TILE_X, SimpleGame.GAMEBOARD_Y * SimpleGame.TILE_Y);
      this.canvas.setLayoutX(SimpleGame.WINDOW_WIDTH - SimpleGame.GAMEBOARD_X * SimpleGame.TILE_X - 20);
      this.canvas.setLayoutY(8);
      this.root=new Pane();
      this.gc = canvas.getGraphicsContext2D();
      root.getChildren().addAll(canvas);
   }

   public void createBackground()
   {
      Stop[] stops = new Stop[] {new Stop(0, Color.web("0xFCEBCC")),new Stop(1,Color.web("0x3a0732"))};
      root.setBackground(new Background(new BackgroundFill(new LinearGradient(1,0,0,0,true, CycleMethod.NO_CYCLE,stops), CornerRadii.EMPTY, Insets.EMPTY)));
   }

   public void setupPlayersTable()
   {

      playersTable.setLayoutX(60);
      playersTable.setLayoutY(220);
      playersTable.setMaxWidth(479);
      playersTable.setMinWidth(479);
      playersTable.setMaxHeight(200);
      playersTable.setMinHeight(200);
      playersTable.setPlaceholder(new Label("Nie ma graczy"));
      playersTable.setStyle("-fx-background-color: #4c0640"+";"+"-fx-text-fill: #ffffff");


      TableColumn<Player, String> playerNameColumn = new TableColumn<>("GRACZ");
      playerNameColumn.setMaxWidth(70);
      playerNameColumn.setMinWidth(70);
      //playerNameColumn.setStyle("-fx-background-color: linear-gradient(to top,#4c0640,#ffdab9)");
      playerNameColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff"+";"+"-fx-font: Georgia"+";"+"-fx-font-size: 15px"+";"+"-fx-font-weight: bold");

      playerNameColumn.setCellValueFactory(data ->
      {
         Player player = data.getValue();
         return new SimpleStringProperty("GRACZ " + player.getId());
      });

      TableColumn<Player, String> playerXYColumn = new TableColumn<>("X, Y");
      playerXYColumn.setMaxWidth(70);
      playerXYColumn.setMinWidth(70);
      playerXYColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff"+";"+"-fx-font: Georgia"+";"+"-fx-font-size: 15px"+";"+"-fx-font-weight: bold");

      playerXYColumn.setCellValueFactory(data ->
      {
         Player player = data.getValue();
         return new SimpleStringProperty("X: " + player.getCoordX() + ", Y: " + player.getCoordY());
      });

      TableColumn<Player, String> playerHealthColumn = new TableColumn<>("ŻYCIE");
      playerHealthColumn.setMaxWidth(140);
      playerHealthColumn.setMinWidth(140);
      playerHealthColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff"+";"+"-fx-font-size: 15px"+";"+"-fx-font-weight: bold");

      TableColumn<Player, String> playerReversedColumn = new TableColumn<>("");
      playerReversedColumn.setMaxWidth(50);
      playerReversedColumn.setMinWidth(50);
      playerReversedColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff");

      playerReversedColumn.setCellFactory(param -> new TableCell<Player, String>()
      {
         @Override
         protected void updateItem(String item, boolean empty)
         {
            super.updateItem(item, empty);
            if(!empty)
            {
               Player player = (Player)getTableRow().getItem();

               if(player != null)
               {
                  if(player.getIsReverted() > 0)
                  {
                     ImageView view = new ImageView(ImagesWrapper.reverseIcon);
                     view.setFitHeight(40);
                     view.setPreserveRatio(true);
                     setGraphic(view);
                  }
               }
            }
         }
      });

      TableColumn<Player, String> playerSlowColumn = new TableColumn<>("");
      playerSlowColumn.setMaxWidth(50);
      playerSlowColumn.setMinWidth(50);
      playerSlowColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640");

      playerSlowColumn.setCellFactory(param -> new TableCell<Player, String>()
      {
         @Override
         protected void updateItem(String item, boolean empty)
         {
            super.updateItem(item, empty);
            if(!empty)
            {
               Player player = (Player)getTableRow().getItem();

               if(player != null)
               {
                  if(player.getIsSlow() > 0)
                  {
                     ImageView view = new ImageView(ImagesWrapper.slowIcon);
                     view.setFitHeight(40);
                     view.setPreserveRatio(true);
                     setGraphic(view);
                  }
               }
            }
         }
      });

      TableColumn<Player, String> playerLightColumn = new TableColumn<>("");
      playerLightColumn.setMaxWidth(50);
      playerLightColumn.setMinWidth(50);
      playerLightColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640");

      playerLightColumn.setCellFactory(param -> new TableCell<Player, String>()
      {
         @Override
         protected void updateItem(String item, boolean empty)
         {
            super.updateItem(item, empty);
            if(!empty)
            {
               Player player = (Player)getTableRow().getItem();

               if(player != null)
               {
                  if(player.getIsLight() > 0)
                  {
                     ImageView view = new ImageView(ImagesWrapper.lightIcon);
                     view.setFitHeight(40);
                     view.setPreserveRatio(true);
                     setGraphic(view);
                  }
               }
            }
         }
      });

      TableColumn<Player, String> playerBlindColumn = new TableColumn<>("");
      playerBlindColumn.setMaxWidth(45);
      playerBlindColumn.setMinWidth(45);
      playerBlindColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff");

      playerBlindColumn.setCellFactory(param -> new TableCell<Player, String>()
      {
         @Override
         protected void updateItem(String item, boolean empty)
         {
            super.updateItem(item, empty);
            if(!empty)
            {
               Player player = (Player)getTableRow().getItem();

               if(player != null)
               {
                  if(player.getIsBlind() > 0)
                  {
                     ImageView view = new ImageView(ImagesWrapper.blindIcon);
                     view.setFitHeight(40);
                     view.setPreserveRatio(true);
                     setGraphic(view);
                  }
               }
            }
         }
      });

      playersTable.getColumns().addAll(playerNameColumn, playerXYColumn, playerHealthColumn);
      playersTable.getColumns().addAll(playerReversedColumn, playerSlowColumn, playerLightColumn, playerBlindColumn);

      updateTable();
      root.getChildren().add(playersTable);
   }

   public void updateTable()
   {
      ObservableList<Player> oList = FXCollections.observableArrayList(SimpleGame.getPlayerList());
      playersTable.setItems(oList);
      playersTable.refresh();
   }

   public void drawServerGame()
   {
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, SimpleGame.GAMEBOARD_Y * SimpleGame.TILE_Y, SimpleGame.GAMEBOARD_X * SimpleGame.TILE_X);

      for(int i = 0; i < SimpleGame.GAMEBOARD_X; i++)
      {
         for(int j = 0; j < SimpleGame.GAMEBOARD_Y; j++)
         {
            SimpleGame.getTileByIndex(i, j).draw(gc, i, j);
         }
      }

      for(int i = 0; i < SimpleGame.getTeleportList().size(); i++)
      {
         SimpleGame.getTeleportList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getPrizeList().size(); i++)
      {
         SimpleGame.getPrizeList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getGateList().size(); i++)
      {
         SimpleGame.getGateList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getHealthPackList().size(); i++)
      {
         SimpleGame.getHealthPackList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getSpikeList().size(); i++)
      {
         SimpleGame.getSpikeList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getCatapultList().size(); i++)
      {
         SimpleGame.getCatapultList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getPressurePlateList().size(); i++)
      {
         SimpleGame.getPressurePlateList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getLeverList().size(); i++)
      {
         SimpleGame.getLeverList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getPitfallList().size(); i++)
      {
         SimpleGame.getPitfallList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getPlayerList().size(); i++)
      {
         SimpleGame.getPlayerList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
      {
         SimpleGame.getStoneList().get(i).draw(gc);
      }

      for(int i = 0; i < SimpleGame.getOpeningKeyList().size(); i++)
      {
         SimpleGame.getOpeningKeyList().get(i).draw(gc);
      }

      if(SimpleGame.winPlayersCount >= SimpleGame.WIN_PLAYERS_END)
      {
         gc.setFill(Color.BLUE);
         gc.setFont(new Font("Digital-7", 35));
         gc.fillText("Uciekinierzy wygrali!", 0, SimpleGame.GAMEBOARD_X * SimpleGame.TILE_X);
      }

      if(SimpleGame.deadPlayersCount >= SimpleGame.DEAD_PLAYERS_END)
      {
         gc.setFill(Color.BLUE);
         gc.setFont(new Font("Digital-7", 35));
         gc.fillText("Demiurg wygrał!", 0, SimpleGame.GAMEBOARD_X * SimpleGame.TILE_X);
      }

      gc.setFill(Color.GREEN);
      gc.setFont(new Font("Digital-7", 35));
      gc.fillText(Integer.toString(SimpleGame.getPlayerList().get(0).getHealth()), 0, SimpleGame.GAMEBOARD_X * SimpleGame.TILE_X);

      updateTable();
      updateSpellPlayerMenu();
      updateManaBar();
      updateTeleportGUI();
   }

   public void setupSpellMenu()
   {
      spellMenu.getItems().addAll("Spowolnienie", "Ślepota", "Lekkość", "Inwersja", "Obrażenia");
      spellMenu.setCellFactory(e -> new ListCell<String>()
      {
         private ImageView iconView = new ImageView();
         @Override
         public void updateItem(String name, boolean empty)
         {
            super.updateItem(name, empty);
            setGraphic(null);

            if(!empty)
            {
               switch(name)
               {
                  case "Spowolnienie":
                     setText(name + " (-" + SimpleGame.SLOW_COST + " many)");
                     iconView.setImage(ImagesWrapper.slowIcon);
                     break;
                  case "Ślepota":
                     setText(name + " (-" + SimpleGame.BLIND_COST + " many)");
                     iconView.setImage(ImagesWrapper.blindIcon);
                     break;
                  case "Lekkość":
                     setText(name + " (-" + SimpleGame.LIGHT_COST + " many)");
                     iconView.setImage(ImagesWrapper.lightIcon);
                     break;
                  case "Inwersja":
                     setText(name + " (-" + SimpleGame.REVERSE_COST + " many)");
                     iconView.setImage(ImagesWrapper.reverseIcon);
                     break;
                  case "Obrażenia":
                     setText(name + " (-" + SimpleGame.DAMAGE_COST + " many)");
                     iconView.setImage(ImagesWrapper.damageIcon);
                     break;
               }

               iconView.setFitHeight(50);
               iconView.setFitWidth(50);
               setGraphic(iconView);
            }
         }
      });

      spellMenu.setLayoutX(0);
      spellMenu.setLayoutY(275);
      spellMenu.setMaxWidth(130);
      spellMenu.setMinWidth(130);
      spellMenu.setPromptText("Wybierz zaklęcie");
      root.getChildren().add(spellMenu);

      spellPlayerMenu.setConverter(new StringConverter<Player>() {
         @Override
         public String toString(Player player) {
            if(player == null)
            {
               return "Wybierz gracza";
            }

            return "Gracz " + Integer.toString(player.getId());
         }

         @Override
         public Player fromString(String string) {
            String strNumber = string.substring(6);

            try
            {
               int id = Integer.parseInt(strNumber);
               for(int i = 0; i < SimpleGame.getPlayerList().size(); i++)
               {
                  if(SimpleGame.getPlayerList().get(i).getId() == id)
                  {
                     return SimpleGame.getPlayerList().get(i);
                  }
               }
            }
            catch(NumberFormatException nfe)
            {
               return null;
            }

            return null;
         }
      });

      spellPlayerMenu.setLayoutX(130);
      spellPlayerMenu.setLayoutY(275);
      spellPlayerMenu.setMaxWidth(130);
      spellPlayerMenu.setMinWidth(130);
      updateSpellPlayerMenu();
      root.getChildren().add(spellPlayerMenu);

      spellButton = new Button("Rzuć zaklęcie");
      ImagesWrapper.setButtonCastSpellImage(spellButton);
      spellButton.setLayoutX(260);
      spellButton.setLayoutY(275);
      spellButton.setMinWidth(50);
      spellButton.setMinHeight(50);
      spellButton.setContentDisplay(ContentDisplay.TOP);


      spellButton.setOnAction(new EventHandler<ActionEvent>()
      {
         @Override
         public void handle(ActionEvent event)
         {
            Player player = (Player)spellPlayerMenu.getValue();

            if(player == null)
            {
               return;
            }

            switch((String)spellMenu.getValue())
            {
               case "Spowolnienie":
                  SimpleGame.slowPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "Ślepota":
                  SimpleGame.blindPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "Lekkość":
                  SimpleGame.lightPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "Inwersja":
                  SimpleGame.revertPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "Obrażenia":
                  SimpleGame.harmPlayer(player, SimpleGame.DAMAGE_VALUE);
                  break;
            }
         }
      });

      root.getChildren().add(spellButton);
   }

   public void updateSpellPlayerMenu()
   {
      ObservableList<Player> oList = FXCollections.observableArrayList(SimpleGame.getPlayerList());
      spellPlayerMenu.setItems(oList);
   }

   public void setupManaBar()
   {
      manaBar = new ManaBar(root);
      manaBar.setMaxWidth(300);
      manaBar.setMinWidth(300);
      manaBar.setLayoutX(canvas.getLayoutX()-manaBar.getMaxWidth()-25);
      manaBar.setLayoutY(150);
      manaBar.setMaxHeight(30);
      manaBar.setMinHeight(30);
      manaBar.setStyle("-fx-text-box-border: #302c2f" + ";" + "-fx-accent: #ab1294" + ";" + "-fx-control-inner-background: #302c2f");
      root.getChildren().add(manaBar);

      manaLabel = new Label();
      manaLabel.setText("MANA");
      manaLabel.setLayoutX(manaBar.getLayoutX()+manaBar.getMaxWidth()*0.44);
      manaLabel.setLayoutY(manaBar.getLayoutY()-30);
      manaLabel.setFont(Font.font("Georgia",20.0));
      manaLabel.setStyle("-fx-text-fill: #ffffff;");
      root.getChildren().add(manaLabel);

      manaValueLabel = new Label();
      manaValueLabel.setText(SimpleGame.mana + "/1000");
      manaValueLabel.setFont( Font.font("Georgia",20)); //Georgia
      manaValueLabel.setStyle("-fx-text-fill: #ffffff;");
      manaValueLabel.setLayoutX((400 - manaValueLabel.getWidth()) * 0.5);
      manaValueLabel.setLayoutY(30);
      root.getChildren().add(manaValueLabel);
   }

   public void updateManaBar()
   {
      manaBar.updateMana();
      manaValueLabel.setText(SimpleGame.mana + "/1000");
      manaValueLabel.setLayoutX(manaBar.getLayoutX()+(manaBar.getWidth()*0.4));
      manaValueLabel.setLayoutY(manaBar.getLayoutY());
   }

   public void setupTeleportGUI()
   {
      teleportPlayerMenu.setConverter(new StringConverter<Player>()
      {
      @Override
         public String toString(Player player)
         {
            if(player == null)
            {
               return "Wybierz gracza";
            }

            return "Gracz " + Integer.toString(player.getId());
         }

         @Override
         public Player fromString(String string)
         {
            String strNumber = string.substring(6);

            try
            {
               int id = Integer.parseInt(strNumber);
               for(int i = 0; i < SimpleGame.getPlayerList().size(); i++)
               {
                  if(SimpleGame.getPlayerList().get(i).getId() == id)
                  {
                     return SimpleGame.getPlayerList().get(i);
                  }
               }
            }
            catch(NumberFormatException nfe)
            {
               return null;
            }

            return null;
         }
      });

      teleportPlayerMenu.setLayoutX(70);
      teleportPlayerMenu.setLayoutY(600);
      teleportPlayerMenu.setMaxWidth(130);
      teleportPlayerMenu.setMinWidth(130);
      updateTeleportGUI();
      root.getChildren().add(teleportPlayerMenu);

      xTeleportField.setLayoutX(teleportPlayerMenu.getMaxWidth()+teleportPlayerMenu.getLayoutX());
      xTeleportField.setLayoutY(600);
      xTeleportField.setMaxWidth(75);
      xTeleportField.setMinWidth(75);
      xTeleportField.setPromptText("Podaj X");

      yTeleportField.setLayoutX(xTeleportField.getLayoutX()+xTeleportField.getMaxWidth());
      yTeleportField.setLayoutY(600);
      yTeleportField.setMaxWidth(75);
      yTeleportField.setMinWidth(75);
      yTeleportField.setPromptText("Podaj Y");
      root.getChildren().addAll(xTeleportField, yTeleportField);

      teleportButton.setLayoutX(yTeleportField.getLayoutX()+yTeleportField.getMaxWidth());
      teleportButton.setLayoutY(600);
      teleportButton.setMaxWidth(75);
      teleportButton.setMinWidth(75);
      teleportButton.setText("Teleportuj!");
      teleportButton.setMinHeight(75);
      teleportButton.setMaxHeight(75);
      teleportButton.setContentDisplay(ContentDisplay.TOP);
      ImageView teleportImageView = new ImageView(ImagesWrapper.teleportIcon);
      teleportImageView.setFitHeight(40);
      teleportImageView.setPreserveRatio(true);
      teleportButton.setGraphic(teleportImageView);
      root.getChildren().add(teleportButton);

      teleportButton.setOnAction(new EventHandler<ActionEvent>()
      {
         @Override
         public void handle(ActionEvent event)
         {
            Player player = (Player)teleportPlayerMenu.getValue();

            if(player == null)
            {
               return;
            }

            try
            {
               SimpleGame.teleportPlayer(player, Integer.parseInt(xTeleportField.getText()), Integer.parseInt(yTeleportField.getText()));
            }
            catch(NumberFormatException nfe) { }
         }
      });
   }

   public void updateTeleportGUI()
   {
      ObservableList<Player> oList = FXCollections.observableArrayList(SimpleGame.getPlayerList());
      teleportPlayerMenu.setItems(oList);
   }

   private void setupDemiurgAvatar()
   {
      Label demiurgLabel = new Label();
      demiurgLabel.setText("DEMIURG");
      demiurgLabel.setFont(Font.font("Georgia",35));
      demiurgLabel.setStyle("-fx-text-fill: #ffffff");
      demiurgLabel.setLayoutX(manaLabel.getLayoutX()-60);
      demiurgLabel.setLayoutY(manaLabel.getLayoutY()-90);

      ImageView demiurgAvatar = new ImageView(ImagesWrapper.demiurgAvatar);
      demiurgAvatar.setFitHeight(150);
      demiurgAvatar.setFitWidth(150);
      demiurgAvatar.setLayoutX(30);
      demiurgAvatar.setLayoutY(30);
      root.getChildren().addAll(demiurgAvatar,demiurgLabel);
   }
}
