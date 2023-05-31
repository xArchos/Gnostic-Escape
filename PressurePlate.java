class PressurePlate extends XYObject {
    boolean isPowered = false;
    boolean wasPowered = false;
    Teleport teleport = null;

    public PressurePlate(int x, int y, Teleport teleport) {
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
        if (isPowered) {
            SimpleGame.gc.drawImage(SimpleGame.pressurePlateOnImage, coordX * SimpleGame.TILE_X,
                    coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        } else {
            SimpleGame.gc.drawImage(SimpleGame.pressurePlateOffImage, coordX * SimpleGame.TILE_X,
                    coordY * SimpleGame.TILE_Y, SimpleGame.TILE_X, SimpleGame.TILE_Y);
        }
    }

    public void togglePortal() {
        this.teleport.toggle();
    }

    public void turnOn() {
        isPowered = true;
    }

    public void turnOff() {
        isPowered = false;
    }
}