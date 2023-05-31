class Prize extends XYObject {
    public Prize(int x, int y) {
        coordX = x;
        coordY = y;
    }

    public void draw() {
        SimpleGame.gc.drawImage(SimpleGame.prizeImage, coordX * SimpleGame.TILE_X,
                coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}