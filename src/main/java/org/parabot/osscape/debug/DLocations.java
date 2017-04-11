package org.parabot.osscape.debug;

import org.parabot.core.Context;
import org.parabot.core.paint.AbstractDebugger;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.osscape.Loader;
import org.parabot.osscape.accessors.Client;
import org.parabot.osscape.api.methods.Calculations;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;
import org.parabot.osscape.api.wrapper.Player;
import org.parabot.osscape.api.wrapper.Tile;

import java.awt.*;

public class DLocations extends AbstractDebugger {
    private boolean enabled;

    @Override
    public void paint(Graphics g) {
        if (!Game.isLoggedIn()) {
            return;
        }
        final Client        client   = Loader.getClient();
        final Player        p        = Players.getMyPlayer();
        final Tile          t        = p.getLocation();
        final Point         point    = Calculations.tileToMap(t);
        final PaintDebugger debugger = Context.getInstance().getPaintDebugger();
        debugger.addLine("MapBase: (" + client.getBaseX() + ", "
                + client.getBaseY() + ")");
        debugger.addLine("Location: (" + t.getX() + ", " + t.getY() + ")");
        debugger.addLine("Moving: " + p.isMoving());

        g.setColor(Color.RED);
        g.fillRect(point.x - 2, point.y - 2, 4, 4);

        g.setColor(Color.BLUE);
        g.fillRect(point.x, point.y, 1, 1);
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