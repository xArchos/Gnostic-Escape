import java.util.ListIterator;

class OpeningKey extends Moveable {
    public OpeningKey(int x, int y) {
        coordX = x;
        coordY = y;
    }

    public void draw() {
        SimpleGame.gc.drawImage(SimpleGame.openingKeyImage, coordX * SimpleGame.TILE_X,
                coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }

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

        ListIterator<Gate> iterator = SimpleGame.getGateList().listIterator();
        while (iterator.hasNext()) {
            Gate currentGate = iterator.next();
            if (desiredX == currentGate.getCoordX() && desiredY == currentGate.getCoordY()) {
                iterator.remove();
                SimpleGame.getOpeningKeyList().remove(this);
                return true;
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
}