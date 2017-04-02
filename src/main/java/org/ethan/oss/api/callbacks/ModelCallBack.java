package org.ethan.oss.api.callbacks;

import org.ethan.oss.api.wrappers.Model;
import org.parabot.environment.api.utils.Timer;

import java.util.Hashtable;

public class ModelCallBack {

    private static Hashtable<Object, Model> modelCache = new Hashtable<>();

    private static Timer timer = new Timer(5 * 60 * 1000);

    public static void add(Model model, Object render) {
        if (render != null && model != null && model.isValid()) {
            if (!timer.isRunning()) {
                modelCache.clear();
                timer = new Timer(5 * 60 * 1000);
            }
            if (modelCache.containsKey(render)) {
                modelCache.remove(render);
            }
            modelCache.put(render, model);
        }
    }

    public static Model get(Object render) {
        if (render == null) {
            return null;
        }
        return modelCache.get(render);
    }

}