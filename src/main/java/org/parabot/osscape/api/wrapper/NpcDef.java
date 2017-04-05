package org.parabot.osscape.api.wrapper;

import org.parabot.osscape.accessors.NpcDefinition;

/**
 * @author JKetelaar
 */
public class NpcDef {

    private NpcDefinition accessor;

    public NpcDef(NpcDefinition accessor) {
        this.accessor = accessor;
    }

    public String getName(){
        return accessor.getNpcName();
    }

    public String[] getActions(){
        return accessor.getNpcActions();
    }

    public int getCombatLevel(){
        return accessor.getNpcCombatLevel();
    }

    public int getId(){
        return accessor.getNpcID();
    }

    public NpcDefinition getAccessor() {
        return accessor;
    }
}
