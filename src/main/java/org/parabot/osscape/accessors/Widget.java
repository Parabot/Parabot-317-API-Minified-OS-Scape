package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface Widget {
    Widget[] getChildren();

    String[] getWidgetActions();

    String getText();

    String getWidgetName();

    int[] getWidgetStackSizes();

    int[] getInvIDs();

    int getScrollY();

    int getScrollX();

    int getRelativeY();

    int getRelativeX();

    int getWidgetID();

    int getWidgetWidth();

    int getWidgetHeight();

    int getAbsoluteY();

    int getAbsoluteX();

    int getWidgetType();

    int getWidgetItemID();

    int getScrollHeight();

    int getParentID();

    int getTextureID();

    int getItemAmount();

    int getBoundsIndex();

    boolean getIsHidden();
}
