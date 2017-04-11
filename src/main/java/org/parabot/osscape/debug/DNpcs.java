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
    public void paint(Graphics g) {
        final Npc[] npcs = Npcs.getNearest();
        for (final Npc npc : npcs) {
            npc.draw((Graphics2D) g);
        }
    }
}
