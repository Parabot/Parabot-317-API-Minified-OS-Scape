package org.ethan.oss.api.wrappers;

import org.ethan.oss.interfaces.Nameable;

public class Player extends Actor implements Nameable {

    public Player(Object raw) {
        super(raw);
    }

    @Override
    public String getName() {
        if (getRaw() == null) {
            return null;
        }
        return (String) getFieldValue("PlayerName", getRaw());
    }

    public int getCombatLevel() {
        if (getRaw() == null) {
            return -1;
        }
        return (int) getFieldValue("CombatLevel", getRaw());
    }

}