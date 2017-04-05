package org.ethan.oss.api.wrappers;

import org.ethan.oss.api.definitions.NPCDefinition;
import org.ethan.oss.interfaces.Identifiable;
import org.ethan.oss.interfaces.Nameable;
import org.parabot.osscape.api.wrapper.Character;

public class NPC extends Character implements Identifiable, Nameable {

    private NPCDefinition npcDefinition;

    public NPC(Object raw) {
        super(null, 1);
    }

    public String getName() {
        return npcDefinition.getName();
    }

    public int getId() {
        return npcDefinition.getId();
    }

    public int getCombatLevel() {
        return npcDefinition.getCombatLevel();
    }

    public String[] getActions() {
        return npcDefinition.getActions();
    }

}