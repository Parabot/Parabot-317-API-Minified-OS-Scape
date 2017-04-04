package org.parabot.osscape.api.interfaces;

import org.ethan.oss.api.wrappers.Tile;

import java.awt.*;

public interface Locatable {

    public boolean isOnScreen();

    public Point getPointOnScreen();

    public int distanceTo();

    public int distanceTo(Locatable locatable);

    public int distanceTo(Tile tile);

    public boolean turnTo();

    public Tile getLocation();

    public void draw(Graphics2D g, Color color);

    public void draw(Graphics2D g);

    boolean canReach();

    public static interface Query<Q> {
        public Q within(final int radius);

        public Q at(final Tile tile);

        public Q nearest();
    }
}
