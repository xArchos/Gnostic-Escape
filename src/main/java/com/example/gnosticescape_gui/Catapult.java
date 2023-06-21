package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Catapult extends XYObject implements Serializable, Cloneable {
    private final transient int power;
    private final Direction direction;

    public Catapult(int x, int y, int power, Direction direction) {
        coordX = x;
        coordY = y;
        this.power = power;
        this.direction = direction;
    }

    @Override
    public Catapult clone() {
        Catapult c = new Catapult(coordX, coordY, power, direction);
        return c;
    }

    public int getPower() {
        return power;
    }

    public Direction getDirection() {
        return direction;
    }

    public void draw(GraphicsContext gc) {
        switch (direction) {
            case UP ->
                    gc.drawImage(ImagesWrapper.catapultUpImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            case DOWN ->
                    gc.drawImage(ImagesWrapper.catapultDownImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            case RIGHT ->
                    gc.drawImage(ImagesWrapper.catapultRightImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
            case LEFT ->
                    gc.drawImage(ImagesWrapper.catapultLeftImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
    }
}
