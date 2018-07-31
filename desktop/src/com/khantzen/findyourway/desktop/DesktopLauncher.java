package com.khantzen.findyourway.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.khantzen.findyourway.FindYourWay;

import static com.khantzen.findyourway.constants.GameConstants.SCREEN_HEIGHT;
import static com.khantzen.findyourway.constants.GameConstants.SCREEN_WIDTH;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;

        new LwjglApplication(new FindYourWay(), config);
    }
}
