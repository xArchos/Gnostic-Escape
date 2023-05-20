import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SimpleGame extends Application
{
    private static final int GAMEBOARD_Y = 24;
    private static final int GAMEBOARD_X = 24;
    private static final int TILE_Y = 30;
    private static final int TILE_X = 30;
    public static Tile[][] Tiles = new Tile[GAMEBOARD_X][GAMEBOARD_Y];
    private static int winX = GAMEBOARD_X / 2;
    private static int winY = GAMEBOARD_Y / 2;

    static List<Player> playerList = new ArrayList<Player>();
    static List<Stone> stoneList = new ArrayList<Stone>();
    static List<Teleport> teleportList = new ArrayList<Teleport>();
    static List<Prize> prizeList = new ArrayList<Prize>();
    static List<Gate> gateList = new ArrayList<Gate>();

    public static void main(String[] args)
    {
        Player p1 = new Player(0, 0);
        playerList.add(p1);

        Stone s1 = new Stone(1, 1);
        Stone s2 = new Stone(1, 2);
        Stone s3 = new Stone(18, 18, true);
        stoneList.add(s1);
        stoneList.add(s2);
        stoneList.add(s3);

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

    public static Tile getTileByIndex(int x, int y)
    {
        return Tiles[x][y];
    }

    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Hello, World!");
        primaryStage.show();

        Canvas canvas = new Canvas(GAMEBOARD_X * TILE_X, GAMEBOARD_Y * TILE_Y);
		GraphicsContext gc = canvas.getGraphicsContext2D();

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
                // Clear the canvas
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, GAMEBOARD_Y * TILE_Y, GAMEBOARD_X * TILE_X);

                FileInputStream ImgInputStream;
                Image PlayerImage = null;
                Image emptyTileImage = null;
                Image fullTileImage = null;
                Image bluePortalImage = null;
                Image orangePortalImage = null;
                Image blueOffPortalImage = null;
                Image orangeOffPortalImage = null;
                Image stoneImage = null;
                Image magicStoneImage = null;
                Image prizeImage = null;
                Image gateImage = null;

                try
                {
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
                }
                catch(FileNotFoundException fnfe)
                {
                    System.out.println("Brakuje plików.");
                    System.exit(1);
                }

                for(int i = 0; i < GAMEBOARD_X; i++)
                {
                    for(int j = 0; j < GAMEBOARD_Y; j++)
                    {
                        /*gc.setFill(Color.WHITE);
                        if(!Tiles[i][j].isEmpty())
                        {
                            gc.setFill(Color.BLACK);
                        }

                        gc.fillRect(i * TILE_X, j * TILE_Y, TILE_X, TILE_Y);*/

                        if(!Tiles[i][j].isEmpty())
                        {
                            gc.drawImage(fullTileImage, i * TILE_X, j * TILE_Y, TILE_X, TILE_Y);
                        }
                        else
                        {
                            gc.drawImage(emptyTileImage, i * TILE_X, j * TILE_Y, TILE_X, TILE_Y);
                        }
                    }
                }

                for(int i = 0; i < teleportList.size(); i++)
                {
                    if(teleportList.get(i).isActive() == true)
                    {
                        /*gc.setFill(Color.BLUE);
                        gc.fillRect(teleportList.get(i).getCoordX() * TILE_X, teleportList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);

                        gc.setFill(Color.ORANGE);
                        gc.fillRect(teleportList.get(i).getTargetX() * TILE_X, teleportList.get(i).getTargetY() * TILE_Y, TILE_X, TILE_Y);*/
                        gc.drawImage(bluePortalImage, teleportList.get(i).getCoordX() * TILE_X, teleportList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);
                        gc.drawImage(orangePortalImage, teleportList.get(i).getTargetX() * TILE_X, teleportList.get(i).getTargetY() * TILE_Y, TILE_X, TILE_Y);
                    }
                    else
                    {
                        /*gc.setFill(Color.rgb(61,61,255));
                        gc.fillRect(teleportList.get(i).getCoordX() * TILE_X, teleportList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);

                        gc.setFill(Color.rgb(220,88,42));
                        gc.fillRect(teleportList.get(i).getTargetX() * TILE_X, teleportList.get(i).getTargetY() * TILE_Y, TILE_X, TILE_Y);*/
                        gc.drawImage(blueOffPortalImage, teleportList.get(i).getCoordX() * TILE_X, teleportList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);
                        gc.drawImage(orangeOffPortalImage, teleportList.get(i).getTargetX() * TILE_X, teleportList.get(i).getTargetY() * TILE_Y, TILE_X, TILE_Y);
                    }
                }

                for(int i = 0; i < playerList.size(); i++)
                {
                    /*gc.setFill(Color.RED);
                    gc.fillRect(playerList.get(i).getCoordX() * TILE_X, playerList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);*/
                    gc.drawImage(PlayerImage, playerList.get(i).getCoordX() * TILE_X, playerList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);
                }

                for(int i = 0; i < stoneList.size(); i++)
                {
                    /*gc.setFill(Color.GRAY);
                    gc.fillRect(stoneList.get(i).getCoordX() * TILE_X, stoneList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);*/
                    if(stoneList.get(i).isMagic())
                    {
                        gc.drawImage(magicStoneImage, stoneList.get(i).getCoordX() * TILE_X, stoneList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);
                    }
                    else
                    {
                        gc.drawImage(stoneImage, stoneList.get(i).getCoordX() * TILE_X, stoneList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);
                    }
                }

                for(int i = 0; i < prizeList.size(); i++)
                {
                    /*gc.setFill(Color.GREEN);
                    gc.fillRect(prizeList.get(i).getCoordX() * TILE_X, prizeList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);*/
                    gc.drawImage(prizeImage, prizeList.get(i).getCoordX() * TILE_X, prizeList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);
                }

                for(int i = 0; i < gateList.size(); i++)
                {
                    gc.drawImage(gateImage, gateList.get(i).getCoordX() * TILE_X, gateList.get(i).getCoordY() * TILE_Y, TILE_X, TILE_Y);
                }
            }
        }.start();
    }

    private void run(GraphicsContext gc) //TYLKO 1 GRACZ
    {
        if(playerList.size() > 0)
        {
            for(int i = 0; i < prizeList.size(); i++)
            {
                if(playerList.get(0).getCoordX() == prizeList.get(i).getCoordX() && playerList.get(0).getCoordY() == prizeList.get(i).getCoordY())
                {
                    System.exit(0);
                }
            }
        }
    }

    static void togglePortals()
    {
        for(int i = 0; i < teleportList.size(); i++)
        {
            teleportList.get(i).toggle();
        }
    }
}

enum Direction
{
    UP, DOWN, LEFT, RIGHT, EMPTY;
}

class Teleport
{
    protected int coordX;
    protected int coordY;
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

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
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
}

abstract class Moveable
{
    protected boolean lastMoveTeleported;
    protected int coordX;
    protected int coordY;

    abstract public boolean move(Direction direction);
}

class Stone extends Moveable
{
    boolean isMagic;

    public Stone(int x, int y)
    {
        coordX = x;
        coordY = y;
        lastMoveTeleported = false;
        isMagic = false;
    }

    public Stone(int x, int y, boolean magic)
    {
        coordX = x;
        coordY = y;
        lastMoveTeleported = false;
        isMagic = magic;
    }

    public boolean move(Direction direction)
    {
        switch(direction)
        {
            case DOWN:
                if(coordY >= SimpleGame.getGameboardY() - 1)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX == SimpleGame.getStoneList().get(i).getCoordX() && coordY + 1 == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX == SimpleGame.getGateList().get(i).getCoordX() && coordY + 1 == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        if(!isMagic)
                        {
                            return false;
                        }
                        else
                        {
                            SimpleGame.getStoneList().remove(this);
                            SimpleGame.getGateList().remove(i);
                            return true;
                        }
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX, coordY + 1).isEmpty())
                {
                    return false;
                }

                coordY = coordY + 1;
            break;
            case UP:
                if(coordY <= 0)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX == SimpleGame.getStoneList().get(i).getCoordX() && coordY - 1 == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX == SimpleGame.getGateList().get(i).getCoordX() && coordY - 1 == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        if(!isMagic)
                        {
                            return false;
                        }
                        else
                        {
                            SimpleGame.getStoneList().remove(this);
                            SimpleGame.getGateList().remove(i);
                            return true;
                        }
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX, coordY - 1).isEmpty())
                {
                    return false;
                }

                coordY = coordY - 1;
            break;
            case LEFT:
                if(coordX <= 0)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX - 1 == SimpleGame.getStoneList().get(i).getCoordX() && coordY == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX - 1 == SimpleGame.getGateList().get(i).getCoordX() && coordY == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        if(!isMagic)
                        {
                            return false;
                        }
                        else
                        {
                            SimpleGame.getStoneList().remove(this);
                            SimpleGame.getGateList().remove(i);
                            return true;
                        }
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX - 1, coordY).isEmpty())
                {
                    return false;
                }

                coordX = coordX - 1;
            break;
            case RIGHT:
                if(coordX >= SimpleGame.getGameboardX() - 1)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX + 1 == SimpleGame.getStoneList().get(i).getCoordX() && coordY == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX + 1 == SimpleGame.getGateList().get(i).getCoordX() && coordY == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        if(!isMagic)
                        {
                            return false;
                        }
                        else
                        {
                            SimpleGame.getStoneList().remove(this);
                            SimpleGame.getGateList().remove(i);
                            return true;
                        }
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX + 1, coordY).isEmpty())
                {
                    return false;
                }

                coordX = coordX + 1;
            break;
            default:
            break;
        }

        lastMoveTeleported = false;

        if(lastMoveTeleported == false)
        {
            for(int i = 0; i < SimpleGame.getTeleportList().size(); i++)
            {
                if(coordX == SimpleGame.getTeleportList().get(i).getCoordX() && coordY == SimpleGame.getTeleportList().get(i).getCoordY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    coordX = SimpleGame.getTeleportList().get(i).getTargetX();
                    coordY = SimpleGame.getTeleportList().get(i).getTargetY();
                    lastMoveTeleported = true;
                }
                else if(coordX == SimpleGame.getTeleportList().get(i).getTargetX() && coordY == SimpleGame.getTeleportList().get(i).getTargetY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    coordX = SimpleGame.getTeleportList().get(i).getCoordX();
                    coordY = SimpleGame.getTeleportList().get(i).getCoordY();
                    lastMoveTeleported = true;
                }
            }
        }

        return true;
    }

    public boolean isMagic()
    {
        return isMagic;
    }

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }
}

class Tile
{
    private boolean empty;

    public Tile()
    {
        empty = true;
    }

    public void setEmpty()
    {
        empty = true;
    }

    public void setBlock()
    {
        empty = false;
    }

    public boolean isEmpty()
    {
        return empty;
    }
}

class Prize
{
    protected int coordX;
    protected int coordY;

    public Prize(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }
}

class Player extends Moveable
{
    protected int health;

    public Player(int x, int y)
    {
        coordX = x;
        coordY = y;
        health = 1000;
        lastMoveTeleported = false;
    }

    @Override
    public boolean move(Direction direction)
    {
        switch(direction)
        {
            case DOWN:
                if(coordY >= SimpleGame.getGameboardY() - 1)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX == SimpleGame.getStoneList().get(i).getCoordX() && coordY + 1 == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX == SimpleGame.getGateList().get(i).getCoordX() && coordY + 1 == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        return false;
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX, coordY + 1).isEmpty())
                {
                    return false;
                }

                coordY = coordY + 1;
            break;
            case UP:
                if(coordY <= 0)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX == SimpleGame.getStoneList().get(i).getCoordX() && coordY - 1 == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX == SimpleGame.getGateList().get(i).getCoordX() && coordY - 1 == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        return false;
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX, coordY - 1).isEmpty())
                {
                    return false;
                }

                coordY = coordY - 1;
            break;
            case LEFT:
                if(coordX <= 0)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX - 1 == SimpleGame.getStoneList().get(i).getCoordX() && coordY == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX - 1 == SimpleGame.getGateList().get(i).getCoordX() && coordY == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        return false;
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX - 1, coordY).isEmpty())
                {
                    return false;
                }

                coordX = coordX - 1;
            break;
            case RIGHT:
                if(coordX >= SimpleGame.getGameboardX() - 1)
                {
                    return false;
                }

                for(int i = 0; i < SimpleGame.getStoneList().size(); i++)
                {
                    if(coordX + 1 == SimpleGame.getStoneList().get(i).getCoordX() && coordY == SimpleGame.getStoneList().get(i).getCoordY())
                    {
                        if(!SimpleGame.getStoneList().get(i).move(direction))
                        {
                            return false;
                        }
                    }
                }

                for(int i = 0; i < SimpleGame.getGateList().size(); i++)
                {
                    if(coordX + 1 == SimpleGame.getGateList().get(i).getCoordX() && coordY == SimpleGame.getGateList().get(i).getCoordY())
                    {
                        return false;
                    }
                }

                if(!SimpleGame.getTileByIndex(coordX + 1, coordY).isEmpty())
                {
                    return false;
                }

                coordX = coordX + 1;
            break;
            default:
            break;
        }

        lastMoveTeleported = false;

        if(lastMoveTeleported == false)
        {
            for(int i = 0; i < SimpleGame.getTeleportList().size(); i++)
            {
                if(coordX == SimpleGame.getTeleportList().get(i).getCoordX() && coordY == SimpleGame.getTeleportList().get(i).getCoordY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    coordX = SimpleGame.getTeleportList().get(i).getTargetX();
                    coordY = SimpleGame.getTeleportList().get(i).getTargetY();
                    lastMoveTeleported = true;
                }
                else if(coordX == SimpleGame.getTeleportList().get(i).getTargetX() && coordY == SimpleGame.getTeleportList().get(i).getTargetY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    coordX = SimpleGame.getTeleportList().get(i).getCoordX();
                    coordY = SimpleGame.getTeleportList().get(i).getCoordY();
                    lastMoveTeleported = true;
                }
            }
        }

        return true;
    }

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }

    public int getHealth()
    {
        return health;
    }

    public void addHealth(int delta)
    {
        health = health + delta;
    }
}

class Gate
{
    private int coordX;
    private int coordY;

    public Gate(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }
}
