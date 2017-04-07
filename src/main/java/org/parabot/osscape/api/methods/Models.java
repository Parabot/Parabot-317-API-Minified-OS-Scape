package org.parabot.osscape.api.methods;

import org.parabot.osscape.accessors.Model;

import java.util.Hashtable;

/**
 * @author JKetelaar
 */
public class Models {

    private static Hashtable<Object, Model> modelCache = new Hashtable<>();

    private static long cacheTime = 0;

    public static void add(Model model, Object render) {
        if (render != null && model != null) {
            if (cacheTime < System.currentTimeMillis() - 5 * 60 * 1000) {
                modelCache.clear();
                cacheTime = System.currentTimeMillis();
            }

            if (modelCache.containsKey(render)) {
                modelCache.remove(render);
            }
            modelCache.put(render, model);
        }
    }

    public static Model get(Object render) {
        return modelCache.get(render);
    }
}
