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
