class Teleport extends XYObject {
    protected int targetX;
    protected int targetY;
    boolean active;

    public Teleport(int coordX_, int coordY_, int targetX_, int targetY_) {
        coordX = coordX_;
        coordY = coordY_;
        targetX = targetX_;
        targetY = targetY_;
        active = true;
    }

    public Teleport(int coordX_, int coordY_, int targetX_, int targetY_, boolean active_) {
        coordX = coordX_;
        coordY = coordY_;
        targetX = targetX_;
        targetY = targetY_;
        active = active_;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public boolean isActive() {
        return active;
    }

    public void toggle() {
        active = !active;
    }

    public void turnOn() {
        active = true;
    }

    public void turnOff() {
        active = false;
    }

    public void draw() {
        if (active) {
            SimpleGame.gc.drawImage(SimpleGame.bluePortalImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            SimpleGame.gc.drawImage(SimpleGame.orangePortalImage, targetX * SimpleGame.TILE_X, targetY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        } else {
            SimpleGame.gc.drawImage(SimpleGame.blueOffPortalImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
            SimpleGame.gc.drawImage(SimpleGame.orangeOffPortalImage, targetX * SimpleGame.TILE_X, targetY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }
}