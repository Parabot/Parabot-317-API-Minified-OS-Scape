package org.parabot.osscape.debug;

import org.parabot.core.paint.AbstractDebugger;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.osscape.api.methods.Players;

import java.awt.*;

public class DUsername extends AbstractDebugger {
    private boolean enabled;

    @Override
    public void paint(Graphics g) {
        PaintDebugger.getInstance().addLine("Username: " + Players.getMyPlayer().getName());
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