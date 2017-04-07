package org.parabot.osscape.api.wrapper;

import org.parabot.osscape.accessors.Widget;
import org.parabot.osscape.api.methods.Interfaces;

/**
 * @author JKetelaar
 */
public class InterfaceChild {

    private Widget widget;
    private int    index;

    private int widgetX;
    private int widgetY;

    public InterfaceChild(Widget widget, int index) {
        this.widget = widget;
        this.index = index;
    }

    public Widget getWidget() {
        return widget;
    }

    public InterfaceChild[] getChildren() {
        Widget[]         widgets           = widget.getChildren();
        InterfaceChild[] interfaceChildren = new InterfaceChild[widgets.length];
        for (int i = 0; i < widgets.length; i++) {
            interfaceChildren[i] = new InterfaceChild(widgets[i], i);
        }

        return interfaceChildren;
    }

    public InterfaceChild getChild(int index) {
        InterfaceChild[] children = getChildren();
        return children[index];
    }

    public String[] getWidgetActions() {
        return widget.getWidgetActions();
    }

    public String getText() {
        return widget.getText();
    }

    public String getWidgetName() {
        return widget.getWidgetName();
    }

    public int[] getWidgetStackSizes() {
        return widget.getWidgetStackSizes();
    }

    public int[] getInvIDs() {
        return widget.getInvIDs();
    }

    public int getScrollY() {
        return widget.getScrollY();
    }

    public int getScrollX() {
        return widget.getScrollX();
    }

    public int getRelativeY() {
        return widget.getRelativeY();
    }

    public int getRelativeX() {
        return widget.getRelativeX();
    }

    public int getWidgetID() {
        return widget.getWidgetID();
    }

    public int getWidgetWidth() {
        return widget.getWidgetWidth();
    }

    public int getWidgetHeight() {
        return widget.getWidgetHeight();
    }

    public int getAbsoluteY() {
        return widget.getAbsoluteY();
    }

    public int getAbsoluteX() {
        return widget.getAbsoluteX();
    }

    public int getWidgetType() {
        return widget.getWidgetType();
    }

    public int getWidgetItemID() {
        return widget.getWidgetItemID();
    }

    public int getScrollHeight() {
        return widget.getScrollHeight();
    }

    public int getParentID() {
        return widget.getParentID();
    }

    public int getTextureID() {
        return widget.getTextureID();
    }

    public int getItemAmount() {
        return widget.getItemAmount();
    }

    public int getBoundsIndex() {
        return widget.getBoundsIndex();
    }

    public boolean getIsHidden() {
        return widget.getIsHidden();
    }

    public boolean isVisible() {
        if (getIsHidden()) {
            int parentId = getParentID();
            if (parentId == -1 || parentId == 0) {
                final InterfaceChild parent = Interfaces.get(parentId >> 16, parentId & 65535);
                return parent.isVisible();
            }
        }

        return false;
    }
}
