package org.parabot.osscape.debug;

import org.parabot.core.paint.AbstractDebugger;
import org.parabot.osscape.api.methods.Npcs;
import org.parabot.osscape.api.wrapper.Npc;

import java.awt.*;

/**
 * @author JKetelaar
 */
public class DNpcs extends AbstractDebugger {

    private boolean enabled;

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void paint(Graphics graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();

        for (Npc npc : Npcs.getNearest()) {
            if (npc != null && npc.isOnScreen()) {
                Point point = npc.getPointOnScreen();

                graphics.setColor(Color.BLUE);
                graphics.fillRect(point.x, point.y, 5, 5);
                graphics.setColor(Color.black);
                String name = "[" + npc.getName() + " - " + npc.getDef().getId() + "] - " + npc.getLocation();
                graphics.drawString(name, point.x - (metrics.stringWidth(name) / 2), point.y - 5);
                npc.getLocation().draw((Graphics2D) graphics, Color.orange);
            }
        }
    }
}
