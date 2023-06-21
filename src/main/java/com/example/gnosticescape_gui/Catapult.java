package com.example.gnosticescape_gui;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

public class Catapult extends XYObject implements Serializable, Cloneable
{
    private transient int power;
    private Direction direction;

    public Catapult(int x, int y, int power, Direction direction)
    {
        coordX = x;
        coordY = y;
        this.power = power;
        this.direction = direction;
    }

    @Override
    public Catapult clone()
    {
        Catapult c = new Catapult(coordX, coordY, power, direction);
        return c;
    }

    public int getPower()
    {
        return power;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void draw(GraphicsContext gc)
    {
        switch(direction)
        {
            case UP:
                gc.drawImage(ImagesWrapper.catapultUpImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            break;
            case DOWN:
                gc.drawImage(ImagesWrapper.catapultDownImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            break;
            case RIGHT:
                gc.drawImage(ImagesWrapper.catapultRightImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            break;
            case LEFT:
                gc.drawImage(ImagesWrapper.catapultLeftImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            break;
        }
    }
}
