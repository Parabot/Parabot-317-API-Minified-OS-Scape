package org.parabot.osscape.debug;

import org.parabot.core.paint.AbstractDebugger;
import org.parabot.osscape.api.methods.Players;
import org.parabot.osscape.api.wrapper.Player;

import java.awt.*;

public class DPlayers extends AbstractDebugger {
    private boolean enabled;

    @Override
    public void paint(Graphics g) {
        final Player[] players = Players.getNearest();
        for (final Player player : players) {
            player.draw((Graphics2D) g);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void toggle() {
        enabled = !enabled;
    }

}