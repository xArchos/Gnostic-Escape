class Spike extends XYObject {
    int damage;

    public Spike(int x, int y, int damage) {
        coordX = x;
        coordY = y;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void draw() {
        SimpleGame.gc.drawImage(SimpleGame.spikeImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}