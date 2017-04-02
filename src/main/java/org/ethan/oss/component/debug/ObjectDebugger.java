package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.interactive.GameObjects;
import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.wrappers.GameObject;
import org.ethan.oss.api.wrappers.GameObject.Type;
import org.parabot.environment.api.utils.Filter;

import java.awt.*;

public class ObjectDebugger extends Debugger<GameObject> {
    GameObject[] objectCache = null;
    private long lastUpdate = System.currentTimeMillis();

    @Override
    public GameObject[] elements() {
        if (objectCache == null) {
            objectCache = GameObjects.getAll(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject n) {
                    return n.isValid() && n.distanceTo() < 7;
                }
            });
            return objectCache;
        } else {
            return objectCache;
        }
    }

    public GameObject[] objects() {

        return GameObjects.getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject n) {
                return n.isValid() && n.distanceTo() < 12 && n.getType().equals(Type.INTERACTIVE);
            }
        });

    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugGameObjects() && Game.isLoggedIn();
    }

    @Override
    public void render(Graphics2D graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();
        for (GameObject n : elements()) {
            if (n != null && n.isValid()) {
                if (n.distanceTo() < 7 && n.getType().equals(Type.INTERACTIVE)) {
                    Point point = n.getLocation().getPointOnScreen();
                    graphics.setColor(Color.BLUE);
                    graphics.fillRect((int) point.x, (int) point.y, 5, 5);
                    graphics.setColor(Color.black);
                    String name = "[" + n.getId() + "]";
                    graphics.drawString(name, point.x - (metrics.stringWidth(name) / 2), point.y - 5);

                }
            }

            if (System.currentTimeMillis() - lastUpdate > 3000) {
                lastUpdate = System.currentTimeMillis();

                objectCache = GameObjects.getAll(new Filter<GameObject>() {
                    @Override
                    public boolean accept(GameObject n) {
                        return n.isValid() && n.distanceTo() < 12 && n.getType().equals(Type.INTERACTIVE);
                    }
                });
            }
        }

    }

}