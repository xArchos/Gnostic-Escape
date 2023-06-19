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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

    public static final int GAMEBOARD_Y = 24;
    public static final int GAMEBOARD_X = 24;
    private static final int SPAWN_X = 0;
    private static final int SPAWN_Y = 0;
    private static final int WINDOW_HEIGHT = GAMEBOARD_Y * TILE_Y + 50;
    public static final int WINDOW_WIDTH = 1300;

    public static int mana = INITIAL_MANA;
    static int winPlayersCount = 0;
    static int deadPlayersCount = 0;
    private static Tile[][] Tiles = new Tile[GAMEBOARD_X][GAMEBOARD_Y];
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

    public static void main(String[] args)
    {
        Player p1 = new Player(2, 2);
        playerList.add(p1);

        Stone s1 = new Stone(1, 1);
        Stone s2 = new Stone(1, 2);
        stoneList.add(s1);
        stoneList.add(s2);

        OpeningKey ok = new OpeningKey(18, 18);
        openingKeyList.add(ok);

        for(int i = 0; i < GAMEBOARD_X; i++)
        {
            for(int j = 0; j < GAMEBOARD_Y; j++)
            {
                Tiles[i][j] = new Tile();
            }
        }

        for(int i = 14; i < 21; i++)
        {
            Tiles[i][14].setBlock();
        }

        for(int i = 14; i < 21; i++)
        {
            Tiles[14][i].setBlock();
        }

        for(int i = 14; i < 21; i++)
        {
            Tiles[i][20].setBlock();
        }

        for(int i = 14; i < 21; i++)
        {
            Tiles[20][i].setBlock();
        }

        Teleport t1 = new Teleport(10, 10, 17, 17);
        teleportList.add(t1);
        Teleport t2 = new Teleport(4, 4, 5, 5);
        teleportList.add(t2);

        PressurePlate pl1 = new PressurePlate(6, 6, t2);
        pressurePlateList.add(pl1);

        Prize pr1 = new Prize(10, 0);
        prizeList.add(pr1);

        for(int i = 0; i < 5; i++)
        {
            Tiles[7][i].setBlock();
        }

        for(int i = 0; i < 5; i++)
        {
            Tiles[13][i].setBlock();
        }

        for(int i = 7; i < 14; i++)
        {
            Tiles[i][5].setBlock();
        }

        Tiles[10][5].setEmpty();

        Gate g1 = new Gate(10, 5);
        gateList.add(g1);

        Spike spike1 = new Spike(3, 11, 200);
        spikeList.add(spike1);

        Pitfall pitfall1 = new Pitfall(14, 6, 200);
        pitfallList.add(pitfall1);

        HealthPack h1 = new HealthPack(22, 16, 150);
        healthPackList.add(h1);

        Catapult c1 = new Catapult(15, 5, 5, Direction.LEFT);
        catapultList.add(c1);

        Catapult c2 = new Catapult(0, 23, 5, Direction.DOWN);
        catapultList.add(c2);

        Catapult c3 = new Catapult(23, 0, 5, Direction.DOWN);
        catapultList.add(c3);

        Catapult c4 = new Catapult(23, 23, 5, Direction.DOWN);
        catapultList.add(c4);

        Lever l1 = new Lever(17, 10, t1);
        leverList.add(l1);

        Tiles[0][15].setGrowing();

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

            gui.createBackground();
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
            System.out.println("Brakuje plików.");
            System.exit(1);
        }
        catch(EOFException eofe) { }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    private void run(GraphicsContext gc_) //TYLKO 1 GRACZ, nie uzywać argumentu (docelowo zrobić z tego po prostu "void run()")
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
        return Tiles[x][y];
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
            if(Tiles[playerList.get(i).getCoordX()][playerList.get(i).getCoordY()].getType() == TileType.GROWING)
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
                if(Tiles[i][j].getType() == TileType.GROWING)
                {
                    if(i > 0)
                    {
                        if(Tiles[i - 1][j].getType() == TileType.EMPTY)
                        {
                            Tiles[i - 1][j].setBeforeGrowing();
                        }
                    }

                    if(i < GAMEBOARD_X - 1)
                    {
                        if(Tiles[i + 1][j].getType() == TileType.EMPTY)
                        {
                            Tiles[i + 1][j].setBeforeGrowing();
                        }
                    }

                    if(j > 0)
                    {
                        if(Tiles[i][j - 1].getType() == TileType.EMPTY)
                        {
                            Tiles[i][j - 1].setBeforeGrowing();
                        }
                    }

                    if(j < GAMEBOARD_Y - 1)
                    {
                        if(Tiles[i][j + 1].getType() == TileType.EMPTY)
                        {
                            Tiles[i][j + 1].setBeforeGrowing();
                        }
                    }
                }
            }
        }

        for(int i = 0; i < GAMEBOARD_X; i++)
        {
            for(int j = 0; j < GAMEBOARD_Y; j++)
            {
                if(Tiles[i][j].getType() == TileType.BEFORE_GROWING)
                {
                    Tiles[i][j].setGrowing();
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

    public static void harmPlayer(Player player, int damage)
    {
        if(mana < DAMAGE_COST)
        {
            return;
        }

        mana = mana - DAMAGE_COST;
        player.addHealth(-damage);
    }

    public static void teleportPlayer(Player player, int x, int y)
    {
        if(mana < TELEPORT_COST)
        {
            return;
        }

        if(x < 0 || y < 0)
        {
            return;
        }

        if(x >= GAMEBOARD_X || y >= GAMEBOARD_Y)
        {
            return;
        }

        if(Tiles[x][y].getType() == TileType.BLOCK)
        {
            return;
        }

        mana = mana - TELEPORT_COST;
        player.setXY(x, y);
    }

    public static void slowPlayer(Player player, int ticks)
    {
        if(mana < SLOW_COST)
        {
            return;
        }

        mana = mana - SLOW_COST;
        player.setSlow(ticks);
    }

    public static void revertPlayer(Player player, int ticks)
    {
        if(mana < REVERSE_COST)
        {
            return;
        }

        mana = mana - REVERSE_COST;
        player.setReverted(ticks);
    }

    public static void blindPlayer(Player player, int ticks)
    {
        if(mana < BLIND_COST)
        {
            return;
        }

        mana = mana - BLIND_COST;
        player.setBlind(ticks);
    }

    public static void lightPlayer(Player player, int ticks)
    {
        if(mana < LIGHT_COST)
        {
            return;
        }

        mana = mana - LIGHT_COST;

        player.setLight(ticks);
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
        catch(IOException ioe) //zawsze wypisze stack przy zamknięciu
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
            ioe.printStackTrace();
        }
    }
}
