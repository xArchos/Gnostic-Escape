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

public class PressurePlate extends XYObject implements Serializable, Cloneable
{
    private boolean isPowered = false;
    private transient boolean wasPowered = false;
    private transient Teleport teleport = null;

    public PressurePlate(int x, int y, Teleport teleport)
    {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
    }

    @Override
    public PressurePlate clone()
    {
        PressurePlate p = new PressurePlate(coordX, coordY, teleport);
        p.isPowered = isPowered;
        p.wasPowered = wasPowered;
        return p;
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
        if(isPowered)
        {
            gc.drawImage(ImagesWrapper.pressurePlateOnImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
        else
        {
            gc.drawImage(ImagesWrapper.pressurePlateOffImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
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
