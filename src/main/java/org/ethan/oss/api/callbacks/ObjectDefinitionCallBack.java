package org.ethan.oss.api.callbacks;

import java.util.Hashtable;

public class ObjectDefinitionCallBack {

    private static Hashtable<Integer, Object> defcache = new Hashtable<>();

    public static void add(Object definition, int id) {
        if (definition != null) {
            if (defcache.containsKey(id)) {
                defcache.remove(id);
            }
            defcache.put(id, definition);
        }
    }

    public static Object get(int id) {
        return defcache.get(id);
    }

}