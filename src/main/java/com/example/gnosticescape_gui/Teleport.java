package com.example.gnosticescape_gui;
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
