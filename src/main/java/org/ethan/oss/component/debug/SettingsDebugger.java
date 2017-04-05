package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.parabot.osscape.api.methods.Game;

import java.awt.*;

public class SettingsDebugger extends Debugger<String> {

    private int[] array = new int[2000];

    @Override
    public String[] elements() {
        return new String[0];
    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugSettings();
    }

    @Override
    public void render(Graphics2D graphics) {
        for (int i = 0; i < array.length; i++) {
            int latest = Game.getSettings().get(i);
            if (array[i] != latest) {
                if (latest != -1) {
                    if (i != 0) {
                        System.out.println("Index: " + i + " changed from value: " + array[i] + " to " + latest);
                    }
                }
            }
        }
        for (int i = 1; i < array.length; i++) {
            array[i] = Game.getSettings().get(i);
        }
    }

}