package org.ethan.oss.api.definitions;

import org.ethan.oss.api.callbacks.ObjectDefinitionCallBack;
import org.ethan.oss.reflection.ReflWrapper;

import java.util.Hashtable;

public class ObjectDefinition extends ReflWrapper {
    private static Hashtable<Integer, String>   nameCache    = new Hashtable<>();
    private static Hashtable<Integer, String[]> actionsCache = new Hashtable<>();

    private String   name;
    private String[] actions;

    public ObjectDefinition(int id) {
        if (nameCache.get(id) == null) {
            Object transformedComposite = null;
            Object raw                  = ObjectDefinitionCallBack.get(id);
            if (raw != null) {
                String name = (String) getFieldValue("getName21", raw);
                if (name == null || name.equalsIgnoreCase("null")) {
                    transformedComposite = ObjectDefinitionCallBack.get(id);

                }
                if (transformedComposite == null) {

                    transformedComposite = null;
                }
            }
            nameCache.put(id, (String) getFieldValue("getName21", transformedComposite == null ? raw : transformedComposite));
            actionsCache.put(id, (String[]) getFieldValue("getActions22", transformedComposite == null ? raw : transformedComposite));
        }

        if (nameCache.containsKey(id)) {
            name = nameCache.get(id);
            actions = actionsCache.get(id);
        }
    }

    public String getName() {
        return name;
    }

    public String[] getActions() {
        return actions;
    }

    public boolean isValid() {
        return name != null;
    }
}
