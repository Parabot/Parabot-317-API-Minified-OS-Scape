package org.parabot.osscape.api.wrapper;

import org.parabot.osscape.accessors.Actor;

/**
 * @author JKetelaar
 */
public class Player extends Character {
    public Player(Actor accessor, int index) {
        super(accessor, index);
    }

    @Override
    public String getName() {
        return ((org.parabot.osscape.accessors.Player) getAccessor()).getPlayerName();
    }

}
