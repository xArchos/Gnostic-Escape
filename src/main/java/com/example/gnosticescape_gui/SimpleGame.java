package com.example.gnosticescape_gui;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleGame extends Application {
    private static final int PORT = 1388;
    private static final int SOCKET_TIMEOUT = 300000;
    private static final int MAX_PLAYERS = 3;
    static final int WIN_PLAYERS_END = 2;
    static final int DEAD_PLAYERS_END = 2;
    private static final int TICK_CHECK = 80;
    public static final int EFFECT_TICKS = 160;
    private static final int TICK_TIME_MILLIS = 100;
    static final int TILE_Y = 30;
    static final int TILE_X = 30;

    private static final int INITIAL_MANA = 200;
    public static final int MANA_LIMIT = 1000;
    private static final int MANA_TICK_GROWTH = 1;
    public static final int TELEPORT_COST = 400;
    public static final int DAMAGE_COST = 200;
    public static final int REVERSE_COST = 40;
    public static final int BLIND_COST = 80;
    public static final int LIGHT_COST = 600;
    public static final int SLOW_COST = 150;
    public static final int DAMAGE_VALUE = 148;


    public static int SPAWN_X = 0;
    public static int SPAWN_Y = 0;
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

    private static final List<Player> playerList = new ArrayList<Player>();
    private static final List<Stone> stoneList = new ArrayList<Stone>();
    private static final List<Teleport> teleportList = new ArrayList<Teleport>();
    private static final List<Prize> prizeList = new ArrayList<Prize>();
    private static final List<Gate> gateList = new ArrayList<Gate>();
    private static final List<OpeningKey> openingKeyList = new ArrayList<OpeningKey>();
    private static final List<HealthPack> healthPackList = new ArrayList<HealthPack>();
    private static final List<Spike> spikeList = new ArrayList<Spike>();
    private static final List<Pitfall> pitfallList = new ArrayList<Pitfall>();
    private static final List<PressurePlate> pressurePlateList = new ArrayList<PressurePlate>();
    private static final List<Lever> leverList = new ArrayList<Lever>();
    private static final List<Catapult> catapultList = new ArrayList<Catapult>();

    private static GUICreator gui = null;
    public static Tile[][] Map;
    public static int GAMEBOARD_Y;
    public static int GAMEBOARD_X;
    public static int WINDOW_HEIGHT;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        GUIMapLoadingController guiMapControl = new GUIMapLoadingController();

        Scene scene = new Scene(guiMapControl.root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gnostic Escape - Demiurg");
        primaryStage.setMaxWidth(1200);
        primaryStage.setMinWidth(1200);
        primaryStage.setMaxHeight(800);
        primaryStage.setMinHeight(800);
        primaryStage.show();
    }

    public static void gameSetup(Stage primaryStage) {
        try {
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

            tl.play();

            new AnimationTimer() {
                public void handle(long currentNanoTime) {
                    gui.drawServerGame();
                }
            }.start();

            acceptMethod = new Runnable() {
                public void run() {
                    acceptClients();
                }
            };
            acceptThread = new Thread(acceptMethod);
            acceptThread.start();

            primaryStage.setOnCloseRequest
                    (
                            new EventHandler<WindowEvent>() {
                                public void handle(WindowEvent we) {
                                    closeGame();
                                }
                            }
                    );

            gameLock = new ReentrantLock();
        } catch (FileNotFoundException fnfe) {
            GUICreator.showAlert("Brakuje plików.");
            System.exit(1);
        } catch (EOFException eofe) {
        } catch (IOException ioe) {
            GUICreator.showAlert("Wystąpił błąd odczytu.");
            System.exit(1);
        }
    }

    private static void run(GraphicsContext gc_) {
        if (playerList.size() > 0) {
            if (winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END) {
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

                if (tick % TICK_CHECK == TICK_CHECK - 1) {
                    advanceGrowingBlock();
                }

                for (Player player : playerList) {
                    player.enchantmentsTimeAdvance();
                }
                unlockMutex();
            }

            tick++;
            tick = tick % TICK_CHECK;
        }
    }

    public static int getGameboardX() {
        return GAMEBOARD_X;
    }

    public static int getGameboardY() {
        return GAMEBOARD_Y;
    }

    public static boolean getServerStatus() {
        return serverOn;
    }

    public static List<Teleport> getTeleportList() {
        return teleportList;
    }

    public static List<Player> getPlayerList() {
        return playerList;
    }

    public static List<Stone> getStoneList() {
        return stoneList;
    }

    public static List<Prize> getPrizeList() {
        return prizeList;
    }

    public static List<Gate> getGateList() {
        return gateList;
    }

    public static List<OpeningKey> getOpeningKeyList() {
        return openingKeyList;
    }

    public static List<Catapult> getCatapultList() {
        return catapultList;
    }

    public static List<Lever> getLeverList() {
        return leverList;
    }

    public static List<PressurePlate> getPressurePlateList() {
        return pressurePlateList;
    }

    public static List<Pitfall> getPitfallList() {
        return pitfallList;
    }

    public static List<Spike> getSpikeList() {
        return spikeList;
    }

    public static List<HealthPack> getHealthPackList() {
        return healthPackList;
    }

    public static Tile getTileByIndex(int x, int y) {
        return Map[x][y];
    }

    private static void playerPizeCollision() {
        for (Player player : playerList) {
            for (Prize prize : prizeList) {
                if (player.getCoordX() == prize.getCoordX() && player.getCoordY() == prize.getCoordY() && !player.isWin()) {
                    player.setWin();
                    player.setXY(SPAWN_X + winPlayersCount, SPAWN_Y);
                    winPlayersCount++;
                }
            }
        }
    }

    private static void playerSpikeCollision() {
        for (Player player : playerList) {
            for (Spike spike : spikeList) {
                if (player.getCoordX() == spike.getCoordX() && player.getCoordY() == spike.getCoordY()) {
                    player.addHealth(-spike.getDamage());
                }
            }
        }
    }

    private static void playerCatapultCollision() {
        for (Catapult catapult : catapultList) {
            for (Player player : playerList) {
                if (player.getCoordX() == catapult.getCoordX() && player.getCoordY() == catapult.getCoordY()) {
                    player.moveByCatapult(catapult.getDirection(), catapult.getPower());
                }
            }
        }
    }

    private static void playerLeverCollision() {
        for (int i = 0; i < leverList.size(); i++) {
            boolean wasLeverPowered = leverList.get(i).wasPowered();
            boolean pressed = false;

            for (int j = 0; j < playerList.size(); j++) {
                if (leverList.get(i).getCoordX() == playerList.get(j).getCoordX() && leverList.get(i).getCoordY() == playerList.get(j).getCoordY()) {
                    leverList.get(i).turnOn();
                    pressed = true;
                }
            }

            if (!pressed) {
                leverList.get(i).turnOff();
            }

            if (leverList.get(i).isPowered() && !wasLeverPowered) {
                leverList.get(i).togglePortal();
            }

            leverList.get(i).setWasPowered(leverList.get(i).isPowered());
        }
    }

    private static void stoneAndPlayerPressurePlateCollision() {
        for (int i = 0; i < pressurePlateList.size(); i++) {
            boolean wasPressurePlatePowered = pressurePlateList.get(i).wasPowered();
            boolean pressed = false;

            for (Stone stone : stoneList) {
                if (pressurePlateList.get(i).getCoordX() == stone.getCoordX() && pressurePlateList.get(i).getCoordY() == stone.getCoordY()) {
                    pressurePlateList.get(i).turnOn();
                    pressed = true;
                }
            }

            for (Player player : playerList) {
                if (pressurePlateList.get(i).getCoordX() == player.getCoordX() && pressurePlateList.get(i).getCoordY() == player.getCoordY()) {
                    pressurePlateList.get(i).turnOn();
                    pressed = true;
                }
            }

            if (!pressed) {
                pressurePlateList.get(i).turnOff();
            }

            if (pressurePlateList.get(i).isPowered() != wasPressurePlatePowered) {
                pressurePlateList.get(i).togglePortal();
            }

            pressurePlateList.get(i).setWasPowered(pressurePlateList.get(i).isPowered());
        }
    }

    private static void stoneCatapultCollision() {
        for (Stone stone : stoneList) {
            for (Catapult catapult : catapultList) {
                if (stone.getCoordX() == catapult.getCoordX() && stone.getCoordY() == catapult.getCoordY()) {
                    stone.moveByCatapult(catapult.getDirection(), catapult.getPower());
                }
            }
        }
    }

    private static void openingKeyCatapultCollision() {
        for (int i = 0; i < openingKeyList.size(); i++) {
            for (int j = 0; j < catapultList.size(); j++) {
                if (openingKeyList.get(i).getCoordX() == catapultList.get(j).getCoordX() && openingKeyList.get(i).getCoordY() == catapultList.get(j).getCoordY()) {
                    openingKeyList.get(i).moveByCatapult(catapultList.get(j).getDirection(), catapultList.get(j).getPower());
                }
            }
        }
    }

    private static void playerGrowingCollision() {
        for (Player player : playerList) {
            if (Map[player.getCoordX()][player.getCoordY()].getType() == TileType.GROWING) {
                player.addHealth(-Tile.getGrowingDamage());
            }
        }
    }

    private static void advanceGrowingBlock() {
        for (int i = 0; i < GAMEBOARD_X; i++) {
            for (int j = 0; j < GAMEBOARD_Y; j++) {
                if (Map[i][j].getType() == TileType.GROWING) {
                    if (i > 0) {
                        if (Map[i - 1][j].getType() == TileType.EMPTY) {
                            Map[i - 1][j].setBeforeGrowing();
                        }
                    }

                    if (i < GAMEBOARD_X - 1) {
                        if (Map[i + 1][j].getType() == TileType.EMPTY) {
                            Map[i + 1][j].setBeforeGrowing();
                        }
                    }

                    if (j > 0) {
                        if (Map[i][j - 1].getType() == TileType.EMPTY) {
                            Map[i][j - 1].setBeforeGrowing();
                        }
                    }

                    if (j < GAMEBOARD_Y - 1) {
                        if (Map[i][j + 1].getType() == TileType.EMPTY) {
                            Map[i][j + 1].setBeforeGrowing();
                        }
                    }
                }
            }
        }

        for (int i = 0; i < GAMEBOARD_X; i++) {
            for (int j = 0; j < GAMEBOARD_Y; j++) {
                if (Map[i][j].getType() == TileType.BEFORE_GROWING) {
                    Map[i][j].setGrowing();
                }
            }
        }
    }

    private static void manaGrowth() {
        if (mana < MANA_LIMIT) {
            mana = mana + MANA_TICK_GROWTH;
        }

        if (mana > MANA_LIMIT) {
            mana = MANA_LIMIT;
        }
    }

    public static void lockMutex() {
        gameLock.lock();
    }

    public static void unlockMutex() {
        gameLock.unlock();
    }

    public static boolean harmPlayer(Player player, int damage) {
        if (!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)) {
            return false;
        }

        if (player.isDead() || player.isWin()) {
            return false;
        }

        if (mana < DAMAGE_COST) {
            return false;
        }

        mana = mana - DAMAGE_COST;
        player.addHealth(-damage);
        return true;
    }

    public static boolean teleportPlayer(Player player, int x, int y) {
        if (!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)) {
            return false;
        }

        if (player.isDead() || player.isWin()) {
            return false;
        }

        if (mana < TELEPORT_COST) {
            return false;
        }

        if (x < 0 || y < 0) {
            return false;
        }

        if (x >= GAMEBOARD_X || y >= GAMEBOARD_Y) {
            return false;
        }

        if (Map[x][y].getType() == TileType.BLOCK) {
            return false;
        }

        mana = mana - TELEPORT_COST;
        player.setXY(x, y);
        return true;
    }

    public static boolean slowPlayer(Player player, int ticks) {
        if (!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)) {
            return false;
        }

        if (player.isDead() || player.isWin()) {
            return false;
        }

        if (mana < SLOW_COST) {
            return false;
        }

        mana = mana - SLOW_COST;
        player.setSlow(ticks);
        return true;
    }

    public static boolean revertPlayer(Player player, int ticks) {
        if (!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)) {
            return false;
        }

        if (player.isDead() || player.isWin()) {
            return false;
        }

        if (mana < REVERSE_COST) {
            return false;
        }

        mana = mana - REVERSE_COST;
        player.setReverted(ticks);
        return true;
    }

    public static boolean blindPlayer(Player player, int ticks) {
        if (!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)) {
            return false;
        }

        if (player.isDead() || player.isWin()) {
            return false;
        }

        if (mana < BLIND_COST) {
            return false;
        }

        mana = mana - BLIND_COST;
        player.setBlind(ticks);
        return true;
    }

    public static boolean lightPlayer(Player player, int ticks) {
        if (!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)) {
            return false;
        }

        if (player.isDead() || player.isWin()) {
            return false;
        }

        if (mana < LIGHT_COST) {
            return false;
        }

        mana = mana - LIGHT_COST;
        player.setLight(ticks);
        return true;
    }

    private static void togglePortals() {
        for (Teleport teleport : teleportList) {
            teleport.toggle();
        }
    }

    private static void acceptClients() {
        try {
            while (serverOn) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(SOCKET_TIMEOUT);

                ObjectInputStream socketInputStream = new ObjectInputStream(socket.getInputStream());
                GameMessage gameMessage = (GameMessage) socketInputStream.readObject();
                if (gameMessage.getGameRequest() != GameRequest.JOIN) {
                    GameMessage responseMessage = new GameMessage(GameRequest.KICK);
                    ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    socketOutputStream.writeObject(responseMessage);
                    socket.close();
                }

                if (playerList.size() == MAX_PLAYERS) {
                    GameMessage responseMessage = new GameMessage(GameRequest.KICK);
                    ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    socketOutputStream.writeObject(responseMessage);
                    socket.close();
                } else if (!(winPlayersCount < WIN_PLAYERS_END && deadPlayersCount < DEAD_PLAYERS_END)) {
                    GameMessage responseMessage = new GameMessage(GameRequest.KICK);
                    ObjectOutputStream socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    socketOutputStream.writeObject(responseMessage);
                    socket.close();
                } else {
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
        } catch (IOException ioe) {
        } catch (ClassNotFoundException cnfe) {
            GUICreator.showAlert("Wystąpił błąd");
        }
    }

    public static void closeGame() {
        try {
            serverSocket.close();
            serverOn = false;
            acceptThread.interrupt();

            for (Player player : playerList) {
                player.endThreadAndRemove();
            }
        } catch (IOException ioe) {
            GUICreator.showAlert("Wystąpił błąd odczytu.");
            ioe.printStackTrace();
        }
    }
}
