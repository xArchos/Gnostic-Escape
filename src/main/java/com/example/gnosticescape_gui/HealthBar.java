package com.example.gnosticescape_gui;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

class HealthBar
{
    private ProgressBar hpBar;
    private Label hpLabel;
    private final int MIN_VALUE=0;
    private final int MAX_VALUE=1000;
    private boolean dead;

    HealthBar(BorderPane root)
    {
        /*VBox bpLeft = new VBox(1);
        HBox hBox = new HBox();
        this.hpBar = new ProgressBar(1);
        this.hpLabel = new Label("100");
        this.hpLabel.setFont(new Font("Chiller",18));
        hpBar.setStyle("-fx-accent: #7FFF00;");
        hBox.getChildren().add(hpLabel);
        hBox.setAlignment(Pos.BASELINE_LEFT);
        hBox.setMargin(hpLabel,new Insets(0,0,0,34));
        bpLeft.getChildren().add(hpBar);
        bpLeft.getChildren().add(hBox);
        root.setTop(bpLeft);
        this.dead=false;*/
    }

    public ProgressBar getHPBar()
    {
        return hpBar;
    }

    public void setHPBar(int value)
    {
        if(value<MIN_VALUE || dead==true)
        {
            value=MAX_VALUE;
            hpBar.setStyle("-fx-accent: grey");
            dead=true;
        }
        else if(value>MAX_VALUE)
        {
            value=MAX_VALUE;
            hpBar.setStyle("fx-accent: #00FFFF;");
        }
        else if(value>=750)
        {
            hpBar.setStyle("-fx-accent: #7FFF00;");
        }

        else if(value>=500)
        {
            hpBar.setStyle("-fx-accent: yellow");
        }

        else if(value>=250)
        {
            hpBar.setStyle("-fx-accent: orange");
        }
        else
        {
            hpBar.setStyle("-fx-accent: red");
        }
        double healthbarUpdate=value;
        healthbarUpdate/=1000;
        hpBar.setProgress(healthbarUpdate);
        if(dead==true)
            hpLabel.setText("DEAD");
        else
            hpLabel.setText(Integer.toString((int)Math.round(healthbarUpdate*100)));
    }
}
