package com.example.gnosticescape_gui;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ClassNotFoundException;
import java.lang.InterruptedException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.Socket;
import java.net.SocketException;
import java.util.ListIterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Player extends Moveable implements Serializable, Cloneable
{
    public transient static final int START_HEALTH = 1000;
    private transient static int playersCount = 0;

    private int health;

    private boolean isDead = false;
    private boolean isWin = false;
    private int id;
    private transient Socket socket = null;
    private transient final Runnable runMethod;
    private transient Thread thread = null;

    private transient boolean slowTurn = false;
    private int isSlow = 0;
    private int isReverted = 0;
    private int isBlind = 0;
    private int isLight = 0;

    public Player(int x, int y, Socket socket)
    {
        coordX = x;
        coordY = y;
        this.socket = socket;
        health = START_HEALTH;
        lastMoveTeleported = false;
        id = playersCount;
        playersCount++;

        this.runMethod = new Runnable()
        {
            public void run()
            {
                runPlayer();
            }
        };
    }

    public Player(int x, int y)
    {
        coordX = x;
        coordY = y;
        health = START_HEALTH;
        lastMoveTeleported = false;
        runMethod = null;
        id = playersCount;
        playersCount++;
    }

    public Player()
    {
        socket = null;
        runMethod = null;
        thread = null;
    }

    @Override
    public Player clone()
    {
        Player p = new Player();
        p.coordX = coordX;
        p.coordY = coordY;
        p.health = health;
        p.isDead = isDead;
        p.isWin = isWin;
        p.id = id;

        p.slowTurn = slowTurn;
        p.isSlow = isSlow;
        p.isReverted = isReverted;
        p.isBlind = isBlind;
        p.isLight = isLight;
        return p;
    }

    public int getHealth()
    {
        return health;
    }

    public void addHealth(int delta)
    {
        if(!isDead && !isWin)
        {
            health = health + delta;
        }

        if(health < 0 && !isWin && !isDead)
        {
            isDead = true;
            SimpleGame.deadPlayersCount++;
        }
    }

    public boolean move(Direction direction)
    {
        if(isWin || isDead)
        {
            return false;
        }

        int previousX = coordX;
        int previousY = coordY;

        if(isSlow > 0)
        {
            slowTurn = !slowTurn;
            if(slowTurn)
            {
                return false;
            }
        }

        if(isReverted > 0)
        {
            switch(direction)
            {
                case LEFT:
                    direction = Direction.RIGHT;
                break;
                case RIGHT:
                    direction = Direction.LEFT;
                break;
                case UP:
                    direction = Direction.DOWN;
                break;
                case DOWN:
                    direction = Direction.UP;
                break;
            }
        }

        boolean moved;

        if(isLight <= 0)
        {
            moved = super.move(direction);
        }
        else
        {
            moved = moveNoStone(direction);
        }

        if(moved)
        {
            ListIterator<HealthPack> healthPackIterator = SimpleGame.getHealthPackList().listIterator();
            while(healthPackIterator.hasNext())
            {
                HealthPack currentHealthPack = healthPackIterator.next();
                if(coordX == currentHealthPack.getCoordX() && coordY == currentHealthPack.getCoordY())
                {
                    health = health + currentHealthPack.getAmount();
                    healthPackIterator.remove();
                }
            }

            ListIterator<Pitfall> pitfallIterator = SimpleGame.getPitfallList().listIterator();
            while(pitfallIterator.hasNext())
            {
                Pitfall currentPitfall = pitfallIterator.next();
                if(coordX == currentPitfall.getCoordX() && coordY == currentPitfall.getCoordY())
                {
                    health = health - currentPitfall.getDamage();
                    pitfallIterator.remove();
                }
            }
        }

        return moved;
    }

    private boolean moveNoStone(Direction direction)
    {
        int desiredX = coordX;
        int desiredY = coordY;

        switch(direction)
        {
            case UP:
                desiredY = desiredY - 1;
            break;
            case DOWN:
                desiredY = desiredY + 1;
            break;
            case RIGHT:
                desiredX = desiredX + 1;
            break;
            case LEFT:
                desiredX = desiredX - 1;
            break;
            default:
            break;
        }

        if(desiredX < 0 || desiredY < 0)
        {
            return false;
        }

        if(desiredX >= SimpleGame.getGameboardX() || desiredY >= SimpleGame.getGameboardY())
        {
            return false;
        }

        for(int i = 0; i < SimpleGame.getGateList().size(); i++)
        {
            if(desiredX == SimpleGame.getGateList().get(i).getCoordX() && desiredY == SimpleGame.getGateList().get(i).getCoordY())
            {
                return false;
            }
        }

        if(SimpleGame.getTileByIndex(desiredX, desiredY).getType() == TileType.BLOCK)
        {
            return false;
        }

        lastMoveTeleported = false;

        if(lastMoveTeleported == false)
        {
            for(int i = 0; i < SimpleGame.getTeleportList().size(); i++)
            {
                if(desiredX == SimpleGame.getTeleportList().get(i).getCoordX() && desiredY == SimpleGame.getTeleportList().get(i).getCoordY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getTargetX();
                    desiredY = SimpleGame.getTeleportList().get(i).getTargetY();
                    lastMoveTeleported = true;
                }
                else if(desiredX == SimpleGame.getTeleportList().get(i).getTargetX() && desiredY == SimpleGame.getTeleportList().get(i).getTargetY() && SimpleGame.getTeleportList().get(i).isActive())
                {
                    desiredX = SimpleGame.getTeleportList().get(i).getCoordX();
                    desiredY = SimpleGame.getTeleportList().get(i).getCoordY();
                    lastMoveTeleported = true;
                }
            }
        }

        coordX = desiredX;
        coordY = desiredY;

        return true;
    }

    public void draw(GraphicsContext gc)
    {
        if(isDead)
        {
            gc.drawImage(ImagesWrapper.deadPlayerImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
        else if(isWin)
        {
            gc.drawImage(ImagesWrapper.winPlayerImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
        else if(isReverted > 0)
        {
            gc.drawImage(ImagesWrapper.revertedPlayerImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }
        else
        {
            gc.drawImage(ImagesWrapper.PlayerImage, coordX * ImagesWrapper.tileX, coordY * ImagesWrapper.tileY, ImagesWrapper.tileX, ImagesWrapper.tileY);
        }

        gc.setFill(Color.BLUE);
        gc.setFont(new Font("Digital-7", 20));
        gc.fillText(Integer.toString(id), coordX * ImagesWrapper.tileX + ImagesWrapper.tileX / 2 - 5, coordY * ImagesWrapper.tileY - 7);
    }

    public void enchantmentsTimeAdvance()
    {
        if(isSlow > 0)
        {
            isSlow = isSlow - 1;
        }

        if(isReverted > 0)
        {
            isReverted = isReverted - 1;
        }

        if(isBlind > 0)
        {
            isBlind = isBlind - 1;
        }

        if(isLight > 0)
        {
            isLight = isLight - 1;
        }
    }

    public void setXY(int x, int y)
    {
        coordX = x;
        coordY = y;
    }

    public void setSlow(int ticks)
    {
        isSlow = ticks;
    }

    public void setReverted(int ticks)
    {
        isReverted = ticks;
    }

    public void setBlind(int ticks)
    {
        isBlind = ticks;
    }

    public void setLight(int ticks)
    {
        isLight = ticks;
    }

    public void setWin()
    {
        isWin = true;
    }

    public boolean isWin()
    {
        return isWin;
    }

    public void runPlayer()
    {
        try
        {
            while(SimpleGame.getServerStatus())
            {
                ObjectInputStream socketInputStream = new ObjectInputStream(this.socket.getInputStream());
                GameMessage gameMessage = (GameMessage) socketInputStream.readObject();

                WorldState worldState = null;
                ObjectOutputStream socketOutputStream = null;
                GameMessage responseMessage = null;

                switch(gameMessage.getGameRequest())
                {
                    case WORLD:
                        worldState = new WorldState(this);
                        responseMessage = new GameMessage(GameRequest.OK, worldState);
                        socketOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
                        socketOutputStream.writeObject(responseMessage);
                        System.out.println("Wysyłam świat!");
                    break;
                    case MOVE_UP:
                        SimpleGame.lockMutex();
                        this.move(Direction.UP);
                        SimpleGame.unlockMutex();
                        worldState = new WorldState(this);
                        responseMessage = new GameMessage(GameRequest.OK, worldState);
                        socketOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
                        socketOutputStream.writeObject(responseMessage);
                        System.out.println("Góra!");
                    break;
                    case MOVE_DOWN:
                        SimpleGame.lockMutex();
                        this.move(Direction.DOWN);
                        SimpleGame.unlockMutex();
                        worldState = new WorldState(this);
                        responseMessage = new GameMessage(GameRequest.OK, worldState);
                        socketOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
                        socketOutputStream.writeObject(responseMessage);
                        System.out.println("Dół!");
                    break;
                    case MOVE_LEFT:
                        SimpleGame.lockMutex();
                        this.move(Direction.LEFT);
                        SimpleGame.unlockMutex();
                        worldState = new WorldState(this);
                        responseMessage = new GameMessage(GameRequest.OK, worldState);
                        socketOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
                        socketOutputStream.writeObject(responseMessage);
                        System.out.println("Lewo!");
                    break;
                    case MOVE_RIGHT:
                        SimpleGame.lockMutex();
                        this.move(Direction.RIGHT);
                        SimpleGame.unlockMutex();
                        worldState = new WorldState(this);
                        responseMessage = new GameMessage(GameRequest.OK, worldState);
                        socketOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
                        socketOutputStream.writeObject(responseMessage);
                        System.out.println("Prawo!");
                    break;
                    case LEAVE:
                        removePlayer();
                        System.out.println("Wyjście!");
                    return;
                }

                Thread.sleep(100);
            }
        }
        catch(SocketException se)
        {
            removePlayer();
            se.printStackTrace();
        }
        catch(EOFException eofe)
        {
            removePlayer();
        }
        catch(IOException ioe)
        {
            removePlayer();
            ioe.printStackTrace();
        }
        catch(ClassNotFoundException cnfe)
        {
            removePlayer();
            cnfe.printStackTrace();
        }
        catch(InterruptedException ie)
        {
            removePlayer();
            ie.printStackTrace();
        }
    }

    public void removePlayer()
    {
        try
        {
            socket.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        SimpleGame.getPlayerList().remove(this);
    }

    public void sendOKMessage()
    {
        try
        {
            GameMessage responseMessage = new GameMessage(GameRequest.OK);
            ObjectOutputStream socketOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            socketOutputStream.writeObject(responseMessage);
        }
        catch(IOException ioe)
        {
            removePlayer();
            ioe.printStackTrace();
        }
    }

    public void sendKickMessage()
    {
        try
        {
            GameMessage responseMessage = new GameMessage(GameRequest.KICK);
            ObjectOutputStream socketOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            socketOutputStream.writeObject(responseMessage);
        }
        catch(IOException ioe)
        {
            removePlayer();
            ioe.printStackTrace();
        }
    }

    public void startThread()
    {
        thread = new Thread(this.runMethod);
        thread.start();
    }

    public void endThreadAndRemove()
    {
        if(thread != null)
        {
            thread.interrupt();
            removePlayer();
        }
    }

    public int getId()
    {
        return this.id;
    }

    public int getIsSlow() {
        return isSlow;
    }

    public int getIsReverted() {
        return isReverted;
    }

    public int getIsBlind() {
        return isBlind;
    }

    public int getIsLight() {
        return isLight;
    }

    public boolean isDead() {
        return isDead;
    }
}
