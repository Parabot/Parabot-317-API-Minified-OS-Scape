package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.interactive.GroundItems;
import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.wrappers.GroundItem;
import org.parabot.environment.api.utils.Filter;

import java.awt.*;

public class GroundItemDebugger extends Debugger<GroundItem> {

    @Override
    public GroundItem[] elements() {
        return GroundItems.getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem p) {
                return p.isValid() && p.distanceTo() < 7;
            }
        });
    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugGroundItems() && Game.isLoggedIn();
    }

    @Override
    public void render(Graphics2D graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();

        for (GroundItem p : refresh()) {
            if (p != null && p.isValid()) {
                if (p.isValid()) {
                    Point point = p.getPointOnScreen();
                    graphics.setColor(Color.ORANGE);
                    graphics.fillRect((int) point.x, (int) point.y, 5, 5);
                    graphics.setColor(Color.black);
                    String name = p.getName() + " - " + p.getId();
                    graphics.drawString(name, point.x - (metrics.stringWidth(name) / 2), point.y - 5);

                }
            }
        }

    }

}