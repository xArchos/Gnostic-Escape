package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Spike extends XYObject implements Serializable, Cloneable {
    private final transient int damage;

    public Spike(int x, int y, int damage) {
        coordX = x;
        coordY = y;
        this.damage = damage;
    }

    @Override
    public Spike clone() {
        Spike s = new Spike(coordX, coordY, damage);
        return s;
    }

    public int getDamage() {
        return damage;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(ImagesWrapper.spikeImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }
}
