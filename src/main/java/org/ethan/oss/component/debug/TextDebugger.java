package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.interactive.Camera;
import org.ethan.oss.api.interactive.Players;
import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.methods.Menu;

import java.awt.*;
import java.util.ArrayList;

public class TextDebugger extends Debugger<String> {

    private final ArrayList<String> debuggedList = new ArrayList<>();
    private final ServerEngine      engine       = ServerEngine.getInstance();

    @Override
    public String[] elements() {
        debuggedList.clear();

        drawText(engine.isDebugText(), "Animation -^^> " + Players.getLocal().getAnimation());
        drawText(engine.isDebugText(), "Logged in -^^> " + Game.isLoggedIn() + " : " + Game.getGameState());
        drawText(engine.isDebugText(), "Moving:  -^^> " + Players.getLocal().isMoving());
        drawText(engine.isDebugText(), "Player Location -^^> [" + Players.getLocal().getLocation().getX() + " - "
                + Players.getLocal().getLocation().getY() + "]");
        drawText(engine.isDebugText(), "Floor -^^> " + Game.getPlane());
        drawText(engine.isDebugText(), "Map Base -^^> [" + Game.getBaseX() + " , " + Game.getBaseY() + "]");
        drawText(engine.isDebugText(),
                "Camera -^^> [" + Camera.getX() + " , " + Camera.getY() + " , " + Camera.getZ() + "] Pitch: "
                        + Camera.getPitch() + " Yaw: " + Camera.getYaw() + " Map Angle: " + Camera.getAngle() + "]");
        drawText(engine.isDebugText(), "Camera Angle -^^> " + Camera.getAngle());
        drawText(engine.isDebugText(), "Menu Rectangle -^^> " + Menu.getArea().toString());
        drawText(engine.isDebugText(), "Menu Open -^^> " + Menu.isOpen());
        drawText(engine.isDebugText(), "Menu");

        java.util.List<String> actions = Menu.getActions();
        java.util.List<String> options = Menu.getOptions();
        for (int i = 0; i < actions.size(); i++) {
            if (options.size() > i) {
                drawText(true, "-^^> A:" + actions.get(i) + " O:" + options.get(i));
            }
        }

        return debuggedList.toArray(new String[debuggedList.size()]);
    }

    @Override
    public boolean activate() {
        return engine.isDebugText();
    }

    @Override
    public void render(Graphics2D graphics) {
        int yOff = 30;

        for (String str : elements()) {
            graphics.drawString(str, 15, yOff);
            yOff += 15;
        }
    }

    private void drawText(boolean active, String debug) {
        if (active) {
            debuggedList.add(debug);
        }
    }

}