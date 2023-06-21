package com.example.gnosticescape_gui;

import java.io.Serializable;

public abstract class XYObject implements Serializable
{
    protected int coordX;
    protected int coordY;

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }
}
