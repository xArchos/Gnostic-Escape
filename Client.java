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
    private static final int START_GAMEBOARD_Y = 10;
    private static final int START_GAMEBOARD_X = 10;
    private static final int TILE_Y = 30;
    private static final int TILE_X = 30;
    private static final int PORT = 1388;
    private static final String ADDRESS = "localhost";
    private static final int SOCKET_TIMEOUT = 5000;

    private static Socket socket;
    private static WorldState worldState = null;
    private static GameRequest desiredRequest = GameRequest.OK;
    private static Runnable receiveWorldRunnable;
    private static Runnable keyboardRunnable;
    private static Thread receiveWorldThread;
    private static Thread keyboardThread;

    static Canvas canvas = null;
    static GraphicsContext gc = null;

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

            canvasSetup();

            primaryStage.setScene(new Scene(new StackPane(canvas)));
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
                            System.out.println("W");
                            break;
                        case S:
                            desiredRequest = GameRequest.MOVE_DOWN;
                            System.out.println("S");
                            break;
                        case A:
                            desiredRequest = GameRequest.MOVE_LEFT;
                            System.out.println("A");
                            break;
                        case D:
                            desiredRequest = GameRequest.MOVE_RIGHT;
                            System.out.println("D");
                        break;
                        case Q:
                            desiredRequest = GameRequest.LEAVE;
                            System.out.println("Q");
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

            primaryStage.setWidth(1200);
            primaryStage.setHeight(900);

            socket = new Socket(ADDRESS, PORT);
            socket.setSoTimeout(SOCKET_TIMEOUT);

            GameMessage joinMessage = new GameMessage(GameRequest.JOIN);
            ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
            socketOutputStream.writeObject(joinMessage);

            ObjectInputStream socketInputStream = new ObjectInputStream(socket.getInputStream());
            GameMessage joinResponse = (GameMessage) socketInputStream.readObject();

            if(joinResponse.getGameRequest() != GameRequest.OK)
            {
                System.out.println("Serwer odrzucił połączenie.");
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

            /*keyboardRunnable = new Runnable()
            {
                public void run()
                {
                    keyboard();
                }
            };
            keyboardThread = new Thread(keyboardRunnable);
            keyboardThread.start();*/
        }
        catch(FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
            System.out.println("Brakuje plików.");
            System.exit(1);
        }
        catch(SocketException se)
        {
            se.printStackTrace();
            System.out.println("Serwer może nie być aktywny.");
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

    private static void canvasSetup()
    {
        canvas = new Canvas(START_GAMEBOARD_X * TILE_X, START_GAMEBOARD_Y * TILE_Y);
		gc = canvas.getGraphicsContext2D();
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
                    canvas.setWidth(worldState.getGameboardX() * TILE_X);
                    canvas.setHeight(worldState.getGameboardY() * TILE_Y);
                    drawGame();
                    System.out.println("Rysuję!");
                }
            }
            catch(FileNotFoundException fnfe)
            {
                fnfe.printStackTrace();
                System.out.println("Brakuje plików.");
                System.exit(1);
            }
            catch(SocketException se)
            {
                se.printStackTrace();
                System.exit(1);
            }
            catch(EOFException eofe)
            {
                eofe.printStackTrace();
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

    private static void keyboard()
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
                        System.out.println("dół 1");
                        moveMessage = new GameMessage(GameRequest.MOVE_DOWN);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                        System.out.println("dół 2");
                    break;
                    case MOVE_UP:
                        System.out.println("góra 1");
                        moveMessage = new GameMessage(GameRequest.MOVE_UP);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                        System.out.println("góra 2");
                    break;
                    case MOVE_LEFT:
                        System.out.println("lewo 1");
                        moveMessage = new GameMessage(GameRequest.MOVE_LEFT);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                        System.out.println("lewo 2");
                    break;
                    case MOVE_RIGHT:
                        System.out.println("prawo 1");
                        moveMessage = new GameMessage(GameRequest.MOVE_RIGHT);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                        System.out.println("prawo 2");
                    break;
                    case LEAVE:
                        System.out.println("wyjść 1");
                        moveMessage = new GameMessage(GameRequest.LEAVE);
                        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        socketOutputStream.writeObject(moveMessage);
                        desiredRequest = GameRequest.OK;
                        System.out.println("wyjść 2");
                    break;
                }

                Thread.sleep(100);
            }
            catch(FileNotFoundException fnfe)
            {
                fnfe.printStackTrace();
                System.out.println("Brakuje plików.");
                System.exit(1);
            }
            catch(SocketException se)
            {
                se.printStackTrace();
                System.exit(1);
            }
            catch(EOFException eofe)
            {
                eofe.printStackTrace();
            }
            catch(InterruptedException ie)
            {
                ie.printStackTrace();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }

    private static void drawGame()
    {
        try
        {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, worldState.getGameboardY() * TILE_Y, worldState.getGameboardX() * TILE_X);

            for(int i = 0; i < worldState.getGameboardX(); i++)
            {
                for(int j = 0; j < worldState.getGameboardY(); j++)
                {
                    worldState.getTileByIndex(i, j).draw(gc, i, j);
                }
            }

            for(int i = 0; i < worldState.getTeleportList().size(); i++)
            {
                worldState.getTeleportList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getPrizeList().size(); i++)
            {
                worldState.getPrizeList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getGateList().size(); i++)
            {
                worldState.getGateList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getHealthPackList().size(); i++)
            {
                worldState.getHealthPackList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getSpikeList().size(); i++)
            {
                worldState.getSpikeList().get(i).draw(gc);
            }

            for(int i = 0; i <  worldState.getCatapultList().size(); i++)
            {
                worldState.getCatapultList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getPressurePlateList().size(); i++)
            {
                worldState.getPressurePlateList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getLeverList().size(); i++)
            {
                worldState.getLeverList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getPitfallList().size(); i++)
            {
            worldState.getPitfallList().get(i).draw(gc);
            }

            for(int i = 0; i <worldState.getPlayerList().size(); i++)
            {
                worldState.getPlayerList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getStoneList().size(); i++)
            {
                worldState.getStoneList().get(i).draw(gc);
            }

            for(int i = 0; i < worldState.getOpeningKeyList().size(); i++)
            {
                worldState.getOpeningKeyList().get(i).draw(gc);
            }

            //hp
            //x
            //y
            //ślepy?
            //ile wygrało
            //ile umarło
        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }
    }
}





















