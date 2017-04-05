package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.interactive.Npcs;
import org.parabot.osscape.api.methods.Game;
import org.ethan.oss.api.wrappers.NPC;
import org.parabot.environment.api.utils.Filter;

import java.awt.*;

public class NpcDebugger extends Debugger<NPC> {

    @Override
    public NPC[] elements() {
        return Npcs.getAll(new Filter<NPC>() {
            @Override
            public boolean accept(NPC n) {
                return n.distanceTo() < 7;
            }
        });
    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugNpcs() && Game.isLoggedIn();
    }

    @Override
    public void render(Graphics2D graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();

        for (NPC n : refresh()) {
            if (n != null) {

                Point point = n.getPointOnScreen();

                graphics.setColor(Color.BLUE);
                graphics.fillRect((int) point.x, (int) point.y, 5, 5);
                graphics.setColor(Color.black);
                String name = "[" + n.getName() + " - " + n.getId() + "] - " + n.getLocation();
                graphics.drawString(name, point.x - (metrics.stringWidth(name) / 2), point.y - 5);
                n.getLocation().draw(graphics, Color.orange);

                //n.draw(graphics);
            }

        }

    }

}