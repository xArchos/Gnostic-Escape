package com.example.gnosticescape_gui;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Stone extends Moveable implements Serializable, Cloneable {
    public Stone(int x, int y) {
        coordX = x;
        coordY = y;
        lastMoveTeleported = false;
    }

    @Override
    public Stone clone() {
        Stone s = new Stone(coordX, coordY);
        return s;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(ImagesWrapper.stoneImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
    }
}
