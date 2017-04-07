package org.parabot.osscape.api.wrapper;

import org.parabot.osscape.accessors.ItemComposite;

import java.util.Hashtable;

public class ItemDefinition {

    private static Hashtable<Integer, ItemDefinition> cache = new Hashtable<>();

    private ItemComposite accessor;
    private int id;

    public ItemDefinition(ItemComposite accessor, int id) {
        this.accessor = accessor;
        this.id = id;
    }

    public String getName() {
        return accessor.getItemName();
    }

    public int getId() {
        return id;
    }

    public ItemComposite getAccessor() {
        return accessor;
    }

    /**
     * // TODO: getAccessor is still null "new ItemDefinition(null, id)"
     *
     * @param id
     *
     * @return
     */
    public static ItemDefinition getItemDefinition(int id){
        ItemDefinition definition;
        if ((definition = cache.get(id)) == null){
            definition = new ItemDefinition(null, id);
            cache.put(id, definition);
        }

        return definition;
    }
}