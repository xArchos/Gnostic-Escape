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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
      playersTable.setPlaceholder(new Label("BRAK GRACZY"));
      playersTable.setStyle("-fx-background-color: #4c0640"+";"+"-fx-text-fill: #ffffff");


      TableColumn<Player, String> playerNameColumn = new TableColumn<>("GRACZ");
      playerNameColumn.setMaxWidth(70);
      playerNameColumn.setMinWidth(70);
      //playerNameColumn.setStyle("-fx-background-color: linear-gradient(to top,#4c0640,#ffdab9)");
      playerNameColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff"+";"+"-fx-font: 13 Georgia"+";"+"-fx-font-size: 13;"+";"+"-fx-font-weight: bold");

      playerNameColumn.setCellValueFactory(data ->
      {
         Player player = data.getValue();
         return new SimpleStringProperty("GRACZ " + player.getId());
      });

      TableColumn<Player, String> playerXYColumn = new TableColumn<>("(X,Y)");
      playerXYColumn.setMaxWidth(70);
      playerXYColumn.setMinWidth(70);
      playerXYColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff"+";"+"-fx-font: 13 Georgia"+";"+"-fx-font-size: 13;"+";"+"-fx-font-weight: bold");

      playerXYColumn.setCellValueFactory(data ->
      {
         Player player = data.getValue();
         return new SimpleStringProperty("("+player.getCoordX() + "," + player.getCoordY()+")");
      });

      TableColumn<Player, String> playerHealthColumn = new TableColumn<>("ŻYCIE");
      playerHealthColumn.setMaxWidth(140);
      playerHealthColumn.setMinWidth(140);
      playerHealthColumn.setStyle("-fx-background-color: #4c0640"+";"+"-fx-border-color: #4c0640" + ";" + "-fx-text-fill: #ffffff"+";"+"-fx-font: 13 Georgia"+";"+"-fx-font-size: 13;"+";"+"-fx-font-weight: bold");

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
         Rectangle winMessageBox = new Rectangle(500,250);
         winMessageBox.setArcHeight(12);
         winMessageBox.setArcWidth(12);
         winMessageBox.setFill(Color.web("#ffdab9"));
         Text winMessageText = new Text("UCIEKINIERZY WYGRALI");
         winMessageText.setFont(Font.font("Georgia",24));
         winMessageText.setStyle("-fx-text-fill: #36130a");

         StackPane winMessagePane= new StackPane();
         winMessagePane.getChildren().addAll(winMessageBox,winMessageText);
         winMessagePane.setStyle("-fx-background-color: transparent");
         winMessagePane.setMaxWidth(winMessageBox.getWidth());
         winMessagePane.setMinWidth(winMessageBox.getWidth());
         winMessagePane.setMinHeight(winMessageBox.getHeight());
         winMessagePane.setMinHeight(winMessageBox.getHeight());
         winMessagePane.setLayoutX(SimpleGame.WINDOW_WIDTH*0.5);
         winMessagePane.setLayoutY(200);

         root.getChildren().add(winMessagePane);
      }

      if(SimpleGame.deadPlayersCount >= SimpleGame.DEAD_PLAYERS_END)
      {
         Rectangle winMessageBox = new Rectangle(500,250);
         winMessageBox.setArcHeight(12);
         winMessageBox.setArcWidth(12);
         winMessageBox.setFill(Color.web("#ffdab9"));
         Text winMessageText = new Text("DEMIURG WYGRAŁ");
         winMessageText.setFont(Font.font("Georgia",24));
         winMessageText.setStyle("-fx-text-fill: #36130a");

         StackPane winMessagePane= new StackPane();
         winMessagePane.getChildren().addAll(winMessageBox,winMessageText);
         winMessagePane.setStyle("-fx-background-color: transparent");
         winMessagePane.setMaxWidth(winMessageBox.getWidth());
         winMessagePane.setMinWidth(winMessageBox.getWidth());
         winMessagePane.setMinHeight(winMessageBox.getHeight());
         winMessagePane.setMinHeight(winMessageBox.getHeight());
         winMessagePane.setLayoutX(SimpleGame.WINDOW_WIDTH*0.5);
         winMessagePane.setLayoutY(200);

         root.getChildren().add(winMessagePane);
      }

      updateTable();
      updateSpellPlayerMenu();
      updateManaBar();
      updateTeleportGUI();
   }

   public void setupSpellMenu()
   {
      spellMenu.getItems().addAll("SPOWOLNIENIE", "ŚLEPOTA", "LEKKOŚĆ", "INWERSJA", "OBRAŻENIA");
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
                  case "SPOWOLNIENIE":
                     setText(name + " (-" + SimpleGame.SLOW_COST+")");
                     iconView.setImage(ImagesWrapper.slowIcon);
                     break;
                  case "ŚLEPOTA":
                     setText(name + " (-" + SimpleGame.BLIND_COST+")");
                     iconView.setImage(ImagesWrapper.blindIcon);
                     break;
                  case "LEKKOŚĆ":
                     setText(name + " (-" + SimpleGame.LIGHT_COST+")");
                     iconView.setImage(ImagesWrapper.lightIcon);
                     break;
                  case "INWERSJA":
                     setText(name + " (-" + SimpleGame.REVERSE_COST+")");
                     iconView.setImage(ImagesWrapper.reverseIcon);
                     break;
                  case "OBRAŻENIA":
                     setText(name + " (-" + SimpleGame.DAMAGE_COST+")");
                     iconView.setImage(ImagesWrapper.damageIcon);
                     break;
               }

               iconView.setFitHeight(50);
               iconView.setFitWidth(50);
               setGraphic(iconView);
            }
         }
      });

      spellMenu.setLayoutX(70);
      spellMenu.setLayoutY(480);
      spellMenu.setMaxWidth(130);
      spellMenu.setMinWidth(130);
      spellMenu.setMinHeight(40);
      spellMenu.setMaxHeight(40);
      spellMenu.setStyle("-fx-background-color: #261f1f"+";"+"-fx-base: #36130b"+";"+"fx-font: 15 Georgia"+";"+"-fx-font-weight: bold"+";"+"-fx-font-size: 13;"+";"+"-fx-border-color: #302c2f");
      spellMenu.setPromptText("ZAKLĘCIE");
      root.getChildren().add(spellMenu);

      spellPlayerMenu.setConverter(new StringConverter<Player>()
      {
         @Override
         public String toString(Player player) {
            if(player == null)
            {
               return "GRACZ";
            }

            return "GRACZ " + Integer.toString(player.getId());
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

      spellPlayerMenu.setLayoutX(spellMenu.getLayoutX()+spellMenu.getMaxWidth());
      spellPlayerMenu.setLayoutY(spellMenu.getLayoutY());
      spellPlayerMenu.setMaxWidth(130);
      spellPlayerMenu.setMinWidth(130);
      spellPlayerMenu.setMinHeight(40);
      spellPlayerMenu.setMaxHeight(40);
      spellPlayerMenu.setStyle("-fx-background-color: #261f1f"+";"+"-fx-base: #36130b"+";"+"fx-font: 15 Georgia"+";"+"-fx-font-weight: bold"+";"+"-fx-font-size: 13;"+";"+"-fx-border-color: #302c2f");
      updateSpellPlayerMenu();
      root.getChildren().add(spellPlayerMenu);

      spellButton = new Button();
      ImagesWrapper.setButtonCastSpellImage(spellButton);
      spellButton.setLayoutX(spellPlayerMenu.getLayoutX()+spellPlayerMenu.getMaxWidth()+20);
      spellButton.setLayoutY(spellMenu.getLayoutY()-7);
      spellButton.setMaxWidth(40);
      spellButton.setMaxHeight(40);
      spellButton.setStyle("-fx-background-color: transparent"+";"+"-fx-border-color: transparent");
      spellButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);


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
               case "SPOWOLNIENIE":
                  SimpleGame.slowPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "ŚLEPOTA":
                  SimpleGame.blindPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "LEKKOŚĆ":
                  SimpleGame.lightPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "INWERSJA":
                  SimpleGame.revertPlayer(player, SimpleGame.EFFECT_TICKS);
                  break;
               case "OBRAŻENIA":
                  SimpleGame.harmPlayer(player, SimpleGame.DAMAGE_VALUE);
                  break;
            }
         }
      });

      root.getChildren().add(spellButton);

      Label spellLabel = new Label("UROKI");
      spellLabel.setLayoutX(spellMenu.getLayoutX());
      spellLabel.setLayoutY(spellMenu.getLayoutY()-2-spellMenu.getMaxHeight());
      spellLabel.setFont(Font.font("Georgia",20.0));
      spellLabel.setStyle("-fx-text-fill: #ffffff; ");

      root.getChildren().add(spellLabel);

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
               return "GRACZ";
            }

            return "GRACZ " + Integer.toString(player.getId());
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
      teleportPlayerMenu.setMaxWidth(spellMenu.getMaxWidth());
      teleportPlayerMenu.setMinWidth(spellMenu.getMinWidth());
      teleportPlayerMenu.setMinHeight(spellMenu.getMinHeight());
      teleportPlayerMenu.setMaxHeight(spellMenu.getMaxHeight());
      teleportPlayerMenu.setStyle("-fx-background-color: #261f1f"+";"+"-fx-base: #36130b"+";"+"fx-font: 15 Georgia"+";"+"-fx-font-weight: bold"+";"+"-fx-font-size: 13;"+";"+"-fx-border-color: #302c2f");
      updateTeleportGUI();
      root.getChildren().add(teleportPlayerMenu);

      xTeleportField.setLayoutX(teleportPlayerMenu.getMaxWidth()+teleportPlayerMenu.getLayoutX());
      xTeleportField.setLayoutY(600);
      xTeleportField.setMaxWidth(40);
      xTeleportField.setMinWidth(40);
      xTeleportField.setMinHeight(40);
      xTeleportField.setMinHeight(40);
      xTeleportField.setStyle("-fx-background-color: #261f1f"+";"+"-fx-base: #4c0640"+";"+"fx-font: 15 Georgia"+";"+"-fx-font-weight: bold"+";"+"-fx-font-size: 13;"+"-fx-text-fill: white;"+";"+"-fx-border-color: #302c2f"+";"+"-fx-control-inner-background: #ffffff");
      xTeleportField.setPromptText("X:");

      yTeleportField.setLayoutX(xTeleportField.getLayoutX()+xTeleportField.getMaxWidth());
      yTeleportField.setLayoutY(600);
      yTeleportField.setMaxWidth(40);
      yTeleportField.setMinWidth(40);
      yTeleportField.setMinHeight(40);
      yTeleportField.setMinHeight(40);
      yTeleportField.setPromptText("Y:");
      yTeleportField.setStyle("-fx-background-color: #261f1f"+";"+"-fx-base: #4c0640"+";"+"fx-font: 15 Georgia"+";"+"-fx-font-weight: bold"+";"+"-fx-font-size: 13;"+"-fx-text-fill: white;"+";"+"-fx-border-color: #302c2f"+";"+"-fx-control-inner-background: #ffffff;");
      root.getChildren().addAll(xTeleportField, yTeleportField);

      teleportButton.setLayoutX(yTeleportField.getLayoutX()+yTeleportField.getMaxWidth()+20);
      teleportButton.setLayoutY(yTeleportField.getLayoutY()-15);
      teleportButton.setMaxWidth(75);
      teleportButton.setMinWidth(75);
      teleportButton.setMinHeight(75);
      teleportButton.setMaxHeight(75);
      teleportButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
      teleportButton.setContentDisplay(ContentDisplay.TOP);
      ImageView teleportImageView = new ImageView(ImagesWrapper.teleportIcon);
      teleportImageView.setFitHeight(55);
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

      Label teleportLabel = new Label("TELEPORTACJA");
      teleportLabel.setLayoutX(teleportPlayerMenu.getLayoutX());
      teleportLabel.setLayoutY(teleportPlayerMenu.getLayoutY()-2-teleportPlayerMenu.getMaxHeight());
      teleportLabel.setFont(Font.font("Georgia",20.0));
      teleportLabel.setStyle("-fx-text-fill: #ffffff; ");

      root.getChildren().add(teleportLabel);
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
