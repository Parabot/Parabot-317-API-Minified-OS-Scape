package org.ethan.oss.api.definitions;

import org.ethan.oss.reflection.ReflWrapper;

public class NPCDefinition extends ReflWrapper {

    private Object raw;

    public NPCDefinition(Object raw) {
        this.raw = raw;

    }

    public int getId() {
        if (raw == null) {
            return -1;
        }
        return (int) getFieldValue("NpcID", raw);
    }

    public int getCombatLevel() {
        if (raw == null) {
            return -1;
        }
        return (int) getFieldValue("NpcCombatLevel", raw);
    }

    public String getName() {
        if (raw == null) {
            return null;
        }
        return (String) getFieldValue("NpcName", raw);
    }

    public String[] getActions() {
        if (raw == null) {
            return null;
        }
        return (String[]) getFieldValue("NpcActions", raw);
    }

    public boolean isValid() {
        return raw != null;
    }
}