import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoadMap {
    public static Tile[][] loadmap(String filename) {
        Tile[][] map = null;
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            int width = scanner.nextInt();
            int height = scanner.nextInt();
            map = new Tile[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    String tileType = scanner.next();
                    map[i][j] = new Tile(TileType.valueOf(tileType));
                }
            }

            while (scanner.hasNext()) {
                String token = scanner.next();
                switch (token) {
                    case "{PLAYERS}":
                        int numPlayers = scanner.nextInt();
                        for (int i = 0; i < numPlayers; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            Player player = new Player(x, y);
                            SimpleGame.getPlayerList().add(player);
                        }
                        break;
                    case "{STONES}":
                        int numStones = scanner.nextInt();
                        for (int i = 0; i < numStones; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            Stone stone = new Stone(x, y);
                            SimpleGame.getStoneList().add(stone);
                        }
                        break;
                    case "{TELEPORTS}":
                        int numTeleports = scanner.nextInt();
                        for (int i = 0; i < numTeleports; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            //Teleport teleport = new Teleport(x ,y);
                            //SimpleGame.getTeleportList().add(teleport);
                        }
                        break;
                    case "{PRIZES}":
                        int numPrizes = scanner.nextInt();
                        for (int i = 0; i < numPrizes; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            Prize prize = new Prize(x, y);
                            SimpleGame.getPrizeList().add(prize);
                        }
                        break;
                    case "{GATES}":
                        int numGates = scanner.nextInt();
                        for (int i = 0; i < numGates; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            Gate gate = new Gate(x, y);
                            SimpleGame.getGateList().add(gate);
                        }
                        break;
                    case "{OPENING_KEYS}":
                        int numOpeningKeys = scanner.nextInt();
                        for (int i = 0; i < numOpeningKeys; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            OpeningKey key = new OpeningKey(x, y);
                            SimpleGame.getOpeningKeyList().add(key);
                        }
                        break;
                    case "{HEALTH_PACKS}":
                        int numHealthPacks = scanner.nextInt();
                        for (int i = 0; i < numHealthPacks; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            HealthPack healthPack = new HealthPack(x, y, 100); //TODO what amount?
                            SimpleGame.getHealthPackList().add(healthPack);
                        }
                        break;
                    case "{SPIKES}":
                        int numSpikes = scanner.nextInt();
                        for (int i = 0; i < numSpikes; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            Spike spike = new Spike(x, y, 1); //TODO what damage?
                            SimpleGame.getSpikeList().add(spike);
                        }
                        break;
                    case "{PITFALL}":
                        int numPitfalls = scanner.nextInt();
                        for (int i = 0; i < numPitfalls; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            Pitfall pitfall = new Pitfall(x, y, 1); //TODO what damage?
                            SimpleGame.getPitfallList().add(pitfall);
                        }
                        break;
                    case "{PRESSURE_PLATES}":
                        int numPressurePlates = scanner.nextInt();
                        for (int i = 0; i < numPressurePlates; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            //PressurePlate pressurePlate = new PressurePlate(x, y);
                            //SimpleGame.getPressurePlateList().add(pressurePlate);
                        }
                        break;
                    case "{LEVERS}":
                        int numLevers = scanner.nextInt();
                        for (int i = 0; i < numLevers; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            //Lever lever = new Lever(x, y);
                            //SimpleGame.getLeverList().add(lever);
                        }
                        break;
                    case "{CATAPULTS}":
                        int numCatapults = scanner.nextInt();
                        for (int i = 0; i < numCatapults; i++) {
                            int x = scanner.nextInt();
                            int y = scanner.nextInt();
                            //Catapult catapult = new Catapult(x, y);
                            //SimpleGame.getCatapultList().add(catapult);
                        }
                        break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException error) {
            error.printStackTrace();
        }
        return map;
    }
}


/**
 5 5
 GRASS GRASS GRASS GRASS GRASS
 GRASS EMPTY BLOCK EMPTY GRASS
 BLOCK BLOCK EMPTY BLOCK BLOCK
 GRASS EMPTY GRASS EMPTY GRASS
 GRASS GRASS GRASS GRASS GRASS
 {PLAYERS}
 2
 1 1
 3 3
 {STONES}
 1
 2 2
 {TELEPORTS}
 1
 4 4
 {PRIZES}
 2
 0 1
 3 4
 {GATES}
 1
 2 3
 {OPENING_KEYS}
 1
 1 3
 {HEALTH_PACKS}
 1
 0 2
 {SPIKES}
 1
 4 2
 {PITFALL}
 1
 2 1
 {PRESSURE_PLATES}
 1
 3 2
 {LEVERS}
 1
 1 4
 {CATAPULTS}
 1
 4 0
 */