package org.parabot.osscape.api.wrapper;

/**
 * @author JKetelaar
 */
public class Npc extends Character {

    private org.parabot.osscape.accessors.Npc accessor;

    public Npc(org.parabot.osscape.accessors.Npc accessor, int index) {
        super(accessor, index);
        this.accessor = accessor;
    }

    @Override
    public String getName() {
        return accessor.getNpcDef().getNpcName();
    }

    @Override
    public org.parabot.osscape.accessors.Npc getAccessor() {
        return accessor;
    }
}
