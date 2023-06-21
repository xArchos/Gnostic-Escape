package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Prize extends XYObject implements Serializable, Cloneable {
    public Prize(int x, int y) {
        coordX = x;
        coordY = y;
    }

    @Override
    public Prize clone() {
        Prize p = new Prize(coordX, coordY);
        return p;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(ImagesWrapper.prizeImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }
}
