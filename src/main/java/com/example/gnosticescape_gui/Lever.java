package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Lever extends XYObject implements Serializable, Cloneable {
    private transient boolean isPowered = false;
    private transient boolean wasPowered = false;
    private transient Teleport teleport = null;
    private boolean leftOrRight = true;

    public Lever(int x, int y, Teleport teleport) {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
    }

    private Lever(int x, int y, Teleport teleport, boolean leftOrRight) {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
        this.leftOrRight = leftOrRight;
    }

    @Override
    public Lever clone() {
        Lever l = new Lever(coordX, coordY, teleport, leftOrRight);
        l.isPowered = isPowered;
        l.wasPowered = wasPowered;
        return l;
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
        if (leftOrRight) {
            gc.drawImage(ImagesWrapper.leverOnImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        } else {
            gc.drawImage(ImagesWrapper.leverOffImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
    }

    public void turnOn() {
        isPowered = true;
    }

    public void turnOff() {
        isPowered = false;
    }

    public void toggle() {
        isPowered = !isPowered;
    }

    public void togglePortal() {
        leftOrRight = !leftOrRight;
        teleport.toggle();
    }
}
