package org.ethan.oss.api.enums;

import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.WidgetChild;

public enum Equipment {

    HEAD(6), CAPE(7), NECKLACE(8), ARROWS(1), WEAPON(9), PLATE(10), SHIELD(11), LEGS(12), GLOVES(13), BOOTS(14), RING(
            15);

    int child;

    private Equipment(int child) {
        this.child = child;
    }

    public int getChild() {
        return child;
    }

    public void getEquippedIds() {
        for (WidgetChild p : Widgets.get(387).getChildren()) {
            for (WidgetChild p1 : p.getChildren()) {
                if (p1.getChildren().length > 0) {
                    if (p1.getChild(1).getItemId() > 0) {
                        System.out.println(p1.getChild(1).getItemId());
                    }
                }
            }
        }
    }

}