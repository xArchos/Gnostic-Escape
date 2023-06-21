package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Gate extends XYObject implements Serializable, Cloneable {
    public Gate(int x, int y) {
        coordX = x;
        coordY = y;
    }

    @Override
    public Gate clone() {
        Gate g = new Gate(coordX, coordY);
        return g;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(ImagesWrapper.gateImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }
}
