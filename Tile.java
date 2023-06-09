import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
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

public class Tile implements Serializable
{
    private TileType tileType;
    private static final int GROWING_DAMAGE = 6;

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

    public void draw(GraphicsContext gc, int x, int y)
    {
        switch(tileType)
        {
            case EMPTY:
                gc.drawImage(ImagesWrapper.emptyTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            break;
            case BLOCK:
                gc.drawImage(ImagesWrapper.fullTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            break;
            case GROWING:
            case BEFORE_GROWING:
                gc.drawImage(ImagesWrapper.growingImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            break;
        }
    }

    public static int getGrowingDamage()
    {
        return GROWING_DAMAGE;
    }
}
