import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapLoader {
    private int gameboardX;
    private int gameboardY;

    public Tile[][] loadMap(String filename) {
        Tile[][] map = null;
        int playerX;
        int playerY;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int y = 0;

            // Wczytaj rozmiary mapy
            line = reader.readLine();
            String[] size = line.split(" ");
            gameboardX = Integer.parseInt(size[0]);
            gameboardY = Integer.parseInt(size[1]);
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


        } catch (IOException e) {
            e.printStackTrace();
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
}