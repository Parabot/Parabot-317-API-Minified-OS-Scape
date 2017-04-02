package org.ethan.oss.api.wrappers;

import org.ethan.oss.api.definitions.NPCDefinition;
import org.ethan.oss.interfaces.Identifiable;
import org.ethan.oss.interfaces.Nameable;

public class NPC extends Actor implements Identifiable, Nameable {

    private NPCDefinition npcDefinition;

    public NPC(Object raw) {
        super(raw);
        if (raw != null) {
            this.npcDefinition = new NPCDefinition(getFieldValue("NpcDef", raw));
        }
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