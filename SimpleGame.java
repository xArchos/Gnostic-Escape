import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SimpleGame extends Application {
    //private static final int GAMEBOARD_Y = 24;
    //private static final int GAMEBOARD_X = 24;
    //private static Tile[][] Tiles = new Tile[GAMEBOARD_X][GAMEBOARD_Y];
    private static int tick = 0;
    private static final int TICK_CHECK = 500;
    private static final int EFFECT_TICKS = 1500;
    static final int TILE_Y = 30; //dodać gettery?
    static final int TILE_X = 30;

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

    //dostęp pakietowy aby nie tworzyć do tego wszystkiego getterów
    static Image PlayerImage = null;
    static Image emptyTileImage = null;
    static Image fullTileImage = null;
    static Image bluePortalImage = null;
    static Image orangePortalImage = null;
    static Image blueOffPortalImage = null;
    static Image orangeOffPortalImage = null;
    static Image stoneImage = null;
    static Image magicStoneImage = null;
    static Image prizeImage = null;
    static Image gateImage = null;
    static Image growingImage = null;
    static Image openingKeyImage = null;
    static Image healthPackImage = null;
    static Image spikeImage = null;
    static Image pitfallImage = null;
    static Image pressurePlateOnImage = null;
    static Image pressurePlateOffImage = null;
    static Image leverOnImage = null;
    static Image leverOffImage = null;
    static Image catapultUpImage = null;
    static Image catapultLeftImage = null;
    static Image catapultRightImage = null;
    static Image catapultDownImage = null;
    static Image revertedPlayerImage = null;

    static Canvas canvas = null;
    static GraphicsContext gc = null;

    private static Tile[][] Tiles;
    private static int GAMEBOARD_X;
    private static int GAMEBOARD_Y;

    public static void main(String[] args) {

        MapLoader mapLoader = new MapLoader();
        Tiles = mapLoader.loadMap("map.txt");
        GAMEBOARD_X = mapLoader.getGameboardX();
        GAMEBOARD_Y = mapLoader.getGameboardY();

        launch(args);
    }

    public static int getGameboardX() {
        return GAMEBOARD_X;
    }

    public static int getGameboardY() {
        return GAMEBOARD_Y;
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
        return Tiles[x][y];
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gnostic Escape");
        primaryStage.show();


        try {
            imagesSetup();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Brakuje plików.");
            System.exit(1);
        }

        canvasSetup();

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);

        primaryStage.setScene(new Scene(new StackPane(canvas)));
        primaryStage.show();

        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (playerList.size() > 0) {
                    switch (event.getCode()) {
                        case W -> playerList.get(0).move(Direction.UP);
                        case S -> playerList.get(0).move(Direction.DOWN);
                        case A -> playerList.get(0).move(Direction.LEFT);
                        case D -> playerList.get(0).move(Direction.RIGHT);
                        case V -> togglePortals();
                        case R -> revertPlayer(playerList.get(0), EFFECT_TICKS);
                        case T -> blindPlayer(playerList.get(0), EFFECT_TICKS);
                        case Y -> slowPlayer(playerList.get(0), EFFECT_TICKS);
                        case U -> lightPlayer(playerList.get(0), EFFECT_TICKS);
                        default -> {
                        }
                    }
                }
            }
        });

        tl.play();

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                drawGame();
            }
        }.start();
    }

    private void playerPrizeCollision() {
        for (Prize prize : prizeList) {
            if (playerList.get(0).getCoordX() == prize.getCoordX() && playerList.get(0).getCoordY() == prize.getCoordY()) {
                System.exit(0);
            }
        }
    }

    private void playerSpikeCollision() {
        for (Spike spike : spikeList) {
            if (playerList.get(0).getCoordX() == spike.getCoordX() && playerList.get(0).getCoordY() == spike.getCoordY()) {
                playerList.get(0).addHealth(-spike.getDamage());
            }
        }
    }

    private void playerCatapultCollision() {
        for (Catapult catapult : catapultList) {
            if (playerList.get(0).getCoordX() == catapult.getCoordX() && playerList.get(0).getCoordY() == catapult.getCoordY()) {
                playerList.get(0).moveByCatapult(catapult.getDirection(), catapult.getPower());
            }
        }
    }

    private void playerLeverCollision() {
        for (Lever lever : leverList) {
            boolean wasLeverPowered = lever.wasPowered();
            boolean pressed = false;

            for (Player player : playerList) {
                if (lever.getCoordX() == player.getCoordX() && lever.getCoordY() == player.getCoordY()) {
                    lever.turnOn();
                    pressed = true;
                }
            }

            if (!pressed) {
                lever.turnOff();
            }

            if (lever.isPowered() && !wasLeverPowered) {
                lever.togglePortal();
            }

            lever.setWasPowered(lever.isPowered());
        }
    }

    private void stoneAndPlayerPressurePlateCollision() {
        for (PressurePlate pressurePlate : pressurePlateList) {
            boolean wasPressurePlatePowered = pressurePlate.wasPowered();
            boolean pressed = false;

            for (Stone stone : stoneList) {
                if (pressurePlate.getCoordX() == stone.getCoordX() && pressurePlate.getCoordY() == stone.getCoordY()) {
                    pressurePlate.turnOn();
                    pressed = true;
                }
            }

            for (Player player : playerList) {
                if (pressurePlate.getCoordX() == player.getCoordX() && pressurePlate.getCoordY() == player.getCoordY()) {
                    pressurePlate.turnOn();
                    pressed = true;
                }
            }

            if (!pressed) {
                pressurePlate.turnOff();
            }

            if (pressurePlate.isPowered() != wasPressurePlatePowered) {
                pressurePlate.togglePortal();
            }

            pressurePlate.setWasPowered(pressurePlate.isPowered());
        }
    }

    private void stoneCatapultCollision() {
        for (Stone stone : stoneList) {
            for (Catapult catapult : catapultList) {
                if (stone.getCoordX() == catapult.getCoordX() && stone.getCoordY() == catapult.getCoordY()) {
                    stone.moveByCatapult(catapult.getDirection(), catapult.getPower());
                }
            }
        }
    }

    private void openingKeyCatapultCollision() {
        for (OpeningKey openingKey : openingKeyList) {
            for (Catapult catapult : catapultList) {
                if (openingKey.getCoordX() == catapult.getCoordX() && openingKey.getCoordY() == catapult.getCoordY()) {
                    openingKey.moveByCatapult(catapult.getDirection(), catapult.getPower());
                }
            }
        }
    }

    private void playerGrowingCollision() {
        for (Player player : playerList) {
            if (Tiles[player.getCoordX()][player.getCoordY()].getType() == TileType.GROWING) {
                player.addHealth(-Tile.getGrowingDamage());
            }
        }
    }

    private void advanceGrowingBlock() {
        for (int i = 0; i < GAMEBOARD_X; i++) {
            for (int j = 0; j < GAMEBOARD_Y; j++) {
                if (Tiles[i][j].getType() == TileType.GROWING) {
                    if (i > 0) {
                        if (Tiles[i - 1][j].getType() == TileType.EMPTY) {
                            Tiles[i - 1][j].setBeforeGrowing();
                        }
                    }

                    if (i < GAMEBOARD_X - 1) {
                        if (Tiles[i + 1][j].getType() == TileType.EMPTY) {
                            Tiles[i + 1][j].setBeforeGrowing();
                        }
                    }

                    if (j > 0) {
                        if (Tiles[i][j - 1].getType() == TileType.EMPTY) {
                            Tiles[i][j - 1].setBeforeGrowing();
                        }
                    }

                    if (j < GAMEBOARD_Y - 1) {
                        if (Tiles[i][j + 1].getType() == TileType.EMPTY) {
                            Tiles[i][j + 1].setBeforeGrowing();
                        }
                    }
                }
            }
        }

        for (int i = 0; i < GAMEBOARD_X; i++) {
            for (int j = 0; j < GAMEBOARD_Y; j++) {
                if (Tiles[i][j].getType() == TileType.BEFORE_GROWING) {
                    Tiles[i][j].setGrowing();
                }
            }
        }
    }

    private void run(GraphicsContext gc_) //TYLKO 1 GRACZ, nie uzywać argumentu (docelowo zrobić z tego po prostu "void run()")
    {
        if (playerList.size() > 0) {
            playerPrizeCollision();
            playerSpikeCollision();
            playerCatapultCollision();
            stoneCatapultCollision();
            openingKeyCatapultCollision();
            playerLeverCollision();
            stoneAndPlayerPressurePlateCollision();
            playerGrowingCollision();

            if (tick % TICK_CHECK == TICK_CHECK - 1) {
                advanceGrowingBlock();
            }

            for (Player player : playerList) {
                player.enchantmentsTimeAdvance();
            }

            tick++;
            tick = tick % TICK_CHECK;
        }
    }

    private void harmPlayer(Player player, int damage) {
        player.addHealth(-damage);
    }

    private void teleportPlayer(Player player, int x, int y) {
        if (x < 0 || y < 0) {
            return;
        }

        if (x >= GAMEBOARD_X || y >= GAMEBOARD_Y) {
            return;
        }

        if (Tiles[x][y].getType() == TileType.BLOCK) {
            return;
        }

        player.setXY(x, y);
    }

    private void slowPlayer(Player player, int ticks) {
        player.setSlow(ticks);
    }

    private void revertPlayer(Player player, int ticks) {
        player.setReverted(ticks);
    }

    private void blindPlayer(Player player, int ticks) {
        player.setBlind(ticks);
    }

    private void lightPlayer(Player player, int ticks) {
        player.setLight(ticks);
    }

    private static void togglePortals() {
        for (int i = 0; i < teleportList.size(); i++) {
            teleportList.get(i).toggle();
        }
    }

    private static void imagesSetup() throws FileNotFoundException {
        FileInputStream ImgInputStream;

        ImgInputStream = new FileInputStream("./img/player.png");
        PlayerImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/tile.png");
        emptyTileImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/block.png");
        fullTileImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/blue_portal.png");
        bluePortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/orange_portal.png");
        orangePortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/blue_off_portal.png");
        blueOffPortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/orange_off_portal.png");
        orangeOffPortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/stone.png");
        stoneImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/magic_stone.png");
        magicStoneImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/prize.png");
        prizeImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/gate.png");
        gateImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/growing.png");
        growingImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/key.png");
        openingKeyImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/health_pack.png");
        healthPackImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/spike.png");
        spikeImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/pitfall.png");
        pitfallImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/plateOn.png");
        pressurePlateOnImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/plateOff.png");
        pressurePlateOffImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/leverOn.png");
        leverOnImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/leverOff.png");
        leverOffImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultUp.png");
        catapultUpImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultDown.png");
        catapultDownImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultRight.png");
        catapultRightImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultLeft.png");
        catapultLeftImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/revertedPlayer.png");
        revertedPlayerImage = new Image(ImgInputStream);
    }

    private static void canvasSetup() {
        canvas = new Canvas(GAMEBOARD_X * TILE_X, GAMEBOARD_Y * TILE_Y);
        gc = canvas.getGraphicsContext2D();
    }

    private static void drawGame() {
        // Clear the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GAMEBOARD_Y * TILE_Y, GAMEBOARD_X * TILE_X);

        for (int i = 0; i < GAMEBOARD_X; i++) {
            for (int j = 0; j < GAMEBOARD_Y; j++) {
                Tiles[i][j].draw(i, j);
            }
        }

        for (Teleport teleport : teleportList) {
            teleport.draw();
        }

        for (Prize prize : prizeList) {
            prize.draw();
        }

        for (Gate gate : gateList) {
            gate.draw();
        }

        for (HealthPack healthPack : healthPackList) {
            healthPack.draw();
        }

        for (Spike spike : spikeList) {
            spike.draw();
        }

        for (Catapult catapult : catapultList) {
            catapult.draw();
        }

        for (PressurePlate pressurePlate : pressurePlateList) {
            pressurePlate.draw();
        }

        for (Lever lever : leverList) {
            lever.draw();
        }

        for (Pitfall pitfall : pitfallList) {
            pitfall.draw();
        }

        for (Player player : playerList) {
            player.draw();
        }

        for (Stone stone : stoneList) {
            stone.draw();
        }

        for (OpeningKey openingKey : openingKeyList) {
            openingKey.draw();
        }

        gc.setFill(Color.GREEN);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText(Integer.toString(playerList.get(0).getHealth()), 0, GAMEBOARD_X * TILE_X);

        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 15));
        gc.fillText("X " + Integer.toString(playerList.get(0).getCoordX()), 35, 35);
        gc.fillText("Y " + Integer.toString(playerList.get(0).getCoordY()), 105, 35);
    }
}

enum Direction {
    UP, DOWN, LEFT, RIGHT, EMPTY;
}

enum TileType {
    EMPTY, BLOCK, GROWING, BEFORE_GROWING;
}