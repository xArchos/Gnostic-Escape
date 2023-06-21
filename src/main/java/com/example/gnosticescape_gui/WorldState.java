package com.example.gnosticescape_gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorldState implements Serializable
{
    private int gameboardX;
    private int gameboardY;
    private Tile[][] tiles = null;

    private List<Player> playerList = null;
    private List<Stone> stoneList = null;
    private List<Teleport> teleportList = null;
    private List<Prize> prizeList = null;
    private List<Gate> gateList = null;
    private List<OpeningKey> openingKeyList = null;
    private List<HealthPack> healthPackList = null;
    private List<Spike> spikeList = null;
    private List<Pitfall> pitfallList = null;
    private List<PressurePlate> pressurePlateList = null;
    private List<Lever> leverList = null;
    private List<Catapult> catapultList = null;

    private Player player = null;
    private int deadPlayersNow, deadPlayersEnd;
    private int winPlayersNow, winPlayersEnd;

    public WorldState(Player p)
    {
        gameboardX = SimpleGame.getGameboardX();
        gameboardY = SimpleGame.getGameboardY();
        tiles = new Tile[gameboardX][gameboardY];

        player = p.clone();
        deadPlayersEnd = SimpleGame.DEAD_PLAYERS_END;
        winPlayersEnd = SimpleGame.WIN_PLAYERS_END;
        deadPlayersNow = SimpleGame.deadPlayersCount;
        winPlayersNow = SimpleGame.winPlayersCount;

        for(int i = 0; i < gameboardX; i++)
        {
            for(int j = 0; j < gameboardY; j++)
            {
                tiles[i][j] = SimpleGame.getTileByIndex(i, j);
            }
        }

        playerList = new ArrayList<Player>();
        stoneList = new ArrayList<Stone>();
        teleportList = new ArrayList<Teleport>();
        prizeList = new ArrayList<Prize>();
        gateList = new ArrayList<Gate>();
        openingKeyList = new ArrayList<OpeningKey>();
        healthPackList = new ArrayList<HealthPack>();
        spikeList = new ArrayList<Spike>();
        pitfallList = new ArrayList<Pitfall>();
        pressurePlateList = new ArrayList<PressurePlate>();
        leverList = new ArrayList<Lever>();
        catapultList = new ArrayList<Catapult>();

        for(Player player : SimpleGame.getPlayerList())
        {
            playerList.add(player.clone());
        }

        for(Stone stone : SimpleGame.getStoneList())
        {
            stoneList.add(stone.clone());
        }

        for(Teleport teleport : SimpleGame.getTeleportList())
        {
            teleportList.add(teleport.clone());
        }

        for(Prize prize : SimpleGame.getPrizeList())
        {
            prizeList.add(prize.clone());
        }

        for(Gate gate : SimpleGame.getGateList())
        {
            gateList.add(gate.clone());
        }

        for(OpeningKey openingKey : SimpleGame.getOpeningKeyList())
        {
            openingKeyList.add(openingKey.clone());
        }

        for(HealthPack healthPack : SimpleGame.getHealthPackList())
        {
            healthPackList.add(healthPack.clone());
        }

        for(Spike spike : SimpleGame.getSpikeList())
        {
            spikeList.add(spike.clone());
        }

        for(Pitfall pitfall : SimpleGame.getPitfallList())
        {
            pitfallList.add(pitfall.clone());
        }

        for(PressurePlate pressurePlate : SimpleGame.getPressurePlateList())
        {
            pressurePlateList.add(pressurePlate.clone());
        }

        for(Lever lever : SimpleGame.getLeverList())
        {
            leverList.add(lever.clone());
        }

        for(Catapult catapult : SimpleGame.getCatapultList())
        {
            catapultList.add(catapult.clone());
        }
    }

    public Player getPlayer()
    {
        return player;
    }

    public int getDeadPlayersNow()
    {
        return deadPlayersNow;
    }

    public int getDeadPlayersEnd()
    {
        return deadPlayersEnd;
    }

    public int getWinPlayersNow()
    {
        return winPlayersNow;
    }

    public int getWinPlayersEnd()
    {
        return winPlayersEnd;
    }

    public int getGameboardX()
    {
        return gameboardX;
    }

    public int getGameboardY()
    {
        return gameboardY;
    }

    public List<Teleport> getTeleportList()
    {
        return teleportList;
    }

    public List<Player> getPlayerList()
    {
        return playerList;
    }

    public List<Stone> getStoneList()
    {
        return stoneList;
    }

    public List<Prize> getPrizeList()
    {
        return prizeList;
    }

    public List<Gate> getGateList()
    {
        return gateList;
    }

    public List<OpeningKey> getOpeningKeyList()
    {
        return openingKeyList;
    }

    public List<Catapult> getCatapultList()
    {
        return catapultList;
    }

    public List<Lever> getLeverList()
    {
        return leverList;
    }

    public List<PressurePlate> getPressurePlateList()
    {
        return pressurePlateList;
    }

    public List<Pitfall> getPitfallList()
    {
        return pitfallList;
    }

    public List<Spike> getSpikeList()
    {
        return spikeList;
    }

    public List<HealthPack> getHealthPackList()
    {
        return healthPackList;
    }

    public Tile getTileByIndex(int x, int y)
    {
        return tiles[x][y];
    }
}
