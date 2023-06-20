package com.example.gnosticescape_gui;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.InterruptedException;
import java.lang.NullPointerException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.util.Duration;

import javafx.animation.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

public class Client extends Application
{
    public static final int START_GAMEBOARD_Y = 10;
    public static final int START_GAMEBOARD_X = 10;
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 770;
    public static final int TILE_Y = 30;
    public static final int TILE_X = 30;
    private static final int PORT = 1388;
    private static final String ADDRESS = "localhost";
    private static final int SOCKET_TIMEOUT = 5000;
    private static boolean laterSetup = false;

    private static Socket socket;

    private static WorldState worldState = null;
    private static GameRequest desiredRequest = GameRequest.OK;
    private static Runnable receiveWorldRunnable;
    private static Thread receiveWorldThread;

    private static GUIClientCreator clientGUI = null;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage primaryStage)
    {
        try
        {
            primaryStage.setTitle("Gnostic Escape - Uciekinier");
            primaryStage.show();

            ImagesWrapper.imagesSetup();
            ImagesWrapper.tileX = TILE_X;
            ImagesWrapper.tileY = TILE_Y;

            clientGUI = new GUIClientCreator();

            primaryStage.setScene(new Scene(clientGUI.root));
            primaryStage.show();

            primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent event)
                {
                    switch(event.getCode())
                    {
                        case W:
                            desiredRequest = GameRequest.MOVE_UP;
                            break;
                        case S:
                            desiredRequest = GameRequest.MOVE_DOWN;
                            break;
                        case A:
                            desiredRequest = GameRequest.MOVE_LEFT;
                            break;
                        case D:
                            desiredRequest = GameRequest.MOVE_RIGHT;
                        break;
                        case P:
                            desiredRequest = GameRequest.LEAVE;
                        break;
                    }
                }
            });

            primaryStage.setOnCloseRequest
            (
                new EventHandler<WindowEvent>()
                {
                    public void handle(WindowEvent we)
                    {
                        closeGame();
                    }
                }
            );

            primaryStage.setWidth(SCREEN_WIDTH);
            primaryStage.setHeight(SCREEN_HEIGHT);

            socket = new Socket(ADDRESS, PORT);
            socket.setSoTimeout(SOCKET_TIMEOUT);

            GameMessage joinMessage = new GameMessage(GameRequest.JOIN);
            ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
            socketOutputStream.writeObject(joinMessage);

            ObjectInputStream socketInputStream = new ObjectInputStream(socket.getInputStream());
            GameMessage joinResponse = (GameMessage) socketInputStream.readObject();

            if(joinResponse.getGameRequest() != GameRequest.OK)
            {
                GUIClientCreator.showAlert("Serwer odrzucił połączenie.");
                System.exit(1);
            }

            receiveWorldRunnable = new Runnable()
            {
                public void run()
                {
                    receiveWorld();
                }
            };
            receiveWorldThread = new Thread(receiveWorldRunnable);
            receiveWorldThread.start();
        }
        catch(FileNotFoundException fnfe)
        {
            receiveWorldRunnable = new Runnable()
            {
                public void run()
                {
                    GUIClientCreator.showAlert("Brakuje plików.");
                }
            };
            System.exit(1);
        }
        catch(SocketException se)
        {
            receiveWorldRunnable = new Runnable()
            {
                public void run()
                {
                    GUIClientCreator.showAlert("Serwer może nie być aktywny.");
                }
            };
            System.exit(1);
        }
        catch(EOFException eofe) { }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch(ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
        }
    }

    public static void closeGame()
    {
        try
        {
            socket.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static WorldState getWorldState()
    {
        return worldState;
    }

    private static void receiveWorld()
    {
        while(true)
        {
            try
            {
                GameMessage moveMessage = null;
                ObjectOutputStream socketOutputStream = null;

                switch(desiredRequest)
                {
                    case MOVE_DOWN:
                        moveMessage = new GameMessage(GameRequest.MOVE_DOWN);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                    break;
                    case MOVE_UP:
                        moveMessage = new GameMessage(GameRequest.MOVE_UP);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                    break;
                    case MOVE_LEFT:
                        moveMessage = new GameMessage(GameRequest.MOVE_LEFT);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                    break;
                    case MOVE_RIGHT:
                        moveMessage = new GameMessage(GameRequest.MOVE_RIGHT);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                    break;
                    case LEAVE:
                        moveMessage = new GameMessage(GameRequest.LEAVE);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                    break;
                    default:
                        moveMessage = new GameMessage(GameRequest.WORLD);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                    break;
                }

                ObjectInputStream socketInputStream = new ObjectInputStream(socket.getInputStream());
                GameMessage receivedWorld = (GameMessage) socketInputStream.readObject();
                if(receivedWorld.getWorldState() != null)
                {
                    worldState = receivedWorld.getWorldState();
                    clientGUI.resizeCanvas(worldState.getGameboardX() * TILE_X, worldState.getGameboardY() * TILE_Y);
                    clientGUI.moveCanvas(SCREEN_WIDTH - worldState.getGameboardX() * TILE_X - 20, 8);
                    clientGUI.drawGame();

                    if(!laterSetup)
                    {
                        laterSetup = true;

                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                clientGUI.setupLaterElements();
                            }
                        });
                    }

                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            clientGUI.updateGUIElements();
                        }
                    });
                }
            }
            catch(FileNotFoundException fnfe)
            {

                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        GUIClientCreator.showAlert("Brakuje plików.");
                    }
                });
                System.exit(1);
            }
            catch(SocketException se)
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        GUIClientCreator.showAlert("Błąd połączenia.");
                    }
                });
                System.exit(1);
            }
            catch(EOFException eofe)
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        GUIClientCreator.showAlert("Błąd połączenia.");
                    }
                });
                System.exit(1);
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
            catch(ClassNotFoundException cnfe)
            {
                cnfe.printStackTrace();
            }
        }
    }
}





















