package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class PressurePlate extends XYObject implements Serializable, Cloneable {
    private boolean isPowered = false;
    private transient boolean wasPowered = false;
    private transient Teleport teleport = null;

    public PressurePlate(int x, int y, Teleport teleport) {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
    }

    @Override
    public PressurePlate clone() {
        PressurePlate p = new PressurePlate(coordX, coordY, teleport);
        p.isPowered = isPowered;
        p.wasPowered = wasPowered;
        return p;
    }

    public boolean isPowered() {
        return isPowered;
    }

    public boolean wasPowered() {
        return wasPowered;
    }

    public void setPowered(boolean power) {
        isPowered = power;
    }

    public void setWasPowered(boolean power) {
        wasPowered = power;
    }

    public void draw(GraphicsContext gc) {
        if (isPowered) {
            gc.drawImage(ImagesWrapper.pressurePlateOnImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        } else {
            gc.drawImage(ImagesWrapper.pressurePlateOffImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
    }

    public void togglePortal() {
        this.teleport.toggle();
    }

    public void turnOn() {
        isPowered = true;
    }

    public void turnOff() {
        isPowered = false;
    }
}
