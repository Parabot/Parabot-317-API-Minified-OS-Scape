package org.parabot.osscape.api.wrapper.walking.pathfinder.impl;

import org.ethan.oss.api.methods.Walking;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;
import org.parabot.osscape.api.wrapper.walking.pathfinder.AStarPathFinder;
import org.parabot.osscape.api.wrapper.walking.pathfinder.ClosestHeuristic;
import org.parabot.osscape.api.wrapper.walking.pathfinder.Path;

import java.awt.*;

public class RSRegionPathFinder {

    public static final int LAZY = 0;
    public static final int FULL = 1;
    private AStarPathFinder pathFinder;

    public RSRegionPathFinder() {
        reload();
    }

    private void reload() {

        int[][] clips = Walking.getCollisionFlags(Game.getPlane());
        pathFinder = new AStarPathFinder(new RSRegion(Game.getPlane(), clips), 90, true, new ClosestHeuristic());
    }

    public Path getPath(int destX, int destY, int full) {
        reload();
        Point destPoint = getFixedPoint(destX, destY);
        return getPath(Game.getPlane(), Players.getMyPlayer().getLocalX() >> 7,
                Players.getMyPlayer().getLocalY() >> 7,
                destPoint.x,
                destPoint.y, full);
    }

    private Point getFixedPoint(int x, int y) {
        x = x - Game.getBaseX();
        y = y - Game.getBaseY();

        if (x > 103) {
            x = 103;
        } else if (x < 0) {
            x = 0;
        }

        if (y > 103) {
            y = 103;
        } else if (y < 0) {
            y = 0;
        }
        return new Point(x, y);
    }

    public Path getPath(int plane, int startX, int startY, int destX, int destY, int full) {
        return pathFinder.findPath(plane, startX, startY, destX, destY, full);
    }
}