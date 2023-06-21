package com.example.gnosticescape_gui;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

class HealthBar extends ProgressBar {
    private Player player = null;
    private final Label hpLabel;

    HealthBar(Player player) {
        super((double) (player.getHealth() / Player.START_HEALTH));
        this.player = player;
        hpLabel = new Label();
    }

    public void updateValue() {
        String barColor = "";

        if (player.getHealth() < 0 || player.isDead()) {
            barColor = "-fx-accent: grey";
        } else if (player.getHealth() > 1000) {
            barColor = "fx-accent: #00FFFF";
        } else if (player.getHealth() >= 750) {
            barColor = "-fx-accent: #7FFF00";
        } else if (player.getHealth() >= 500) {
            barColor = "-fx-accent: yellow";
        } else if (player.getHealth() >= 250) {
            barColor = "-fx-accent: orange";
        } else {
            barColor = "-fx-accent: red";
        }

        double healthbarUpdate = ((double) player.getHealth() / (double) Player.START_HEALTH);
        setProgress(healthbarUpdate);

        if (player.isDead()) {
            hpLabel.setText("DEAD");
        } else {
            hpLabel.setText(player.getHealth() + "/" + Player.START_HEALTH);
        }
        hpLabel.setStyle("-fx-text-fill: #ffffff" + ";" + "-fx-font: 13 Georgia");
        setStyle("-fx-text-box-border: #302c2f;" + ";" + barColor + ";" + "-fx-control-inner-background: #302c2f");
    }

    public Label getLabel() {
        return hpLabel;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
