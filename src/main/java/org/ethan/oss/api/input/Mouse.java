package org.ethan.oss.api.input;

import org.ethan.oss.ServerEngine;
import org.parabot.osscape.api.wrapper.Tile;

import java.awt.*;

public class Mouse {
    public static void move(int x, int y) {
        ServerEngine.getInstance().getMouse().move(x, y);
    }

    public static void move(Point p) {
        if (p == null) {
            return;
        }
        move(p.x, p.y);
    }

    public static int getPressX() {
        return ServerEngine.getInstance().getMouse().getPressX();
    }

    public static int getPressY() {
        return ServerEngine.getInstance().getMouse().getPressY();
    }

    public static boolean hover(Tile t) {
        Point p = t.getPointOnScreen();
        if (p != null) {
            move(p.x, p.y);
            return true;
        }
        return false;

    }

    public static long getPressTime() {
        return ServerEngine.getInstance().getMouse().getPressTime();
    }

    public static boolean isPressed() {
        return ServerEngine.getInstance().getMouse().isPressed();
    }

    public static Point getLocation() {
        return ServerEngine.getInstance().getMouse().getLocation();
    }

    public static void click(boolean right) {
        ServerEngine.getInstance().getMouse().click(right);
    }

    public static void click(Point p, boolean right) {
        move(p);
        click(right);
    }

    public static void press(int x, int y, int button) {
        ServerEngine.getInstance().getMouse().press(x, y, button);
    }

    public static void release(int x, int y, int button) {
        ServerEngine.getInstance().getMouse().release(x, y, button);
    }

    public static boolean dragMouse(Point p1, Point p2) {
        return dragMouse(p1.getLocation().x, p1.getLocation().y, p2.getLocation().x, p2.getLocation().y);
    }

    public static boolean dragMouse(int x, int y, int x1, int y1) {
        return ServerEngine.getInstance().getMouse().dragMouse(x, y, x1, y1);
    }
}