package com.example.gnosticescape_gui;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

public class Teleport extends XYObject implements Serializable, Cloneable
{
    private int targetX;
    private int targetY;
    private boolean active;

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

    @Override
    public Teleport clone()
    {
        Teleport t = new Teleport(coordX, coordY, targetX, targetY, active);
        return t;
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

    public void draw(GraphicsContext gc)
    {
        if(active == true)
        {
            gc.drawImage(ImagesWrapper.bluePortalImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            gc.drawImage(ImagesWrapper.orangePortalImage, targetX * ImagesWrapper.tileX, targetY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
        else
        {
            gc.drawImage(ImagesWrapper.blueOffPortalImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            gc.drawImage(ImagesWrapper.orangeOffPortalImage, targetX * ImagesWrapper.tileX, targetY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
    }
}
