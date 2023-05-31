class Pitfall extends XYObject {
    int damage;

    public Pitfall(int x, int y, int damage) {
        coordX = x;
        coordY = y;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void draw() {
        SimpleGame.gc.drawImage(SimpleGame.pitfallImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
    }
}
