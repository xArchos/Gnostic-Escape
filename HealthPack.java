class HealthPack extends XYObject {
    int amount;

    public int getAmount() {
        return amount;
    }

    public HealthPack(int x, int y, int amount) {
        coordX = x;
        coordY = y;
        this.amount = amount;
    }

    public void draw() {
        SimpleGame.gc.drawImage(SimpleGame.healthPackImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}