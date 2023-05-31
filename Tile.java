class Tile {
    private TileType tileType;
    private static final int GROWING_DAMAGE = 1;

    public Tile() {
        tileType = TileType.EMPTY;
    }

    public Tile(TileType type) {
        tileType = type;
    }

    public void setEmpty() {
        tileType = TileType.EMPTY;
    }

    public void setBlock() {
        tileType = TileType.BLOCK;
    }

    public void setGrowing() {
        tileType = TileType.GROWING;
    }

    public void setBeforeGrowing() {
        tileType = TileType.BEFORE_GROWING;
    }

    public TileType getType() {
        return tileType;
    }

    public void draw(int x, int y) {
        switch (tileType) {
            case EMPTY -> SimpleGame.gc.drawImage(SimpleGame.emptyTileImage, x * SimpleGame.TILE_X,
                    y * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            case BLOCK -> SimpleGame.gc.drawImage(SimpleGame.fullTileImage, x * SimpleGame.TILE_X,
                    y * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            case GROWING, BEFORE_GROWING -> SimpleGame.gc.drawImage(SimpleGame.growingImage, x * SimpleGame.TILE_X,
                    y * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }

    public static int getGrowingDamage() {
        return GROWING_DAMAGE;
    }
}