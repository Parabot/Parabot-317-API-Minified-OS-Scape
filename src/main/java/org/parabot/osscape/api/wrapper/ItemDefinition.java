package org.parabot.osscape.api.wrapper;

import org.parabot.core.Context;
import org.parabot.core.asm.wrappers.Invoker;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.parsers.hooks.XMLHookParser;
import org.parabot.core.reflect.RefClass;
import org.parabot.core.reflect.RefMethod;
import org.parabot.osscape.accessors.ItemComposite;

import java.util.ArrayList;
import java.util.Hashtable;

public class ItemDefinition {

    private static Hashtable<Integer, ItemDefinition> cache = new Hashtable<>();

    private ItemComposite accessor;
    private int           id;

    public ItemDefinition(ItemComposite accessor, int id) {
        this.accessor = accessor;
        this.id = id;
    }

    /**
     * @param id
     *
     * @return
     */
    public static ItemDefinition getItemDefinition(int id) {
        ItemDefinition definition;
        if ((definition = cache.get(id)) == null) {
            definition = new ItemDefinition(getItemCompositeDefinition(id), id);
            cache.put(id, definition);
        }

        return definition;
    }

    private static ItemComposite getItemCompositeDefinition(int id) {
        HookParser parser = Context.getInstance().getHookParser();
        if (parser instanceof XMLHookParser) {
            XMLHookParser xmlHookParser = (XMLHookParser) parser;

            ArrayList<Invoker> invokers = xmlHookParser.getInvokersCache();
            for (Invoker invoker : invokers) {
                if (invoker.getInto().interfaces.size() == 1) {
                    if (invoker.getMethodName().equalsIgnoreCase("getItem")) {
                        try {
                            RefClass  itemClass  = new RefClass(Context.getInstance().getASMClassLoader().loadClass(invoker.getInto().name));
                            RefMethod itemMethod = null;

                            for (RefMethod method : itemClass.getMethods()) {
                                if (method.getParameterTypes().length == 1) {
                                    itemMethod = method;
                                }
                            }

                            if (itemMethod != null) {
                                Object o = itemMethod.invoke(id);

                                return (ItemComposite) o;
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return null;
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
}