package com.example.gnosticescape_gui;

import java.io.Serializable;

public class GameMessage implements Serializable
{
    private GameRequest gameRequest;
    private WorldState worldState = null;

    public GameMessage(GameRequest gameRequest)
    {
        this.gameRequest = gameRequest;
    }

    public GameMessage(GameRequest gameRequest, WorldState worldState)
    {
        this.gameRequest = gameRequest;
        this.worldState = worldState;
    }

    public GameRequest getGameRequest()
    {
        return gameRequest;
    }

    public WorldState getWorldState()
    {
        return worldState;
    }
}
