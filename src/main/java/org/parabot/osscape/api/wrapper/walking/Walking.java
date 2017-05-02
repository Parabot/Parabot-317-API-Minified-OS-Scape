package org.parabot.osscape.api.wrapper.walking;

import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.osscape.api.interfaces.Locatable;
import org.parabot.osscape.api.interfaces.StatePredicate;
import org.parabot.osscape.api.methods.Calculations;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;
import org.parabot.osscape.api.wrapper.Tile;
import org.parabot.osscape.api.wrapper.walking.pathfinder.Path;
import org.parabot.osscape.api.wrapper.walking.pathfinder.impl.RSRegionPathFinder;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author JKetelaar
 */
public class Walking {
    public static final int FORWARDS  = 0;
    public static final int BACKWARDS = 1;

    private static final StatePredicate WALKING() {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Players.getMyPlayer().isMoving();
            }
        };
    }

    private static final StatePredicate WALKING(final Tile tile, final int distance) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Players.getMyPlayer().isMoving()
                        && Calculations.distanceBetween(tile, Players.getMyPlayer().getLocation()) > distance;
            }
        };
    }

    public static Tile[] reversePath(Tile[] path) {
        Tile temp;
        for (int start = 0, end = path.length - 1; start < end; start++, end--) {
            temp = path[start];
            path[start] = path[end];
            path[end] = temp;
        }
        return path;
    }

    public static Tile getCollisionOffset(final int plane) {
        return new Tile(0, 0, plane);
    }

    public static byte[][][] getTileFlags() {
        return Game.getTileSettings();
    }

    public static int[][] getCollisionFlags(int plane) {

//        final Object collisionMap = ((Object[]) getFieldValue("getCollisionMaps", null))[plane];
//        return (int[][]) getFieldValue("getFlags", collisionMap);

        return new int[0][];
    }

    public static Tile getClosestTileOnMap(Tile current) {
        if (!current.isOnMap()) {
            Tile loc  = Players.getMyPlayer().getLocation();
            Tile walk = new Tile((loc.getX() + current.getX()) / 2, (loc.getY() + current.getY()) / 2);
            return walk.isOnMap() ? walk : getClosestTileOnMap(walk);
        }
        return current;
    }

    public static boolean clickOnMap(Tile tile) {
        Point m = Calculations.tileToMap(tile);
        if (m.x != -1 || clickOnMap(getClosestTileOnMap(tile))) {
            Keyboard.getInstance().pressKey(KeyEvent.VK_CONTROL);
            Mouse.getInstance().click(m.x, m.y, true);
            Keyboard.getInstance().releaseKey(KeyEvent.VK_CONTROL);
            Utilities.sleepUntil(WALKING(), 600);
            if (Players.getMyPlayer().isMoving()) {
                Utilities.sleepWhile(WALKING(tile, 3), 7500);
                return true;
            }
        } else {
            System.out.println("Tile is off screen " + tile);
            System.out.println("Current position " + Players.getMyPlayer().getLocation());
        }
        return false;
    }

    public static void traverse(Tile[] path, int direction) {
        Tile target = direction == FORWARDS ? path[path.length - 1] : path[0];
        if (direction == BACKWARDS) {
            path = reversePath(path);
        }

        int attemptsMade = 0;
        while (Calculations.distanceBetween(Players.getMyPlayer().getLocation(), target) > 3 && attemptsMade < 10
                && !Thread.currentThread().isInterrupted()) {
            Tile next = nextTile(path, 10);
            if (next != null) {
                boolean status = clickOnMap(next);
                if (!status) {
                    attemptsMade++;
                }
            } else {
                Logger.addMessage("No next tile in path to " + target, false);
                break;
            }
            Time.sleep(10);
        }
        clickOnMap(target);
    }

    public static Tile nextTile(Tile[] path, int maxDist) {
        Tile cur = Players.getMyPlayer().getLocation();
        for (int i = path.length - 1; i >= 0; i--) {
            if (Calculations.distanceBetween(cur, path[i]) <= maxDist
                    && Calculations.distanceBetween(cur, path[path.length - 1]) > 3) {
                return path[i];
            }
        }
        return null;
    }

    public static void walkToLocal(int x, int y) {
        RSRegionPathFinder pf   = new RSRegionPathFinder();
        Path               path = pf.getPath(x, y, RSRegionPathFinder.FULL);
        if (path != null && path.getLength() != 0) {
            final Tile[] tiles = path.toTiles(1);
            traverse(tiles, 0);
        }
    }

    public static void walkToLocal(Tile tile) {
        walkToLocal(tile.getX(), tile.getY());
    }

    public static void walkToLocal(Locatable locatable) {
        walkToLocal(locatable.getLocation());
    }

    public static void walkTo(Locatable target, boolean useRun) {
        walkTo(target.getLocation(), useRun);
    }

    public static void walkTo(Tile target, boolean useRun) {
//        if (!Game.isLoggedIn()) {
//            return;
//        }
        if (useRun && !Game.isRunning()) {
            if (Game.getEnergy() >= Random.nextInt(10, 30)) {
                Game.enableRunning();
            }
        }
        Tile step = getClosestTileOnMap(target);

        if (step.isOnMap()) {
            step.clickOnMap();
        }
    }

    public static interface Flag {
        static final int WALL_NORTHWEST = 0x1;
        static final int WALL_NORTH     = 0x2;
        static final int WALL_NORTHEAST = 0x4;
        static final int WALL_EAST      = 0x8;
        static final int WALL_SOUTHEAST = 0x10;
        static final int WALL_SOUTH     = 0x20;
        static final int WALL_SOUTHWEST = 0x40;
        static final int WALL_WEST      = 0x80;

        static final int OBJECT_TILE = 0x100;

        static final int WALL_BLOCK_NORTHWEST = 0x200;
        static final int WALL_BLOCK_NORTH     = 0x400;
        static final int WALL_BLOCK_NORTHEAST = 0x800;
        static final int WALL_BLOCK_EAST      = 0x1000;
        static final int WALL_BLOCK_SOUTHEAST = 0x2000;
        static final int WALL_BLOCK_SOUTH     = 0x4000;
        static final int WALL_BLOCK_SOUTHWEST = 0x8000;
        static final int WALL_BLOCK_WEST      = 0x10000;

        static final int OBJECT_BLOCK     = 0x20000;
        static final int DECORATION_BLOCK = 0x40000;

        static final int WALL_ALLOW_RANGE_NORTHWEST = 0x400000;
        static final int WALL_ALLOW_RANGE_NORTH     = 0x800000;
        static final int WALL_ALLOW_RANGE_NORTHEAST = 0x1000000;
        static final int WALL_ALLOW_RANGE_EAST      = 0x2000000;
        static final int WALL_ALLOW_RANGE_SOUTHEAST = 0x4000000;
        static final int WALL_ALLOW_RANGE_SOUTH     = 0x8000000;
        static final int WALL_ALLOW_RANGE_SOUTHWEST = 0x10000000;
        static final int WALL_ALLOW_RANGE_WEST      = 0x20000000;

        static final int OBJECT_ALLOW_RANGE = 0x40000000;

        static final int BLOCKED = 0x1280100;
    }
}
