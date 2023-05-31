class Catapult extends XYObject {
    int power;
    Direction direction;

    public Catapult(int x, int y, int power, Direction direction) {
        coordX = x;
        coordY = y;
        this.power = power;
        this.direction = direction;
    }

    public int getPower() {
        return power;
    }

    public Direction getDirection() {
        return direction;
    }

    public void draw() {
        switch (direction) {
            case UP:
                SimpleGame.gc.drawImage(SimpleGame.catapultUpImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
                break;
            case DOWN:
                SimpleGame.gc.drawImage(SimpleGame.catapultDownImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
                break;
            case RIGHT:
                SimpleGame.gc.drawImage(SimpleGame.catapultRightImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
                break;
            case LEFT:
                SimpleGame.gc.drawImage(SimpleGame.catapultLeftImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
                break;
        }
    }
}