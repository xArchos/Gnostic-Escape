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

public class Pitfall extends XYObject implements Serializable, Cloneable
{
    private transient int damage;

    public Pitfall(int x, int y, int damage)
    {
        coordX = x;
        coordY = y;
        this.damage = damage;
    }

    @Override
    public Pitfall clone()
    {
        Pitfall p = new Pitfall(coordX, coordY, damage);
        return p;
    }

    public int getDamage()
    {
        return damage;
    }

    public void draw(GraphicsContext gc)
    {
        gc.drawImage(ImagesWrapper.pitfallImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }
}