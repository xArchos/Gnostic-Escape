import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

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

public class OpeningKey extends Moveable implements Serializable, Cloneable
{
    public OpeningKey(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public void draw(GraphicsContext gc)
    {
        gc.drawImage(ImagesWrapper.openingKeyImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }

    @Override
    public OpeningKey clone()
    {
        OpeningKey op = new OpeningKey(coordX, coordY);
        return op;
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
