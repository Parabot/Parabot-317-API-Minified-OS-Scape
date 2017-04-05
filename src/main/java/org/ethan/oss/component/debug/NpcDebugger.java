package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.parabot.environment.api.utils.Filter;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Npcs;
import org.parabot.osscape.api.wrapper.Npc;

import java.awt.*;

public class NpcDebugger extends Debugger<Npc> {

    @Override
    public Npc[] elements() {
        return Npcs.getNearest(new Filter<Npc>() {
            @Override
            public boolean accept(Npc n) {
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

        for (Npc n : refresh()) {
            if (n != null) {

                Point point = n.getPointOnScreen();

                graphics.setColor(Color.BLUE);
                graphics.fillRect((int) point.x, (int) point.y, 5, 5);
                graphics.setColor(Color.black);
                String name = "[" + n.getName() + " - " + n.getDef().getId() + "] - " + n.getLocation();
                graphics.drawString(name, point.x - (metrics.stringWidth(name) / 2), point.y - 5);
                n.getLocation().draw(graphics, Color.orange);

                //n.draw(graphics);
            }

        }

    }

}