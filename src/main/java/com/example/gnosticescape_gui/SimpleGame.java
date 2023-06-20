package com.example.gnosticescape_gui;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SimpleGame extends Application
{
    private static final int PORT = 1388;
    private static final int SOCKET_TIMEOUT = 300000;
    private static final int MAX_PLAYERS = 3;
    static final int WIN_PLAYERS_END = 2;
    static final int DEAD_PLAYERS_END = 2;
    private static final int TICK_CHECK = 80;
    public static final int EFFECT_TICKS = 80;
    private static final int TICK_TIME_MILLIS = 100;
    static final int TILE_Y = 30;
    static final int TILE_X = 30;

    private static final int INITIAL_MANA = 1000; //200
    public static final int MANA_LIMIT = 1000;
    private static final int MANA_TICK_GROWTH = 1;
    public static final int TELEPORT_COST = 400;
    public static final int DAMAGE_COST = 200;
    public static final int REVERSE_COST = 40;
    public static final int BLIND_COST = 80;
    public static final int LIGHT_COST = 600;
    public static final int SLOW_COST = 150;
    public static final int DAMAGE_VALUE = 148;


    private static final int SPAWN_X = 0;
    private static final int SPAWN_Y = 0;
    public static final int WINDOW_WIDTH = 1300;

    public static int mana = INITIAL_MANA;
    static int winPlayersCount = 0;
    static int deadPlayersCount = 0;
    private static int tick = 0;
    private static boolean serverOn = true;
    private static ServerSocket serverSocket;
    private static Runnable acceptMethod;
    private static Thread acceptThread;
    private static Lock gameLock;

    private static List<Player> playerList = new ArrayList<Player>();
    private static List<Stone> stoneList = new ArrayList<Stone>();
    private static List<Teleport> teleportList = new ArrayList<Teleport>();
    private static List<Prize> prizeList = new ArrayList<Prize>();
    private static List<Gate> gateList = new ArrayList<Gate>();
    private static List<OpeningKey> openingKeyList = new ArrayList<OpeningKey>();
    private static List<HealthPack> healthPackList = new ArrayList<HealthPack>();
    private static List<Spike> spikeList = new ArrayList<Spike>();
    private static List<Pitfall> pitfallList = new ArrayList<Pitfall>();
    private static List<PressurePlate> pressurePlateList = new ArrayList<PressurePlate>();
    private static List<Lever> leverList = new ArrayList<Lever>();
    private static List<Catapult> catapultList = new ArrayList<Catapult>();

    private static GUICreator gui = null;
    private static Tile[][] Map;
    public static int GAMEBOARD_Y;
    public static int GAMEBOARD_X;
    private static int WINDOW_HEIGHT;



    public static void main(String[] args)
    {
        MapLoader mapLoader = new MapLoader();
        Map = mapLoader.loadMap("map.txt");
        GAMEBOARD_X = mapLoader.getGameboardX();
        GAMEBOARD_Y = mapLoader.getGameboardY();
        WINDOW_HEIGHT = GAMEBOARD_Y * TILE_Y + 50;



        launch(args);
    }

    public void start(Stage primaryStage)
    {
        try
        {
            serverSocket = new ServerSocket(PORT);

            ImagesWrapper.imagesSetup();
            ImagesWrapper.tileX = TILE_X;
            ImagesWrapper.tileY = TILE_Y;

            Timeline tl = new Timeline(new KeyFrame(Duration.millis(TICK_TIME_MILLIS), e -> run(gui.gc)));
            tl.setCycleCount(Timeline.INDEFINITE);

            gui = new GUICreator();

            Scene scene = new Scene(gui.root);
            primaryStage.setScene(scene);

            primaryStage.setTitle("Gnostic Escape - Demiurg");
            primaryStage.setMaxWidth(WINDOW_WIDTH);
            primaryStage.setMinWidth(WINDOW_WIDTH);
            primaryStage.setMaxHeight(WINDOW_HEIGHT);
            primaryStage.setMinHeight(WINDOW_HEIGHT);
            primaryStage.show();

            primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent event)
                {
                    if(playerList.size() > 0)
                    {
                        switch(event.getCode())
                        {
                        case W:
                            playerList.get(0).move(Direction.UP);
                            break;
                        case S:
                            playerList.get(0).move(Direction.DOWN);
                            break;
                        case A:
                            playerList.get(0).move(Direction.LEFT);
                            break;
                        case D:
                            playerList.get(0).move(Direction.RIGHT);
                            break;
                        case V:
                            togglePortals();
                        break;
                        case R:
                            revertPlayer(playerList.get(0), EFFECT_TICKS);
                        break;
                        case T:
                            blindPlayer(playerList.get(0), EFFECT_TICKS);
                        break;
                        case Y:
                            slowPlayer(playerList.get(0), EFFECT_TICKS);
                        break;
                        case U:
                            lightPlayer(playerList.get(0), EFFECT_TICKS);
                        break;
                        default:
                            break;
                        }
                    }
                }
            });

            tl.play();

            new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                {
                    gui.drawServerGame();
                }
            }.start();

            acceptMethod = new Runnable()
            {
                public void run()
                {
                    acceptClients();
                }
            };
            acceptThread = new Thread(acceptMethod);
            acceptThread.start();

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

            gameLock = new ReentrantLock();
        }
        catch(FileNotFoundException fnfe)
        {
            GUICreator.showAlert("Brakuje plików.");
            System.exit(1);
        }
        catch(EOFException eofe) { }
        catch(IOException ioe)
        {
            GUICreator.showAlert("Wystąpił błąd odczytu.");
            System.exit(1);
        }
    }

    private void run(GraphicsContext gc_)
    {
        if(playerList.size() > 0)
        {
            if(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)
            {
                lockMutex();
                playerPizeCollision();
                playerSpikeCollision();
                playerCatapultCollision();
                stoneCatapultCollision();
                openingKeyCatapultCollision();
                playerLeverCollision();
                stoneAndPlayerPressurePlateCollision();
                playerGrowingCollision();
                manaGrowth();

                if(tick % TICK_CHECK == TICK_CHECK - 1)
                {
                    advanceGrowingBlock();
                }

                for(int i = 0; i < playerList.size(); i++)
                {
                    playerList.get(i).enchantmentsTimeAdvance();
                }
                unlockMutex();
            }

            tick++;
            tick = tick % TICK_CHECK;
        }
    }

    public static int getGameboardX()
    {
        return GAMEBOARD_X;
    }

    public static int getGameboardY()
    {
        return GAMEBOARD_Y;
    }

    public static boolean getServerStatus()
    {
        return serverOn;
    }

    public static List<Teleport> getTeleportList()
    {
        return teleportList;
    }

    public static List<Player> getPlayerList()
    {
        return playerList;
    }

    public static List<Stone> getStoneList()
    {
        return stoneList;
    }

    public static List<Prize> getPrizeList()
    {
        return prizeList;
    }

    public static List<Gate> getGateList()
    {
        return gateList;
    }

    public static List<OpeningKey> getOpeningKeyList()
    {
        return openingKeyList;
    }

    public static List<Catapult> getCatapultList()
    {
        return catapultList;
    }

    public static List<Lever> getLeverList()
    {
        return leverList;
    }

    public static List<PressurePlate> getPressurePlateList()
    {
        return pressurePlateList;
    }

    public static List<Pitfall> getPitfallList()
    {
        return pitfallList;
    }

    public static List<Spike> getSpikeList()
    {
        return spikeList;
    }

    public static List<HealthPack> getHealthPackList()
    {
        return healthPackList;
    }

    public static Tile getTileByIndex(int x, int y)
    {
        return Map[x][y];
    }

    private void playerPizeCollision()
    {
        for(int j = 0; j < playerList.size(); j++)
        {
            for(int i = 0; i < prizeList.size(); i++)
            {
                if(playerList.get(j).getCoordX() == prizeList.get(i).getCoordX() && playerList.get(j).getCoordY() == prizeList.get(i).getCoordY() && !playerList.get(j).isWin())
                {
                    playerList.get(j).setWin();
                    playerList.get(j).setXY(SPAWN_X + winPlayersCount, SPAWN_Y);
                    winPlayersCount++;
                }
            }
        }
    }

    private void playerSpikeCollision()
    {
        for(int j = 0; j < playerList.size(); j++)
        {
            for(int i = 0; i < spikeList.size(); i++)
            {
                if(playerList.get(j).getCoordX() == spikeList.get(i).getCoordX() && playerList.get(j).getCoordY() == spikeList.get(i).getCoordY())
                {
                    playerList.get(j).addHealth(-spikeList.get(i).getDamage());
                }
            }
        }
    }

    private void playerCatapultCollision()
    {
        for(int i = 0; i < catapultList.size(); i++)
        {
            for(int j = 0; j < playerList.size(); j++)
            {
                if(playerList.get(j).getCoordX() == catapultList.get(i).getCoordX() && playerList.get(j).getCoordY() == catapultList.get(i).getCoordY())
                {
                    playerList.get(j).moveByCatapult(catapultList.get(i).getDirection(), catapultList.get(i).getPower());
                }
            }
        }
    }

    private void playerLeverCollision()
    {
        for(int i = 0; i < leverList.size(); i++)
        {
            boolean wasLeverPowered = leverList.get(i).wasPowered();
            boolean pressed = false;

            for(int j = 0; j < playerList.size(); j++)
            {
                if(leverList.get(i).getCoordX() == playerList.get(j).getCoordX() && leverList.get(i).getCoordY() == playerList.get(j).getCoordY())
                {
                    leverList.get(i).turnOn();
                    pressed = true;
                }
            }

            if(!pressed)
            {
                leverList.get(i).turnOff();
            }

            if(leverList.get(i).isPowered() == true && wasLeverPowered == false)
            {
                leverList.get(i).togglePortal();
            }

            leverList.get(i).setWasPowered(leverList.get(i).isPowered());
        }
    }

    private void stoneAndPlayerPressurePlateCollision()
    {
        for(int i = 0; i < pressurePlateList.size(); i++)
        {
            boolean wasPressurePlatePowered = pressurePlateList.get(i).wasPowered();
            boolean pressed = false;

            for(int j = 0; j < stoneList.size(); j++)
            {
                if(pressurePlateList.get(i).getCoordX() == stoneList.get(j).getCoordX() && pressurePlateList.get(i).getCoordY() == stoneList.get(j).getCoordY())
                {
                    pressurePlateList.get(i).turnOn();
                    pressed = true;
                }
            }

            for(int j = 0; j < playerList.size(); j++)
            {
                if(pressurePlateList.get(i).getCoordX() == playerList.get(j).getCoordX() && pressurePlateList.get(i).getCoordY() == playerList.get(j).getCoordY())
                {
                    pressurePlateList.get(i).turnOn();
                    pressed = true;
                }
            }

            if(!pressed)
            {
                pressurePlateList.get(i).turnOff();
            }

            if(pressurePlateList.get(i).isPowered() != wasPressurePlatePowered)
            {
                pressurePlateList.get(i).togglePortal();
            }

            pressurePlateList.get(i).setWasPowered(pressurePlateList.get(i).isPowered());
        }
    }

    private void stoneCatapultCollision()
    {
        for(int i = 0; i < stoneList.size(); i++)
        {
            for(int j = 0; j < catapultList.size(); j++)
            {
                if(stoneList.get(i).getCoordX() == catapultList.get(j).getCoordX() && stoneList.get(i).getCoordY() == catapultList.get(j).getCoordY())
                {
                    stoneList.get(i).moveByCatapult(catapultList.get(j).getDirection(), catapultList.get(j).getPower());
                }
            }
        }
    }

    private void openingKeyCatapultCollision()
    {
        for(int i = 0; i < openingKeyList.size(); i++)
        {
            for(int j = 0; j < catapultList.size(); j++)
            {
                if(openingKeyList.get(i).getCoordX() == catapultList.get(j).getCoordX() && openingKeyList.get(i).getCoordY() == catapultList.get(j).getCoordY())
                {
                    openingKeyList.get(i).moveByCatapult(catapultList.get(j).getDirection(), catapultList.get(j).getPower());
                }
            }
        }
    }

    private void playerGrowingCollision()
    {
        for(int i = 0; i < playerList.size(); i++)
        {
            if(Map[playerList.get(i).getCoordX()][playerList.get(i).getCoordY()].getType() == TileType.GROWING)
            {
                playerList.get(i).addHealth(-Tile.getGrowingDamage());
            }
        }
    }

    private void advanceGrowingBlock()
    {
        for(int i = 0; i < GAMEBOARD_X; i++)
        {
            for(int j = 0; j < GAMEBOARD_Y; j++)
            {
                if(Map[i][j].getType() == TileType.GROWING)
                {
                    if(i > 0)
                    {
                        if(Map[i - 1][j].getType() == TileType.EMPTY)
                        {
                            Map[i - 1][j].setBeforeGrowing();
                        }
                    }

                    if(i < GAMEBOARD_X - 1)
                    {
                        if(Map[i + 1][j].getType() == TileType.EMPTY)
                        {
                            Map[i + 1][j].setBeforeGrowing();
                        }
                    }

                    if(j > 0)
                    {
                        if(Map[i][j - 1].getType() == TileType.EMPTY)
                        {
                            Map[i][j - 1].setBeforeGrowing();
                        }
                    }

                    if(j < GAMEBOARD_Y - 1)
                    {
                        if(Map[i][j + 1].getType() == TileType.EMPTY)
                        {
                            Map[i][j + 1].setBeforeGrowing();
                        }
                    }
                }
            }
        }

        for(int i = 0; i < GAMEBOARD_X; i++)
        {
            for(int j = 0; j < GAMEBOARD_Y; j++)
            {
                if(Map[i][j].getType() == TileType.BEFORE_GROWING)
                {
                    Map[i][j].setGrowing();
                }
            }
        }
    }

    private void manaGrowth()
    {
        if(mana < MANA_LIMIT)
        {
            mana = mana + MANA_TICK_GROWTH;
        }

        if(mana > MANA_LIMIT)
        {
            mana = MANA_LIMIT;
        }
    }

    public static void lockMutex()
    {
        gameLock.lock();
    }

    public static void unlockMutex()
    {
        gameLock.unlock();
    }

    public static boolean harmPlayer(Player player, int damage)
    {
        if(!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END))
        {
            return false;
        }

        if(player.isDead() || player.isWin())
        {
            return false;
        }

        if(mana < DAMAGE_COST)
        {
            return false;
        }

        mana = mana - DAMAGE_COST;
        player.addHealth(-damage);
        return true;
    }

    public static boolean teleportPlayer(Player player, int x, int y)
    {
        if(!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END))
        {
            return false;
        }

        if(player.isDead() || player.isWin())
        {
            return false;
        }

        if(mana < TELEPORT_COST)
        {
            return false;
        }

        if(x < 0 || y < 0)
        {
            return false;
        }

        if(x >= GAMEBOARD_X || y >= GAMEBOARD_Y)
        {
            return false;
        }

        if(Map[x][y].getType() == TileType.BLOCK)
        {
            return false;
        }

        mana = mana - TELEPORT_COST;
        player.setXY(x, y);
        return true;
    }

    public static boolean slowPlayer(Player player, int ticks)
    {
        if(!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END))
        {
            return false;
        }

        if(player.isDead() || player.isWin())
        {
            return false;
        }

        if(mana < SLOW_COST)
        {
            return false;
        }

        mana = mana - SLOW_COST;
        player.setSlow(ticks);
        return true;
    }

    public static boolean revertPlayer(Player player, int ticks)
    {
        if(!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END))
        {
            return false;
        }

        if(player.isDead() || player.isWin())
        {
            return false;
        }

        if(mana < REVERSE_COST)
        {
            return false;
        }

        mana = mana - REVERSE_COST;
        player.setReverted(ticks);
        return true;
    }

    public static boolean blindPlayer(Player player, int ticks)
    {
        if(!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END))
        {
            return false;
        }

        if(player.isDead() || player.isWin())
        {
            return false;
        }

        if(mana < BLIND_COST)
        {
            return false;
        }

        mana = mana - BLIND_COST;
        player.setBlind(ticks);
        return true;
    }

    public static boolean lightPlayer(Player player, int ticks)
    {
        if(!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END))
        {
            return false;
        }

        if(player.isDead() || player.isWin())
        {
            return false;
        }

        if(mana < LIGHT_COST)
        {
            return false;
        }

        mana = mana - LIGHT_COST;
        player.setLight(ticks);
        return true;
    }

    private static void togglePortals()
    {
        for(int i = 0; i < teleportList.size(); i++)
        {
            teleportList.get(i).toggle();
        }
    }

    private static void acceptClients()
    {
        try
        {
            while(serverOn)
            {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(SOCKET_TIMEOUT);

                ObjectInputStream socketInputStream = new ObjectInputStream(socket.getInputStream());
                GameMessage gameMessage = (GameMessage) socketInputStream.readObject();
                if(gameMessage.getGameRequest() != GameRequest.JOIN)
                {
                    GameMessage responseMessage = new GameMessage(GameRequest.KICK);
                    ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    socketOutputStream.writeObject(responseMessage);
                    socket.close();
                }

                if(playerList.size() == MAX_PLAYERS)
                {
                    GameMessage responseMessage = new GameMessage(GameRequest.KICK);
                    ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    socketOutputStream.writeObject(responseMessage);
                    socket.close();
                }
                else if(!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END))
                {
                    GameMessage responseMessage = new GameMessage(GameRequest.KICK);
                    ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    socketOutputStream.writeObject(responseMessage);
                    socket.close();
                }
                else
                {
                    GameMessage responseMessage = new GameMessage(GameRequest.OK);
                    ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    socketOutputStream.writeObject(responseMessage);

                    Player player = new Player(SPAWN_X, SPAWN_Y, socket);
                    lockMutex();
                    playerList.add(player);
                    unlockMutex();
                    player.startThread();
                }
            }
        }
        catch(IOException ioe) { }
        catch(ClassNotFoundException cnfe)
        {
            GUICreator.showAlert("Wystąpił błąd");
        }
    }

    public static void closeGame()
    {
        try
        {
            serverSocket.close();
            serverOn = false;
            acceptThread.interrupt();

            for(int i = 0; i < playerList.size(); i++)
            {
                playerList.get(i).endThreadAndRemove();
            }
        }
        catch(IOException ioe)
        {
            GUICreator.showAlert("Wystąpił błąd odczytu.");
            ioe.printStackTrace();
        }
    }
}
