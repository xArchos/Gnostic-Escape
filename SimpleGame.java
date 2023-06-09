import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

public class SimpleGame extends Application
{
    private static final int GAMEBOARD_Y = 24;
    private static final int GAMEBOARD_X = 24;
    private static Tile[][] Tiles = new Tile[GAMEBOARD_X][GAMEBOARD_Y];
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

        Spike spike1 = new Spike(3, 11, 2);
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

    public static int getGameboardX()
    {
        return GAMEBOARD_X;
    }

    public static int getGameboardY()
    {
        return GAMEBOARD_Y;
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

    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Gnostic Escape");
        primaryStage.show();

        try
        {
            imagesSetup();
        }
        catch(FileNotFoundException fnfe)
        {
            System.out.println("Brakuje plików.");
            System.exit(1);
        }

        canvasSetup();

		//JavaFX Timeline = free form animation defined by KeyFrames and their duration
		Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
		//number of cycles in animation INDEFINITE = repeat indefinitely
		tl.setCycleCount(Timeline.INDEFINITE);

        primaryStage.setScene(new Scene(new StackPane(canvas)));
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
                drawGame();
            }
        }.start();
    }

    private void playerPizeCollision()
    {
        for(int i = 0; i < prizeList.size(); i++)
        {
            if(playerList.get(0).getCoordX() == prizeList.get(i).getCoordX() && playerList.get(0).getCoordY() == prizeList.get(i).getCoordY())
            {
                System.exit(0);
            }
        }
    }

    private void playerSpikeCollision()
    {
        for(int i = 0; i < spikeList.size(); i++)
        {
            if(playerList.get(0).getCoordX() == spikeList.get(i).getCoordX() && playerList.get(0).getCoordY() == spikeList.get(i).getCoordY())
            {
                playerList.get(0).addHealth(-spikeList.get(i).getDamage());
            }
        }
    }

    private void playerCatapultCollision()
    {
        for(int i = 0; i < catapultList.size(); i++)
        {
            if(playerList.get(0).getCoordX() == catapultList.get(i).getCoordX() && playerList.get(0).getCoordY() == catapultList.get(i).getCoordY())
            {
                playerList.get(0).moveByCatapult(catapultList.get(i).getDirection(), catapultList.get(i).getPower());
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

    private void run(GraphicsContext gc_) //TYLKO 1 GRACZ, nie uzywać argumentu (docelowo zrobić z tego po prostu "void run()")
    {
        if(playerList.size() > 0)
        {
            playerPizeCollision();
            playerSpikeCollision();
            playerCatapultCollision();
            stoneCatapultCollision();
            openingKeyCatapultCollision();
            playerLeverCollision();
            stoneAndPlayerPressurePlateCollision();
            playerGrowingCollision();

            if(tick % TICK_CHECK == TICK_CHECK - 1)
            {
                advanceGrowingBlock();
            }

            for(int i = 0; i < playerList.size(); i++)
            {
                playerList.get(i).enchantmentsTimeAdvance();
            }

            tick++;
            tick = tick % TICK_CHECK;
        }
    }

    private void harmPlayer(Player player, int damage)
    {
        player.addHealth(-damage);
    }

    private void teleportPlayer(Player player, int x, int y)
    {
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

        player.setXY(x, y);
    }

    private void slowPlayer(Player player, int ticks)
    {
        player.setSlow(ticks);
    }

    private void revertPlayer(Player player, int ticks)
    {
        player.setReverted(ticks);
    }

    private void blindPlayer(Player player, int ticks)
    {
        player.setBlind(ticks);
    }

    private void lightPlayer(Player player, int ticks)
    {
        player.setLight(ticks);
    }

    private static void togglePortals()
    {
        for(int i = 0; i < teleportList.size(); i++)
        {
            teleportList.get(i).toggle();
        }
    }

    private static void imagesSetup() throws FileNotFoundException
    {
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

    private static void canvasSetup()
    {
        canvas = new Canvas(GAMEBOARD_X * TILE_X, GAMEBOARD_Y * TILE_Y);
		gc = canvas.getGraphicsContext2D();
    }

    private static void drawGame()
    {
        // Clear the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GAMEBOARD_Y * TILE_Y, GAMEBOARD_X * TILE_X);

        for(int i = 0; i < GAMEBOARD_X; i++)
        {
            for(int j = 0; j < GAMEBOARD_Y; j++)
            {
                Tiles[i][j].draw(i, j);
            }
        }

        for(int i = 0; i < teleportList.size(); i++)
        {
            teleportList.get(i).draw();
        }

        for(int i = 0; i < prizeList.size(); i++)
        {
            prizeList.get(i).draw();
        }

        for(int i = 0; i < gateList.size(); i++)
        {
            gateList.get(i).draw();
        }

        for(int i = 0; i < healthPackList.size(); i++)
        {
            healthPackList.get(i).draw();
        }

        for(int i = 0; i < spikeList.size(); i++)
        {
            spikeList.get(i).draw();
        }

        for(int i = 0; i < catapultList.size(); i++)
        {
            catapultList.get(i).draw();
        }

        for(int i = 0; i < pressurePlateList.size(); i++)
        {
            pressurePlateList.get(i).draw();
        }

        for(int i = 0; i < leverList.size(); i++)
        {
            leverList.get(i).draw();
        }

        for(int i = 0; i < pitfallList.size(); i++)
        {
            pitfallList.get(i).draw();
        }

        for(int i = 0; i < playerList.size(); i++)
        {
            playerList.get(i).draw();
        }

        for(int i = 0; i < stoneList.size(); i++)
        {
            stoneList.get(i).draw();
        }

        for(int i = 0; i < openingKeyList.size(); i++)
        {
            openingKeyList.get(i).draw();
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

enum Direction
{
    UP, DOWN, LEFT, RIGHT, EMPTY;
}

enum TileType
{
    EMPTY, BLOCK, GROWING, BEFORE_GROWING;
}

abstract class XYObject
{
    protected int coordX;
    protected int coordY;

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }
}

class Teleport extends XYObject
{
    protected int targetX;
    protected int targetY;
    boolean active;

    public Teleport(int coordX_, int coordY_, int targetX_, int targetY_)
    {
        coordX = coordX_;
        coordY = coordY_;
        targetX = targetX_;
        targetY = targetY_;
        active = true;
    }

    public Teleport(int coordX_, int coordY_, int targetX_, int targetY_, boolean active_)
    {
        coordX = coordX_;
        coordY = coordY_;
        targetX = targetX_;
        targetY = targetY_;
        active = active_;
    }

    public int getTargetX()
    {
        return targetX;
    }

    public int getTargetY()
    {
        return targetY;
    }

    public boolean isActive()
    {
        return active;
    }

    public void toggle()
    {
        active = !active;
    }

    public void turnOn()
    {
        active = true;
    }

    public void turnOff()
    {
        active = false;
    }

    public void draw()
    {
        if(active == true)
        {
            SimpleGame.gc.drawImage(SimpleGame.bluePortalImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            SimpleGame.gc.drawImage(SimpleGame.orangePortalImage, targetX * SimpleGame.TILE_X, targetY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
        else
        {
            SimpleGame.gc.drawImage(SimpleGame.blueOffPortalImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            SimpleGame.gc.drawImage(SimpleGame.orangeOffPortalImage, targetX * SimpleGame.TILE_X, targetY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }
}

abstract class Moveable extends XYObject
{
    protected boolean lastMoveTeleported = false;

    public boolean move(Direction direction)
    {
        int desiredX = coordX;
        int desiredY = coordY;

        switch(direction)
        {
            case UP:
                desiredY = desiredY - 1;
            break;
            case DOWN:
                desiredY = desiredY + 1;
            break;
            case RIGHT:
                desiredX = desiredX + 1;
            break;
            case LEFT:
                desiredX = desiredX - 1;
            break;
            default:
            break;
        }

        if(desiredX < 0 || desiredY < 0)
        {
            return false;
        }

        if(desiredX >= SimpleGame.getGameboardX() || desiredY >= SimpleGame.getGameboardY())
        {
            return false;
        }

        for(int i = 0; i < SimpleGame.getGateList().size(); i++)
        {
            if(desiredX == SimpleGame.getGateList().get(i).getCoordX() && desiredY == SimpleGame.getGateList().get(i).getCoordY())
            {
                return false;
            }
        }

        if(SimpleGame.getTileByIndex(desiredX, desiredY).getType() == TileType.BLOCK)
        {
            return false;
        }

        lastMoveTeleported = false;

        if(lastMoveTeleported == false)
        {
            for(int i = 0; i < SimpleGame.getTeleportList().size(); i++)
            {
                if(desiredX == SimpleGame.getTeleportList().get(i).getCoordX() && desiredY == SimpleGame.getTeleportList().get(i).getCoordY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getTargetX();
                    desiredY = SimpleGame.getTeleportList().get(i).getTargetY();
                    lastMoveTeleported = true;
                }
                else if(desiredX == SimpleGame.getTeleportList().get(i).getTargetX() && desiredY == SimpleGame.getTeleportList().get(i).getTargetY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getCoordX();
                    desiredY = SimpleGame.getTeleportList().get(i).getCoordY();
                    lastMoveTeleported = true;
                }
            }
        }

        for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
        {
            if(desiredX == SimpleGame.getStoneList().get(i).getCoordX() && desiredY == SimpleGame.getStoneList().get(i).getCoordY())
            {
                if(!SimpleGame.getStoneList().get(i).move(direction))
                {
                    return false;
                }
            }
        }

        for(int i = 0; i < SimpleGame.getOpeningKeyList().size(); i++)
        {
            if(desiredX == SimpleGame.getOpeningKeyList().get(i).getCoordX() && desiredY == SimpleGame.getOpeningKeyList().get(i).getCoordY())
            {
                if(!SimpleGame.getOpeningKeyList().get(i).move(direction))
                {
                    return false;
                }
            }
        }

        coordX = desiredX;
        coordY = desiredY;

        return true;
    }

    public void moveByCatapult(Direction direction, int power)
    {
        int desiredX = coordX;
        int desiredY = coordY;
        int toMove = power;

        while(toMove > 0)
        {
            if(direction == Direction.UP)
            {
                if(desiredY <= 0)
                {
                    break;
                }

                if(SimpleGame.getTileByIndex(desiredX, desiredY - 1).getType() == TileType.BLOCK)
                {
                    break;
                }

                desiredY = desiredY - 1;
            }
            else if(direction == Direction.DOWN)
            {
                if(desiredY >= SimpleGame.getGameboardY() - 1)
                {
                    break;
                }

                if(SimpleGame.getTileByIndex(desiredX, desiredY + 1).getType() == TileType.BLOCK)
                {
                    break;
                }

                desiredY = desiredY + 1;
            }
            else if(direction == Direction.LEFT)
            {
                if(desiredX <= 0)
                {
                    break;
                }

                if(SimpleGame.getTileByIndex(desiredX - 1, desiredY).getType() == TileType.BLOCK)
                {
                    break;
                }

                desiredX = desiredX - 1;
            }

            if(direction == Direction.RIGHT)
            {
                if(desiredX >= SimpleGame.getGameboardX() - 1)
                {
                    break;
                }

                if(SimpleGame.getTileByIndex(desiredX + 1, desiredY).getType() == TileType.BLOCK)
                {
                    break;
                }

                desiredX = desiredX + 1;
            }

            toMove--;
        }

        coordX = desiredX;
        coordY = desiredY;
    }
}

class Stone extends Moveable
{
    public Stone(int x, int y)
    {
        coordX = x;
        coordY = y;
        lastMoveTeleported = false;
    }

    public void draw()
    {
        SimpleGame.gc.drawImage(SimpleGame.stoneImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}

class Tile
{
    private TileType tileType;
    private static final int GROWING_DAMAGE = 1;

    public Tile()
    {
        tileType = TileType.EMPTY;
    }

    public Tile(TileType type)
    {
        tileType = type;
    }

    public void setEmpty()
    {
        tileType = TileType.EMPTY;
    }

    public void setBlock()
    {
        tileType = TileType.BLOCK;
    }

    public void setGrowing()
    {
        tileType = TileType.GROWING;
    }

    public void setBeforeGrowing()
    {
        tileType = TileType.BEFORE_GROWING;
    }

    public TileType getType()
    {
        return tileType;
    }

    public void draw(int x, int y)
    {
        switch(tileType)
        {
            case EMPTY:
                SimpleGame.gc.drawImage(SimpleGame.emptyTileImage, x * SimpleGame.TILE_X, y * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            break;
            case BLOCK:
                SimpleGame.gc.drawImage(SimpleGame.fullTileImage, x * SimpleGame.TILE_X, y * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            break;
            case GROWING:
            case BEFORE_GROWING:
                SimpleGame.gc.drawImage(SimpleGame.growingImage, x * SimpleGame.TILE_X, y * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            break;
        }
    }

    public static int getGrowingDamage()
    {
        return GROWING_DAMAGE;
    }
}

class Prize extends XYObject
{
    public Prize(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public void draw()
    {
        SimpleGame.gc.drawImage(SimpleGame.prizeImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}

class Player extends Moveable
{
    private static final int START_HEALTH = 1000;

    private int health;
    private boolean isDead = false;

    private boolean slowTurn = false;
    private int isSlow = 0;
    private int isReverted = 0;
    private int isBlind = 0;
    private int isLight = 0; //dorobić

    public Player(int x, int y)
    {
        coordX = x;
        coordY = y;
        health = START_HEALTH;
        lastMoveTeleported = false;
    }

    public int getHealth()
    {
        return health;
    }

    public void addHealth(int delta)
    {
        health = health + delta;
    }

    public boolean move(Direction direction)
    {
        int previousX = coordX;
        int previousY = coordY;

        if(isSlow > 0)
        {
            slowTurn = !slowTurn;
            if(slowTurn)
            {
                return false;
            }
        }

        if(isReverted > 0)
        {
            switch(direction)
            {
                case LEFT:
                    direction = Direction.RIGHT;
                break;
                case RIGHT:
                    direction = Direction.LEFT;
                break;
                case UP:
                    direction = Direction.DOWN;
                break;
                case DOWN:
                    direction = Direction.UP;
                break;
            }
        }

        boolean moved;

        if(isLight <= 0)
        {
            moved = super.move(direction);
        }
        else
        {
            moved = moveNoStone(direction);
        }

        if(moved)
        {
            ListIterator<HealthPack> healthPackIterator = SimpleGame.getHealthPackList().listIterator();
            while(healthPackIterator.hasNext())
            {
                HealthPack currentHealthPack = healthPackIterator.next();
                if(coordX == currentHealthPack.getCoordX() && coordY == currentHealthPack.getCoordY())
                {
                    health = health + currentHealthPack.getAmount();
                    healthPackIterator.remove();
                }
            }

            ListIterator<Pitfall> pitfallIterator = SimpleGame.getPitfallList().listIterator();
            while(pitfallIterator.hasNext())
            {
                Pitfall currentPitfall = pitfallIterator.next();
                if(coordX == currentPitfall.getCoordX() && coordY == currentPitfall.getCoordY())
                {
                    health = health - currentPitfall.getDamage();
                    pitfallIterator.remove();
                }
            }
        }

        return moved;
    }

    private boolean moveNoStone(Direction direction)
    {
        int desiredX = coordX;
        int desiredY = coordY;

        switch(direction)
        {
            case UP:
                desiredY = desiredY - 1;
            break;
            case DOWN:
                desiredY = desiredY + 1;
            break;
            case RIGHT:
                desiredX = desiredX + 1;
            break;
            case LEFT:
                desiredX = desiredX - 1;
            break;
            default:
            break;
        }

        if(desiredX < 0 || desiredY < 0)
        {
            return false;
        }

        if(desiredX >= SimpleGame.getGameboardX() || desiredY >= SimpleGame.getGameboardY())
        {
            return false;
        }

        for(int i = 0; i < SimpleGame.getGateList().size(); i++)
        {
            if(desiredX == SimpleGame.getGateList().get(i).getCoordX() && desiredY == SimpleGame.getGateList().get(i).getCoordY())
            {
                return false;
            }
        }

        if(SimpleGame.getTileByIndex(desiredX, desiredY).getType() == TileType.BLOCK)
        {
            return false;
        }

        lastMoveTeleported = false;

        if(lastMoveTeleported == false)
        {
            for(int i = 0; i < SimpleGame.getTeleportList().size(); i++)
            {
                if(desiredX == SimpleGame.getTeleportList().get(i).getCoordX() && desiredY == SimpleGame.getTeleportList().get(i).getCoordY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getTargetX();
                    desiredY = SimpleGame.getTeleportList().get(i).getTargetY();
                    lastMoveTeleported = true;
                }
                else if(desiredX == SimpleGame.getTeleportList().get(i).getTargetX() && desiredY == SimpleGame.getTeleportList().get(i).getTargetY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getCoordX();
                    desiredY = SimpleGame.getTeleportList().get(i).getCoordY();
                    lastMoveTeleported = true;
                }
            }
        }

        coordX = desiredX;
        coordY = desiredY;

        return true;
    }

    public void draw()
    {
        if(isReverted > 0)
        {
            SimpleGame.gc.drawImage(SimpleGame.revertedPlayerImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
        else
        {
            SimpleGame.gc.drawImage(SimpleGame.PlayerImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }

    public void enchantmentsTimeAdvance()
    {
        if(isSlow > 0)
        {
            isSlow = isSlow - 1;
        }

        if(isReverted > 0)
        {
            isReverted = isReverted - 1;
        }

        if(isBlind > 0)
        {
            isBlind = isBlind - 1;
        }

        if(isLight > 0)
        {
            isLight = isLight - 1;
        }
    }

    public void setXY(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public void setSlow(int ticks)
    {
        isSlow = ticks;
    }

    public void setReverted(int ticks)
    {
        isReverted = ticks;
    }

    public void setBlind(int ticks)
    {
        isBlind = ticks;
    }

    public void setLight(int ticks)
    {
        isLight = ticks;
    }
}

class Gate extends XYObject
{
    public Gate(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public void draw()
    {
        SimpleGame.gc.drawImage(SimpleGame.gateImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}

class OpeningKey extends Moveable
{
    public OpeningKey(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public void draw()
    {
        SimpleGame.gc.drawImage(SimpleGame.openingKeyImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }

    public boolean move(Direction direction)
    {
        int desiredX = coordX;
        int desiredY = coordY;

        switch(direction)
        {
            case UP:
                desiredY = desiredY - 1;
            break;
            case DOWN:
                desiredY = desiredY + 1;
            break;
            case RIGHT:
                desiredX = desiredX + 1;
            break;
            case LEFT:
                desiredX = desiredX - 1;
            break;
            default:
            break;
        }

        if(desiredX < 0 || desiredY < 0)
        {
            return false;
        }

        if(desiredX >= SimpleGame.getGameboardX() || desiredY >= SimpleGame.getGameboardY())
        {
            return false;
        }

        ListIterator<Gate> iterator = SimpleGame.getGateList().listIterator();
        while(iterator.hasNext())
        {
            Gate currentGate = iterator.next();
            if(desiredX == currentGate.getCoordX() && desiredY == currentGate.getCoordY())
            {
                iterator.remove();
                SimpleGame.getOpeningKeyList().remove(this);
                return true;
            }
        }

        if(SimpleGame.getTileByIndex(desiredX, desiredY).getType() == TileType.BLOCK)
        {
            return false;
        }

        lastMoveTeleported = false;

        if(lastMoveTeleported == false)
        {
            for(int i = 0; i < SimpleGame.getTeleportList().size(); i++)
            {
                if(desiredX == SimpleGame.getTeleportList().get(i).getCoordX() && desiredY == SimpleGame.getTeleportList().get(i).getCoordY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getTargetX();
                    desiredY = SimpleGame.getTeleportList().get(i).getTargetY();
                    lastMoveTeleported = true;
                }
                else if(desiredX == SimpleGame.getTeleportList().get(i).getTargetX() && desiredY == SimpleGame.getTeleportList().get(i).getTargetY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getCoordX();
                    desiredY = SimpleGame.getTeleportList().get(i).getCoordY();
                    lastMoveTeleported = true;
                }
            }
        }

        for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
        {
            if(desiredX == SimpleGame.getStoneList().get(i).getCoordX() && desiredY == SimpleGame.getStoneList().get(i).getCoordY())
            {
                if(!SimpleGame.getStoneList().get(i).move(direction))
                {
                    return false;
                }
            }
        }

        for(int i = 0; i < SimpleGame.getOpeningKeyList().size(); i++)
        {
            if(desiredX == SimpleGame.getOpeningKeyList().get(i).getCoordX() && desiredY == SimpleGame.getOpeningKeyList().get(i).getCoordY())
            {
                if(!SimpleGame.getOpeningKeyList().get(i).move(direction))
                {
                    return false;
                }
            }
        }

        coordX = desiredX;
        coordY = desiredY;

        return true;
    }
}

class HealthPack extends XYObject
{
    int amount;

    public int getAmount()
    {
        return amount;
    }

    public HealthPack(int x, int y, int amount)
    {
        coordX = x;
        coordY = y;
        this.amount = amount;
    }

    public void draw()
    {
        SimpleGame.gc.drawImage(SimpleGame.healthPackImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}

class Spike extends XYObject
{
    int damage;

    public Spike(int x, int y, int damage)
    {
        coordX = x;
        coordY = y;
        this.damage = damage;
    }

    public int getDamage()
    {
        return damage;
    }

    public void draw()
    {
        SimpleGame.gc.drawImage(SimpleGame.spikeImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}

class Pitfall extends XYObject
{
    int damage;

    public Pitfall(int x, int y, int damage)
    {
        coordX = x;
        coordY = y;
        this.damage = damage;
    }

    public int getDamage()
    {
        return damage;
    }

    public void draw()
    {
        SimpleGame.gc.drawImage(SimpleGame.pitfallImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}

class Catapult extends XYObject
{
    int power;
    Direction direction;

    public Catapult(int x, int y, int power, Direction direction)
    {
        coordX = x;
        coordY = y;
        this.power = power;
        this.direction = direction;
    }

    public int getPower()
    {
        return power;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void draw()
    {
        switch(direction)
        {
            case UP:
                SimpleGame.gc.drawImage(SimpleGame.catapultUpImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            break;
            case DOWN:
                SimpleGame.gc.drawImage(SimpleGame.catapultDownImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            break;
            case RIGHT:
                SimpleGame.gc.drawImage(SimpleGame.catapultRightImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            break;
            case LEFT:
                SimpleGame.gc.drawImage(SimpleGame.catapultLeftImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            break;
        }
    }
}

class Lever extends XYObject
{
    boolean isPowered = false;
    boolean wasPowered = false;
    Teleport teleport = null;
    boolean leftOrRight = true;

    public Lever(int x, int y, Teleport teleport)
    {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
    }

    public boolean isPowered()
    {
        return isPowered;
    }

    public boolean wasPowered()
    {
        return wasPowered;
    }

    public void setPowered(boolean power)
    {
        isPowered = power;
    }

    public void setWasPowered(boolean power)
    {
        wasPowered = power;
    }

    public void draw()
    {
        if(leftOrRight == true)
        {
            SimpleGame.gc.drawImage(SimpleGame.leverOnImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
        else
        {
            SimpleGame.gc.drawImage(SimpleGame.leverOffImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }

    public void turnOn()
    {
        isPowered = true;
    }

    public void turnOff()
    {
        isPowered = false;
    }

    public void toggle()
    {
        isPowered = !isPowered;
    }

    public void togglePortal()
    {
        leftOrRight = !leftOrRight;
        teleport.toggle();
    }
}

class PressurePlate extends XYObject
{
    boolean isPowered = false;
    boolean wasPowered = false;
    Teleport teleport = null;

    public PressurePlate(int x, int y, Teleport teleport)
    {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
    }

    public boolean isPowered()
    {
        return isPowered;
    }

    public boolean wasPowered()
    {
        return wasPowered;
    }

    public void setPowered(boolean power)
    {
        isPowered = power;
    }

    public void setWasPowered(boolean power)
    {
        wasPowered = power;
    }

    public void draw()
    {
        if(isPowered)
        {
            SimpleGame.gc.drawImage(SimpleGame.pressurePlateOnImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
        else
        {
            SimpleGame.gc.drawImage(SimpleGame.pressurePlateOffImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }

    public void togglePortal()
    {
        this.teleport.toggle();
    }

    public void turnOn()
    {
        isPowered = true;
    }

    public void turnOff()
    {
        isPowered = false;
    }
}


