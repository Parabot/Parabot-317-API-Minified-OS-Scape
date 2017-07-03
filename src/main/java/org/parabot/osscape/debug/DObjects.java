package org.parabot.osscape.debug;

import org.ethan.oss.api.interactive.GameObjects;
import org.ethan.oss.api.wrappers.GameObject;
import org.parabot.core.paint.AbstractDebugger;

import java.awt.*;

/**
 * @author JKetelaar
 */
public class DObjects extends AbstractDebugger {
    private boolean enabled;

    @Override
    public void toggle() {
        this.enabled = !this.enabled;

        if (true) {
            GameObject[] npcs = GameObjects.getAll();
            for (final GameObject npc : GameObjects.getAll()) {
                System.out.println(npc.getActions());
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void paint(Graphics g) {
        final GameObject[] npcs = GameObjects.getAll();
        for (final GameObject npc : npcs) {
            npc.draw((Graphics2D) g);
            System.out.println(npc.getActions());
        }
    }
}
