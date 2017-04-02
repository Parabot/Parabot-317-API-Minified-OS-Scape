package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.interactive.Players;
import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.wrappers.Player;
import org.ethan.oss.utils.Utilities;
import org.parabot.environment.api.utils.Filter;

import java.awt.*;

public class PlayerDebugger extends Debugger<Player> {
    public static boolean populated = false;

    @Override
    public Player[] elements() {
        return Players.getAll(new Filter<Player>() {
            @Override
            public boolean accept(Player p) {
                return p.isValid();
            }
        });
    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugPlayers() && Game.isLoggedIn();
    }

    @Override
    public void render(Graphics2D graphics) {
        if (!populated) {
            Utilities.populateCheckedList("CheckedAccounts");
        } else {
            for (Player p : refresh()) {
                if (p != null && p.isValid()) {
                    // Point point = p.getPointOnScreen();
                    int size = ServerEngine.getInstance().getNames().size();
                    if (!ServerEngine.getInstance().getNames().contains(p.getName()) && !ServerEngine.getInstance().getPreCheckedNames().contains(p.getName())) {

                        ServerEngine.getInstance().getNames().add(p.getName());
                    }

                    graphics.drawString("" + size, 5, 60);
                    // graphics.setColor(Color.BLUE);
                    // graphics.fillRect((int) point.x, (int) point.y, 5, 5);
                    // graphics.setColor(Color.black);
                    // String name = p.getName();
                    // graphics.drawString(name, point.x -
                    // (metrics.stringWidth(name) / 2), point.y - 5);
                    // p.getLocation().draw(graphics, Color.WHITE);

                }
            }
        }
    }

}