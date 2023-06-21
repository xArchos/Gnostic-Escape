package com.example.gnosticescape_gui;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

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
