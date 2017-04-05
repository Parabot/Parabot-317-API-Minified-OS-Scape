package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.input.Mouse;
import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.Widget;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.parabot.osscape.api.methods.Game;

import java.awt.*;

public class WidgetDebugger extends Debugger<Widget> {
    Widget[] widgetCache = null;
    private long lastUpdate = System.currentTimeMillis();

    @Override
    public Widget[] elements() {
        if (widgetCache == null) {
            widgetCache = Widgets.get();
            return widgetCache;
        } else {
            return widgetCache;
        }
    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugWidgets() && Game.isLoggedIn();
    }

    @Override
    public void render(Graphics2D g) {

        for (Widget n : elements()) {
            if (n.isValid()) {
                for (WidgetChild c : n.getChildren()) {
                    if (c.getArea().contains(Mouse.getLocation())) {
                        if (c.getActions().length > 0 && Widgets.hasAction(c, "remove")) {
                            g.drawRect(c.getArea().x, c.getArea().y, c.getArea().width, c.getArea().height);
                            g.drawString(c.getParentIndex() + " " + c.getIndex(), c.getLocation().x, c.getLocation().y);
                        }
                        for (WidgetChild c1 : c.getChildren()) {
                            if (c1.getArea().contains(Mouse.getLocation())) {
                                if (c1.getActions().length > 0 && Widgets.hasAction(c1, "remove")) {
                                    g.drawRect(c1.getArea().x, c1.getArea().y, c1.getArea().width, c1.getArea().height);
                                    g.drawString(c1.getParentIndex() + " " + c1.getIndex(),
                                            c1.getLocation().x, c1.getLocation().y);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (System.currentTimeMillis() - lastUpdate > 8000) {
            lastUpdate = System.currentTimeMillis();
            widgetCache = Widgets.get();
        }
    }

}