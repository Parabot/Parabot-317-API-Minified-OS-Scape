package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface NpcDefinition {
    String getNpcName();

    String[] getNpcActions();

    int getNpcCombatLevel();

    int getNpcID();
}
