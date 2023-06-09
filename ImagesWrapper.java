import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.util.Duration;

import javafx.animation.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

public class ImagesWrapper
{
    //dostęp pakietowy aby nie tworzyć do tego wszystkiego getterów
    static Image PlayerImage = null;
    static Image deadPlayerImage = null;
    static Image winPlayerImage = null;
    static Image emptyTileImage = null;
    static Image fullTileImage = null;
    static Image bluePortalImage = null;
    static Image orangePortalImage = null;
    static Image blueOffPortalImage = null;
    static Image orangeOffPortalImage = null;
    static Image stoneImage = null;
    static Image magicStoneImage = null;
    static Image prizeImage = null;
    static Image gateImage = null;
    static Image growingImage = null;
    static Image openingKeyImage = null;
    static Image healthPackImage = null;
    static Image spikeImage = null;
    static Image pitfallImage = null;
    static Image pressurePlateOnImage = null;
    static Image pressurePlateOffImage = null;
    static Image leverOnImage = null;
    static Image leverOffImage = null;
    static Image catapultUpImage = null;
    static Image catapultLeftImage = null;
    static Image catapultRightImage = null;
    static Image catapultDownImage = null;
    static Image revertedPlayerImage = null;

    static int tileX;
    static int tileY;

    public static void imagesSetup() throws FileNotFoundException
    {
        FileInputStream ImgInputStream;

        ImgInputStream = new FileInputStream("./img/player.png");
        PlayerImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/tile.png");
        emptyTileImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/block.png");
        fullTileImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/blue_portal.png");
        bluePortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/orange_portal.png");
        orangePortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/blue_off_portal.png");
        blueOffPortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/orange_off_portal.png");
        orangeOffPortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/stone.png");
        stoneImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/magic_stone.png");
        magicStoneImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/prize.png");
        prizeImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/gate.png");
        gateImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/growing.png");
        growingImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/key.png");
        openingKeyImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/health_pack.png");
        healthPackImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/spike.png");
        spikeImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/pitfall.png");
        pitfallImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/plateOn.png");
        pressurePlateOnImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/plateOff.png");
        pressurePlateOffImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/leverOn.png");
        leverOnImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/leverOff.png");
        leverOffImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultUp.png");
        catapultUpImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultDown.png");
        catapultDownImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultRight.png");
        catapultRightImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/catapultLeft.png");
        catapultLeftImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/deadPlayer.png");
        deadPlayerImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("./img/winPlayer.png");
        winPlayerImage = new Image(ImgInputStream);
    }
}
