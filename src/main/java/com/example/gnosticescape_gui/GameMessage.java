package com.example.gnosticescape_gui;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.util.Duration;

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
