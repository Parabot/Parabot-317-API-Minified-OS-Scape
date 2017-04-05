package org.ethan.oss.api.randoms.impl;

import org.ethan.oss.Constants;
import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.enums.Tab;
import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.methods.Inventory;
import org.ethan.oss.api.methods.Item;
import org.ethan.oss.api.randoms.RandomEvent;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.ethan.oss.utils.Condition;
import org.parabot.environment.api.utils.Filter;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;
import org.parabot.osscape.api.wrapper.Player;

public class ModDetection extends RandomEvent {

    private String  name       = "ModLog";
    private boolean sentString = false;

    public static final Player getNextStaff() {
        Player[] targ = Players.getNearest(new Filter<Player>() {

            @Override
            public boolean accept(Player p) {
                if (Game.isLoggedIn() && p != null && p.distanceTo() <= 15) {
                    for (String i : Constants.STAFF_NAMES) {
                        if (p.getName().toLowerCase().equals(i.toLowerCase())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        return targ[0];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return "Ethan";
    }

    @Override
    public boolean active() {
        Player p = getNextStaff();
        if (Constants.EDGEVILLE_TILE.distanceTo() > 70) {
            if (p != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void solve() {
        while (Game.getGameState() != 10) {
            if (Constants.EDGEVILLE_TILE.distanceTo() > 70) {
                if (!Tab.INVENTORY.isOpen()) {
                    Tab.INVENTORY.open();
                    Condition.sleep(750);
                }
                if (Inventory.contains("Teleport to house")) {
                    Item i = Inventory.getItem("Teleport to house");
                    if (i.isValid()) {
                        i.click();
                        Condition.wait(new Condition.Check() {
                            public boolean poll() {
                                return Constants.EDGEVILLE_TILE.distanceTo() <= 70;
                            }
                        }, 100, 40);
                    }
                }
            } else {
                if (!Tab.LOGOUT.isOpen()) {
                    Tab.LOGOUT.open();
                    Condition.sleep(750);
                } else {
                    WidgetChild w = Widgets.get(182, 10);
                    if (w != null && w.isVisible() && w.getWidth() > 0) {
                        w.click();
                        Condition.wait(new Condition.Check() {
                            public boolean poll() {
                                return !Game.isLoggedIn();
                            }
                        }, 100, 40);
                    }
                }
            }
        }
        final long loggedOutStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - loggedOutStart < 600000) {
            if (!sentString) {
                System.out.println("Sleeping while mod is there.");
                sentString = true;
            }
            Condition.sleep(150);
        }
        ServerEngine.getInstance().setStaffRandomsCompleted(ServerEngine.getInstance().getStaffRandomsCompleted() + 1);
    }

    @Override
    public void reset() {
        name = "ModLog";
    }
}