package com.example.gnosticescape_gui;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

public class Tile implements Serializable
{
    private TileType tileType;
    private static final int GROWING_DAMAGE = 9;

    public Tile()
    {
        tileType = TileType.EMPTY;
    }

    public Tile(TileType type)
    {
        tileType = type;
    }

    public void setEmpty()
    {
        tileType = TileType.EMPTY;
    }

    public void setBlock()
    {
        tileType = TileType.BLOCK;
    }

    public void setGrowing()
    {
        tileType = TileType.GROWING;
    }

    public void setBeforeGrowing()
    {
        tileType = TileType.BEFORE_GROWING;
    }

    public TileType getType()
    {
        return tileType;
    }

    public void draw(GraphicsContext gc, int x, int y)
    {
        if(ImagesWrapper.isDayMode)
        {
            switch(tileType)
            {
                case EMPTY:
                    gc.drawImage(ImagesWrapper.emptyTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    break;
                case BLOCK:
                    gc.drawImage(ImagesWrapper.fullTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    break;
                case GROWING:
                case BEFORE_GROWING:
                    gc.drawImage(ImagesWrapper.emptyTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    gc.drawImage(ImagesWrapper.growingImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    break;
            }
        }
        else
        {
            switch(tileType)
            {
                case EMPTY:
                    gc.drawImage(ImagesWrapper.darkEmptyTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    break;
                case BLOCK:
                    gc.drawImage(ImagesWrapper.darkBlockTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    break;
                case GROWING:
                case BEFORE_GROWING:
                    gc.drawImage(ImagesWrapper.darkEmptyTileImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    gc.drawImage(ImagesWrapper.darkGrowingImage, x * ImagesWrapper.tileX, y * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
                    break;
            }
        }
    }

    public static int getGrowingDamage()
    {
        return GROWING_DAMAGE;
    }
}
