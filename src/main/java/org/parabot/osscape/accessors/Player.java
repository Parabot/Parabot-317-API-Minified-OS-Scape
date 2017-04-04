package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface Player extends Actor {
    Object getDefinition();

    String getPlayerName();

    int getCombatLevel();
}
