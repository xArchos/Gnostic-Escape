package com.example.gnosticescape_gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class DayModeSwitcher extends StackPane
{
    private  Rectangle backgroundRectangle = null;
    private final Button switchButton = new Button();
    private boolean isDayMode = false;

    private String backgroundRectangleStyleDay = null;
    private String switchButtonStyleDay = null;

    private String backgroundRectangleStyleNight = null;
    private String switchButtonStyleNight = null;

    private int backgroundWidth  = 0;

    private int backgroundHeight = 0;

    private int buttonDimmension = 0;

    public DayModeSwitcher(String backgroundRectangleStyleDay, String switchButtonStyleDay,String backgroundRectangleStyleNight, String switchButtonStyleNight, int bgWidth, int bgHeight, int buttonDim)
    {
        super();
        this.backgroundRectangleStyleDay = backgroundRectangleStyleDay;
        this.switchButtonStyleDay = switchButtonStyleDay;
        this.backgroundRectangleStyleNight = backgroundRectangleStyleNight;
        this.switchButtonStyleNight = switchButtonStyleNight;

        this.backgroundWidth=bgWidth;
        this.backgroundHeight = bgHeight;
        this.buttonDimmension = buttonDim;

        this.backgroundRectangle = new Rectangle(backgroundWidth, backgroundHeight, Color.RED);

        getChildren().addAll(backgroundRectangle, switchButton);
        setMinSize(30, 15);

        backgroundRectangle.maxWidth(backgroundWidth); //30
        backgroundRectangle.minWidth(backgroundWidth);
        backgroundRectangle.maxHeight(backgroundHeight); //10
        backgroundRectangle.minHeight(backgroundHeight);

        backgroundRectangle.setArcHeight(backgroundRectangle.getHeight());
        backgroundRectangle.setArcWidth(backgroundRectangle.getHeight());
        backgroundRectangle.setFill(Color.valueOf(backgroundRectangleStyleDay));
        switchButton.setShape(new Circle(2.0));

        setAlignment(switchButton, Pos.CENTER_LEFT);
        switchButton.setMaxSize(buttonDimmension, buttonDimmension);
        switchButton.setMinSize(buttonDimmension, buttonDimmension);
        switchButton.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: " + switchButtonStyleDay + ";");

        EventHandler<Event> dayModeChanger = new EventHandler<Event>()
        {
            @Override
            public void handle(Event e)
            {
                if (isDayMode)
                {
                    switchButton.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: " + switchButtonStyleDay + ";"); //#00893d;
                    backgroundRectangle.setFill(Color.valueOf(backgroundRectangleStyleDay));
                    setAlignment(switchButton, Pos.CENTER_LEFT);
                }
                else
                {
                    switchButton.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); fx-background-color: " + switchButtonStyleNight +";");
                    backgroundRectangle.setFill(Color.valueOf(backgroundRectangleStyleNight));//"#80C49E"
                    setAlignment(switchButton, Pos.CENTER_RIGHT);
                }

                isDayMode = !isDayMode;
                ImagesWrapper.isDayMode = !ImagesWrapper.isDayMode;
            }
        };

        switchButton.setFocusTraversable(false);
        setOnMouseClicked(dayModeChanger);
        switchButton.setOnMouseClicked(dayModeChanger);
    }

    public int getBackgroundWidth() {
        return backgroundWidth;
    }

    public void setBackgroundWidth(int backgroundWidth) {
        this.backgroundWidth = backgroundWidth;
    }

    public int getBackgroundHeight() {
        return backgroundHeight;
    }

    public void setBackgroundHeight(int backgroundHeight) {
        this.backgroundHeight = backgroundHeight;
    }

    public int getButtonDimmension() {
        return buttonDimmension;
    }

    public void setButtonDimmension(int buttonDimmension) {
        this.buttonDimmension = buttonDimmension;
    }

    public String getBackgroundRectangleStyleDay() {
        return backgroundRectangleStyleDay;
    }

    public void setBackgroundRectangleStyleDay(String backgroundRectangleStyleDay) {
        this.backgroundRectangleStyleDay = backgroundRectangleStyleDay;
    }

    public String getSwitchButtonStyleDay() {
        return switchButtonStyleDay;
    }

    public void setSwitchButtonStyleDay(String switchButtonStyleDay) {
        this.switchButtonStyleDay = switchButtonStyleDay;
    }

    public String getBackgroundRectangleStyleNight() {
        return backgroundRectangleStyleNight;
    }

    public void setBackgroundRectangleStyleNight(String backgroundRectangleStyleNight) {
        this.backgroundRectangleStyleNight = backgroundRectangleStyleNight;
    }

    public String getSwitchButtonStyleNight() {
        return switchButtonStyleNight;
    }

    public void setSwitchButtonStyleNight(String switchButtonStyleNight) {
        this.switchButtonStyleNight = switchButtonStyleNight;
    }
}