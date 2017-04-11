package org.parabot.osscape.debug;

import org.parabot.core.paint.AbstractDebugger;
import org.parabot.osscape.api.methods.Calculations;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;
import org.parabot.osscape.api.wrapper.Tile;

import java.awt.*;

public class DMinimap extends AbstractDebugger {
    private boolean enabled;

    @Override
    public void paint(Graphics g) {
        if (!Game.isLoggedIn()) {
            return;
        }
        final Tile  curLoc = Players.getMyPlayer().getLocation();
        final Tile  tile   = new Tile(curLoc.getX() + 1, curLoc.getY());
        final Point point  = Calculations.tileToMap(tile);
        g.setColor(Color.red);
        g.fillRect(point.x - 2, point.y - 2, 4, 4);
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