package org.ethan.oss.api.pathfinder.impl;

import org.ethan.oss.api.interactive.Players;
import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.pathfinder.AStarPathFinder;
import org.ethan.oss.api.pathfinder.ClosestHeuristic;
import org.ethan.oss.api.pathfinder.Path;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class RSMapPathFinder {

    public static final int LAZY = 0;
    public static final int FULL = 1;
    private AStarPathFinder pathFinder;

    public RSMapPathFinder() {
        reload();
    }

    private void reload() {
        pathFinder = new AStarPathFinder(new RSMap(), 600, true, new ClosestHeuristic());
    }

    public Path getPath(int destX, int destY, int full) {
        reload();
        Point destPoint = new Point(destX, destY);
        return getPath(Game.getPlane(), Players.getLocal().getX(),
                Players.getLocal().getY(),
                destPoint.x,
                destPoint.y, full);
    }

    public Path getPath(int plane, int startX, int startY, int destX, int destY, int full) {
        return pathFinder.findPath(plane, startX, startY, destX, destY, full);
    }
}