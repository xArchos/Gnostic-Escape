package com.example.gnosticescape_gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class ImagesWrapper
{
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
    static Image teleportIcon = null;
    static Image blindIcon = null;
    static Image reverseIcon = null;
    static Image damageIcon = null;
    static Image lightIcon = null;
    static Image slowIcon = null;
    static Image darkBlockTileImage = null;
    static Image darkGrowingImage = null;
    static Image darkEmptyTileImage = null;

    static Image demiurgAvatar = null;

    static Image castSpellIcon = null;

    static int tileX;
    static int tileY;
    static boolean isDayMode = true;

    public static void imagesSetup() throws FileNotFoundException
    {
        FileInputStream ImgInputStream;

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/player.png");
        PlayerImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/tile.png");
        emptyTileImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/block.png");
        fullTileImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/blue_portal.png");
        bluePortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/orange_portal.png");
        orangePortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/blue_off_portal.png");
        blueOffPortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/orange_off_portal.png");
        orangeOffPortalImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/stone.png");
        stoneImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/magic_stone.png");
        magicStoneImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/prize.png");
        prizeImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/gate.png");
        gateImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/growing.png");
        growingImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/key.png");
        openingKeyImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/health_pack.png");
        healthPackImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/spike.png");
        spikeImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/pitfall.png");
        pitfallImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/plateOn.png");
        pressurePlateOnImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/plateOff.png");
        pressurePlateOffImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/leverOn.png");
        leverOnImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/leverOff.png");
        leverOffImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/catapultUp.png");
        catapultUpImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/catapultDown.png");
        catapultDownImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/catapultRight.png");
        catapultRightImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/catapultLeft.png");
        catapultLeftImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/deadPlayer.png");
        deadPlayerImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/winPlayer.png");
        winPlayerImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/slowIcon.png");
        slowIcon = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/reverseIcon.png");
        reverseIcon = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/blindIcon.png");
        blindIcon = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/damageIcon.png");
        damageIcon = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/teleportIcon.png");
        teleportIcon = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/lightIcon.png");
        lightIcon = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/revertedPlayer.png");
        revertedPlayerImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/wand.png");
        castSpellIcon = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/demiurgAvatar.png");
        demiurgAvatar = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/growing_dark.png");
        darkGrowingImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/block_dark.png");
        darkBlockTileImage = new Image(ImgInputStream);

        ImgInputStream = new FileInputStream("src/main/java/com/example/gnosticescape_gui/img/tile_dark.png");
        darkEmptyTileImage = new Image(ImgInputStream);
    }
}
