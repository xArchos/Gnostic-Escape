abstract class Moveable extends XYObject {
    protected boolean lastMoveTeleported = false;

    public boolean move(Direction direction) {
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

        for (int i = 0; i < SimpleGame.getStoneList().size(); i++) {
            if (desiredX == SimpleGame.getStoneList().get(i).getCoordX() && desiredY == SimpleGame.getStoneList().get(i).getCoordY()) {
                if (!SimpleGame.getStoneList().get(i).move(direction)) {
                    return false;
                }
            }
        }

        for (int i = 0; i < SimpleGame.getOpeningKeyList().size(); i++) {
            if (desiredX == SimpleGame.getOpeningKeyList().get(i).getCoordX() && desiredY == SimpleGame.getOpeningKeyList().get(i).getCoordY()) {
                if (!SimpleGame.getOpeningKeyList().get(i).move(direction)) {
                    return false;
                }
            }
        }

        coordX = desiredX;
        coordY = desiredY;

        return true;
    }

    public void moveByCatapult(Direction direction, int power) {
        int desiredX = coordX;
        int desiredY = coordY;
        int toMove = power;

        while (toMove > 0) {
            if (direction == Direction.UP) {
                if (desiredY <= 0) {
                    break;
                }

                if (SimpleGame.getTileByIndex(desiredX, desiredY - 1).getType() == TileType.BLOCK) {
                    break;
                }

                desiredY = desiredY - 1;
            } else if (direction == Direction.DOWN) {
                if (desiredY >= SimpleGame.getGameboardY() - 1) {
                    break;
                }

                if (SimpleGame.getTileByIndex(desiredX, desiredY + 1).getType() == TileType.BLOCK) {
                    break;
                }

                desiredY = desiredY + 1;
            } else if (direction == Direction.LEFT) {
                if (desiredX <= 0) {
                    break;
                }

                if (SimpleGame.getTileByIndex(desiredX - 1, desiredY).getType() == TileType.BLOCK) {
                    break;
                }

                desiredX = desiredX - 1;
            }

            if (direction == Direction.RIGHT) {
                if (desiredX >= SimpleGame.getGameboardX() - 1) {
                    break;
                }

                if (SimpleGame.getTileByIndex(desiredX + 1, desiredY).getType() == TileType.BLOCK) {
                    break;
                }

                desiredX = desiredX + 1;
            }

            toMove--;
        }

        coordX = desiredX;
        coordY = desiredY;
    }
}