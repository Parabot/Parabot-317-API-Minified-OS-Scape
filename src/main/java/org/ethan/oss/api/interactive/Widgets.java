package org.ethan.oss.api.interactive;

import org.ethan.oss.api.wrappers.Widget;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.ethan.oss.reflection.ReflWrapper;

public class Widgets extends ReflWrapper {

    public static Widget[] get() {
        Object[][] widgets = (Object[][]) getFieldValue("Widgets", null);
        if (widgets == null) {
            return new Widget[0];
        }
        Widget[] children = new Widget[widgets.length];
        for (int i = 0; i < widgets.length; i++) {
            children[i] = new Widget(widgets[i], i);
        }
        return children;
    }

    public static Widget get(int parent) {
        Object[][] widgets = (Object[][]) getFieldValue("Widgets", null);
        if (widgets.length == 0 || (widgets.length - 1) < parent || parent < 0) {
            return new Widget(null, parent);
        }
        return new Widget(widgets[parent], parent);
    }

    public static WidgetChild get(int parent, int child) {
        Widget widgets = get(parent);
        if (widgets == null) {
            return null;
        }
        return widgets.getChild(child);
    }

    public static WidgetChild getWidgetWithText(String text) {
        Object[][] widgets = (Object[][]) getFieldValue("Widgets", null);

        for (int parentIndex = 0; parentIndex < widgets.length; parentIndex++) {
            Object[] children = widgets[parentIndex];
            if (children == null) {
                continue;
            }
            for (int childIndex = 0; childIndex < children.length; childIndex++) {
                Object child = children[childIndex];
                if (child != null) {
                    String widgetText = (String) getFieldValue("Text", child);
                    if (widgetText != null && widgetText.contains(text)) {
                        return new WidgetChild(child, childIndex);
                    }
                }
            }
        }
        return null;
    }

    public static WidgetChild getWidgetWithAction(String text) {
        Object[][] widgets = (Object[][]) getFieldValue("Widgets", null);

        for (int parentIndex = 0; parentIndex < widgets.length; parentIndex++) {
            Object[] children = widgets[parentIndex];
            if (children == null) {
                continue;
            }
            for (int childIndex = 0; childIndex < children.length; childIndex++) {
                Object child = children[childIndex];
                if (child != null) {
                    String[] widgetText = (String[]) getFieldValue("WidgetActions", child);
                    if (widgetText != null && widgetText.length > 0) {
                        for (String s : widgetText) {
                            if (s != null && s.contains(text)) {
                                return new WidgetChild(child, childIndex);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean hasAction(WidgetChild w, String action) {
        String[] actions = w.getActions();
        for (String s : actions) {
            if (s != null && s.length() > 0) {
                if (s.equalsIgnoreCase(action)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean canEnterAmount() {
        WidgetChild widget = get(162, 32);

        return widget != null && widget.isVisible();
    }

    public static boolean canContinue() {
        WidgetChild widgetChild = getWidgetWithText("Click here to continue");
        return widgetChild != null && widgetChild.isVisible();
    }
}
