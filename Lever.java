class Lever extends XYObject {
    boolean isPowered = false;
    boolean wasPowered = false;
    Teleport teleport = null;
    boolean leftOrRight = true;

    public Lever(int x, int y, Teleport teleport) {
        coordX = x;
        coordY = y;
        this.teleport = teleport;
    }

    public boolean isPowered() {
        return isPowered;
    }

    public boolean wasPowered() {
        return wasPowered;
    }

    public void setPowered(boolean power) {
        isPowered = power;
    }

    public void setWasPowered(boolean power) {
        wasPowered = power;
    }

    public void draw() {
        if (leftOrRight) {
            SimpleGame.gc.drawImage(SimpleGame.leverOnImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        } else {
            SimpleGame.gc.drawImage(SimpleGame.leverOffImage, coordX * SimpleGame.TILE_X, coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }

    public void turnOn() {
        isPowered = true;
    }

    public void turnOff() {
        isPowered = false;
    }

    public void toggle() {
        isPowered = !isPowered;
    }

    public void togglePortal() {
        leftOrRight = !leftOrRight;
        teleport.toggle();
    }
}