package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class HealthPack extends XYObject implements Serializable, Cloneable {
    private final transient int amount;

    public int getAmount() {
        return amount;
    }

    public HealthPack(int x, int y, int amount) {
        coordX = x;
        coordY = y;
        this.amount = amount;
    }

    @Override
    public HealthPack clone() {
        HealthPack hp = new HealthPack(coordX, coordY, amount);
        return hp;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(ImagesWrapper.healthPackImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }
}
