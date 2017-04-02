package org.ethan.oss.api.definitions;

import org.ethan.oss.reflection.ReflWrapper;

import java.util.Hashtable;

public class ItemDefinition extends ReflWrapper {

    private static Hashtable<Integer, String> cache = new Hashtable<>();

    private String name;

    public ItemDefinition(int id) {
        if (cache.get(id) == null) {
            Object raw  = invokeMethod("getItemName", id, null);
            String name = (String) getFieldValue("ItemName", raw);
            cache.put(id, name);
        }
        name = cache.get(id);
    }

    public String getName() {
        return name;
    }

    public boolean isValid() {
        return name != null;
    }
}