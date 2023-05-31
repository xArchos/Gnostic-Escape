class Stone extends Moveable {
    public Stone(int x, int y) {
        coordX = x;
        coordY = y;
        lastMoveTeleported = false;
    }

    public void draw() {
        SimpleGame.gc.drawImage(SimpleGame.stoneImage, coordX * SimpleGame.TILE_X,
                coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}