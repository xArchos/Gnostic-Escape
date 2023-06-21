package com.example.gnosticescape_gui;

import java.io.BufferedReader;
import java.io.FileReader;

public class MapLoader {
    private int gameboardX;
    private int gameboardY;
    private int spawnX;
    private int spawnY;

    public Tile[][] loadMap(String filename) throws Exception {
        Tile[][] map = null;
        int playerX;
        int playerY;

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int y = 0;

        // Wczytaj rozmiary mapy
        line = reader.readLine();
        String[] size = line.split(" ");
        gameboardX = Integer.parseInt(size[0]);
        gameboardY = Integer.parseInt(size[1]);
        spawnX = Integer.parseInt(size[2]);
        spawnY = Integer.parseInt(size[3]);
        map = new Tile[gameboardX][gameboardY];

        // Wczytaj mapę
        while ((line = reader.readLine()) != null) {
            switch (line) {
                case "{PLAYER}" -> {
                    line = reader.readLine();
                    String[] playerCoords = line.split(" ");
                    playerX = Integer.parseInt(playerCoords[0]);
                    playerY = Integer.parseInt(playerCoords[1]);
                    if (playerX != -1 && playerY != -1) {
                        Player player = new Player(playerX, playerY);
                        SimpleGame.getPlayerList().add(player);
                    }
                }
                case "{TELEPORT}" -> {
                    line = reader.readLine();
                    String[] teleportCoords = line.split(" ");
                    int teleportX = Integer.parseInt(teleportCoords[0]);
                    int teleportY = Integer.parseInt(teleportCoords[1]);
                    int targetX = Integer.parseInt(teleportCoords[2]);
                    int targetY = Integer.parseInt(teleportCoords[3]);
                    Teleport teleport = new Teleport(teleportX, teleportY, targetX, targetY);
                    SimpleGame.getTeleportList().add(teleport);
                }
                case "{PRIZE}" -> {
                    line = reader.readLine();
                    String[] prizeCoords = line.split(" ");
                    int prizeX = Integer.parseInt(prizeCoords[0]);
                    int prizeY = Integer.parseInt(prizeCoords[1]);
                    Prize prize = new Prize(prizeX, prizeY);
                    SimpleGame.getPrizeList().add(prize);
                }
                case "{STONE}" -> {
                    line = reader.readLine();
                    String[] stoneCoords = line.split(" ");
                    int stoneX = Integer.parseInt(stoneCoords[0]);
                    int stoneY = Integer.parseInt(stoneCoords[1]);

                    Stone stone = new Stone(stoneX, stoneY);
                    SimpleGame.getStoneList().add(stone);
                }
                case "{KEY}" -> {
                    line = reader.readLine();
                    String[] keyCoords = line.split(" ");
                    int keyX = Integer.parseInt(keyCoords[0]);
                    int keyY = Integer.parseInt(keyCoords[1]);

                    OpeningKey key = new OpeningKey(keyX, keyY);
                    SimpleGame.getOpeningKeyList().add(key);
                }
                case "{PLATE}" -> {
                    line = reader.readLine();
                    String[] plateCoords = line.split(" ");
                    int plateX = Integer.parseInt(plateCoords[0]);
                    int plateY = Integer.parseInt(plateCoords[1]);
                    Teleport matchingTeleport = SimpleGame.getTeleportList().get(SimpleGame.getTeleportList().size() - 1);

                    if (matchingTeleport != null) {
                        PressurePlate pressurePlate = new PressurePlate(plateX, plateY, matchingTeleport);
                        SimpleGame.getPressurePlateList().add(pressurePlate);
                    }
                }
                case "{GATE}" -> {
                    line = reader.readLine();
                    String[] gateCoords = line.split(" ");
                    int gateX = Integer.parseInt(gateCoords[0]);
                    int gateY = Integer.parseInt(gateCoords[1]);

                    Gate gate = new Gate(gateX, gateY);
                    SimpleGame.getGateList().add(gate);
                }
                case "{SPIKE}" -> {
                    line = reader.readLine();
                    String[] spikeCoords = line.split(" ");
                    int spikeX = Integer.parseInt(spikeCoords[0]);
                    int spikeY = Integer.parseInt(spikeCoords[1]);
                    int spikeDMG = Integer.parseInt(spikeCoords[2]);

                    Spike spike = new Spike(spikeX, spikeY, spikeDMG);
                    SimpleGame.getSpikeList().add(spike);
                }
                case "{PITFALL}" -> {
                    line = reader.readLine();
                    String[] pitfallCoords = line.split(" ");
                    int pitfallX = Integer.parseInt(pitfallCoords[0]);
                    int pitfallY = Integer.parseInt(pitfallCoords[1]);
                    int pitfallDMG = Integer.parseInt(pitfallCoords[2]);

                    Pitfall pitfall = new Pitfall(pitfallX, pitfallY, pitfallDMG);
                    SimpleGame.getPitfallList().add(pitfall);
                }
                case "{HEALTHPACK}" -> {
                    line = reader.readLine();
                    String[] healthCoords = line.split(" ");
                    int healthCoordsX = Integer.parseInt(healthCoords[0]);
                    int healthCoordsY = Integer.parseInt(healthCoords[1]);
                    int healthVal = Integer.parseInt(healthCoords[2]);

                    HealthPack healthPack = new HealthPack(healthCoordsX, healthCoordsY, healthVal);
                    SimpleGame.getHealthPackList().add(healthPack);
                }
                case "{CATAPULT}" -> {
                    line = reader.readLine();
                    String[] catapultCoords = line.split(" ");
                    int catapultCoordsX = Integer.parseInt(catapultCoords[0]);
                    int catapultCoordsY = Integer.parseInt(catapultCoords[1]);
                    int catapultPower = Integer.parseInt(catapultCoords[2]);
                    String catapultDirection = catapultCoords[3];

                    Direction direction = switch (catapultDirection) {
                        case "UP" -> Direction.UP;
                        case "DOWN" -> Direction.DOWN;
                        case "LEFT" -> Direction.LEFT;
                        case "RIGHT" -> Direction.RIGHT;
                        default ->
                                throw new IllegalArgumentException("Invalid catapult direction: " + catapultDirection);
                    };

                    Catapult catapult = new Catapult(catapultCoordsX, catapultCoordsY, catapultPower, direction);
                    SimpleGame.getCatapultList().add(catapult);
                }
                case "{LEVER}" -> {
                    line = reader.readLine();
                    String[] leverCoords = line.split(" ");
                    int leverCoordsX = Integer.parseInt(leverCoords[0]);
                    int leverCoordsY = Integer.parseInt(leverCoords[1]);
                    Teleport matchingTeleport = SimpleGame.getTeleportList().get(SimpleGame.getTeleportList().size() - 1);

                    if (matchingTeleport != null) {
                        Lever lever = new Lever(leverCoordsX, leverCoordsY, matchingTeleport);
                        SimpleGame.getLeverList().add(lever);
                    }
                }
                default -> {
                    String[] symbols = line.split(" ");
                    for (int x = 0; x < gameboardX; x++) {
                        char symbol = symbols[x].charAt(0);
                        TileType type = getTileType(symbol);
                        map[x][y] = new Tile(type);
                    }
                    y++;
                }
            }
        }

        return map;
    }

    private TileType getTileType(char symbol) {
        return switch (symbol) {
            case 'B' -> TileType.BLOCK;
            case 'G' -> TileType.GROWING;
            case 'A' -> TileType.BEFORE_GROWING;
            default -> TileType.EMPTY;
        };
    }

    public int getGameboardX() {
        return gameboardX;
    }

    public int getGameboardY() {
        return gameboardY;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnX;
    }
}