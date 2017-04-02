package org.ethan.oss.api.enums;

import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;

public enum Tab {

    COMBAT("Combat Options"),
    SKILLS("Stats"),
    QUEST("Quest List"),
    INVENTORY("Inventory"),
    EQUIPMENT("Worn Equipment"),
    PRAYER("Prayer"),
    MAGIC("Magic"),
    CLAN_CHAT("Clan Chat"),
    FRIEND_LIST("Friends List"),
    IGNORE_LIST("Ignore List"),
    LOGOUT("Logout"),
    SETTINGS("Options"),
    EMOTES("Emotes"),
    MUSIC("Music Player"),
    ACHIEVMENT_DIARY("Achievement Diaries"),
    KOUREND_TASKS("Kourend Tasks"),
    MINIGAMES("Minigames");

    String name;

    private Tab(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean open() {
        if (!Game.isLoggedIn() || isOpen() || getWidgetChild() == null) {
            return true;
        }
        getWidgetChild().click(true);

        for (int i = 0; i < 20 && !isOpen(); i++, Condition.sleep(Random.nextInt(100, 150))) {
            ;
        }

        return isOpen();
    }

    public boolean isOpen() {
        Tab current = Game.getCurrentTab();
        return Game.isLoggedIn() && current != null && current.getName() != null && getName() != null && current.getName().equals(getName());
    }

    public WidgetChild getWidgetChild() {
        if (!Game.isLoggedIn()) {
            return null;
        }
        for (WidgetChild p : Widgets.get(548).getChildren()) {
            if (p.getActions() != null) {
                for (String s : p.getActions()) {
                    if (getName().equalsIgnoreCase(s)) {
                        return p;
                    }
                }
            }
        }

        return null;
    }

}