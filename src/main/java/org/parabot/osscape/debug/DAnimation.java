package org.parabot.osscape.debug;

import org.parabot.core.Context;
import org.parabot.core.paint.AbstractDebugger;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;

import java.awt.*;

public class DAnimation extends AbstractDebugger {
    private boolean enabled;

    @Override
    public void paint(Graphics g) {
        if (!Game.isLoggedIn()) {
            return;
        }
        Context.getInstance().getPaintDebugger().addLine("Animation: " + Players.getMyPlayer().getAnimation());
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