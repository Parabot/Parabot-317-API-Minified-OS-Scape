package org.parabot.osscape.api.methods;

import org.parabot.osscape.accessors.Widget;
import org.parabot.osscape.api.wrapper.Interface;
import org.parabot.osscape.api.wrapper.InterfaceChild;

/**
 * @author JKetelaar
 */
public class Interfaces {

    public static Interface[] get() {
        Widget[][]  widgets    = Game.getWidgets();
        Interface[] interfaces = new Interface[widgets.length];

        for (int i = 0; i < widgets.length; i++) {
            new Interface(widgets[i], i);
        }

        return interfaces;
    }

    public static Interface get(int parent) {
        return get()[parent];
    }

    public static InterfaceChild get(int parent, int child) {
        return get(parent).getChild(child);
    }

    public static InterfaceChild searchForText(String search, boolean contains, boolean strict) {
        for (Interface i : get()) {
            for (InterfaceChild interfaceChild : i.getChildren()) {
                String text = interfaceChild.getText();
                if (!strict) {
                    text = text.toLowerCase();
                    search = search.toLowerCase();
                }

                if (contains) {
                    if (text.contains(search)) {
                        return interfaceChild;
                    }
                } else {
                    if (text.equals(search)) {
                        return interfaceChild;
                    }
                }
            }
        }

        return null;
    }

    public static InterfaceChild searchForText(String text, boolean contains) {
        return searchForText(text, contains, false);
    }

    public static InterfaceChild searchForText(String text) {
        return searchForText(text, false);
    }

    public static boolean hasAction(InterfaceChild child, String action, boolean strict) {
        if (child.getWidgetActions() != null) {
            for (String s : child.getWidgetActions()) {
                if (!strict && s.equalsIgnoreCase(action)) {
                    return true;
                } else if (strict && s.equals(action)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasAction(InterfaceChild child, String action) {
        return hasAction(child, action, false);
    }

    public static boolean canEnterAmount() {
        InterfaceChild child = get(162, 32);

        return child != null && child.isVisible();
    }
}
