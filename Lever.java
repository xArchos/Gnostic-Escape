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

public class Lever extends XYObject implements Serializable, Cloneable
{
    private transient boolean isPowered = false;
    private transient boolean wasPowered = false;
    private transient Teleport teleport = null;
    private boolean leftOrRight = true;

    public Lever(int x, int y, Teleport teleport)
    {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
    }

    private Lever(int x, int y, Teleport teleport, boolean leftOrRight)
    {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
        this.leftOrRight = leftOrRight;
    }

    @Override
    public Lever clone()
    {
        Lever l = new Lever(coordX, coordY, teleport, leftOrRight);
        l.isPowered = isPowered;
        l.wasPowered = wasPowered;
        return l;
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

    public void draw(GraphicsContext gc)
    {
        if(leftOrRight == true)
        {
            gc.drawImage(ImagesWrapper.leverOnImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
        else
        {
            gc.drawImage(ImagesWrapper.leverOffImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
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
