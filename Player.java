import java.util.ListIterator;

class Player extends Moveable {
    private static final int START_HEALTH = 1000;

    private int health;
    private boolean isDead = false;

    private boolean slowTurn = false;
    private int isSlow = 0;
    private int isReverted = 0;
    private int isBlind = 0;
    private int isLight = 0;

    public Player(int x, int y) {
        coordX = x;
        coordY = y;
        health = START_HEALTH;
        lastMoveTeleported = false;
    }

    public int getHealth() {
        return health;
    }

    public void addHealth(int delta) {
        health = health + delta;
    }

    public boolean move(Direction direction) {
        int previousX = coordX;
        int previousY = coordY;

        if (isSlow > 0) {
            slowTurn = !slowTurn;
            if (slowTurn) {
                return false;
            }
        }

        if (isReverted > 0) {
            switch (direction) {
                case LEFT -> direction = Direction.RIGHT;
                case RIGHT -> direction = Direction.LEFT;
                case UP -> direction = Direction.DOWN;
                case DOWN -> direction = Direction.UP;
            }
        }

        boolean moved;

        if (isLight <= 0) {
            moved = super.move(direction);
        } else {
            moved = moveNoStone(direction);
        }

        if (moved) {
            ListIterator<HealthPack> healthPackIterator = SimpleGame.getHealthPackList().listIterator();
            while (healthPackIterator.hasNext()) {
                HealthPack currentHealthPack = healthPackIterator.next();
                if (coordX == currentHealthPack.getCoordX() && coordY == currentHealthPack.getCoordY()) {
                    health = health + currentHealthPack.getAmount();
                    healthPackIterator.remove();
                }
            }

            ListIterator<Pitfall> pitfallIterator = SimpleGame.getPitfallList().listIterator();
            while (pitfallIterator.hasNext()) {
                Pitfall currentPitfall = pitfallIterator.next();
                if (coordX == currentPitfall.getCoordX() && coordY == currentPitfall.getCoordY()) {
                    health = health - currentPitfall.getDamage();
                    pitfallIterator.remove();
                }
            }
        }

        return moved;
    }

    private boolean moveNoStone(Direction direction) {
        int desiredX = coordX;
        int desiredY = coordY;

        switch (direction) {
            case UP -> desiredY = desiredY - 1;
            case DOWN -> desiredY = desiredY + 1;
            case RIGHT -> desiredX = desiredX + 1;
            case LEFT -> desiredX = desiredX - 1;
            default -> {
            }
        }

        if (desiredX < 0 || desiredY < 0) {
            return false;
        }

        if (desiredX >= SimpleGame.getGameboardX() || desiredY >= SimpleGame.getGameboardY()) {
            return false;
        }

        for (int i = 0; i < SimpleGame.getGateList().size(); i++) {
            if (desiredX == SimpleGame.getGateList().get(i).getCoordX() && desiredY == SimpleGame.getGateList().get(i).getCoordY()) {
                return false;
            }
        }

        if (SimpleGame.getTileByIndex(desiredX, desiredY).getType() == TileType.BLOCK) {
            return false;
        }

        lastMoveTeleported = false;

        if (lastMoveTeleported == false) {
            for (int i = 0; i < SimpleGame.getTeleportList().size(); i++) {
                if (desiredX == SimpleGame.getTeleportList().get(i).getCoordX() && desiredY == SimpleGame.getTeleportList().get(i).getCoordY() && SimpleGame.getTeleportList().get(i).isActive()) {
                    desiredX = SimpleGame.getTeleportList().get(i).getTargetX();
                    desiredY = SimpleGame.getTeleportList().get(i).getTargetY();
                    lastMoveTeleported = true;
                } else if (desiredX == SimpleGame.getTeleportList().get(i).getTargetX() && desiredY == SimpleGame.getTeleportList().get(i).getTargetY() && SimpleGame.getTeleportList().get(i).isActive()) {
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

    public void draw() {
        if (isReverted > 0) {
            SimpleGame.gc.drawImage(SimpleGame.revertedPlayerImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        } else {
            SimpleGame.gc.drawImage(SimpleGame.PlayerImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }

    public void enchantmentsTimeAdvance() {
        if (isSlow > 0) {
            isSlow = isSlow - 1;
        }

        if (isReverted > 0) {
            isReverted = isReverted - 1;
        }

        if (isBlind > 0) {
            isBlind = isBlind - 1;
        }

        if (isLight > 0) {
            isLight = isLight - 1;
        }
    }

    public void setXY(int x, int y) {
        coordX = x;
        coordY = y;
    }

    public void setSlow(int ticks) {
        isSlow = ticks;
    }

    public void setReverted(int ticks) {
        isReverted = ticks;
    }

    public void setBlind(int ticks) {
        isBlind = ticks;
    }

    public void setLight(int ticks) {
        isLight = ticks;
    }
}