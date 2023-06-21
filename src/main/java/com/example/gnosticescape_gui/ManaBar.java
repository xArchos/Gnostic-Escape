package com.example.gnosticescape_gui;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

public class ManaBar extends ProgressBar
{
    ManaBar(Pane root)
    {
        super(SimpleGame.mana/(double)SimpleGame.MANA_LIMIT);
        this.setStyle("-fx-accent: purple");
    }

    public void updateMana()
    {
        this.setProgress(SimpleGame.mana/(double)SimpleGame.MANA_LIMIT);
    }

}
