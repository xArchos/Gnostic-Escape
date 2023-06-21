package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Pitfall extends XYObject implements Serializable, Cloneable {
    private final transient int damage;

    public Pitfall(int x, int y, int damage) {
        coordX = x;
        coordY = y;
        this.damage = damage;
    }

    @Override
    public Pitfall clone() {
        Pitfall p = new Pitfall(coordX, coordY, damage);
        return p;
    }

    public int getDamage() {
        return damage;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(ImagesWrapper.pitfallImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }
}
