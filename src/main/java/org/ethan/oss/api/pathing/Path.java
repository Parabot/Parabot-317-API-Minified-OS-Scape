package org.ethan.oss.api.pathing;

import org.parabot.osscape.api.methods.Players;
import org.ethan.oss.api.methods.Calculations;
import org.ethan.oss.api.methods.Walking;
import org.ethan.oss.api.wrappers.Tile;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;

public abstract class Path {

    private boolean end;

    public abstract Tile getStart();

    public abstract Tile getEnd();

    public abstract Tile[] getTiles();

    public boolean traverse(boolean useRun) {
        Tile[] tiles = getTiles();

        if (tiles[tiles.length - 1].distanceTo() < 5) {
            return true;
        }
        final Tile next    = getNext();
        final Tile endTile = tiles[tiles.length - 1];

        if (next.equals(endTile)) {
            if (Calculations.distanceTo(next) <= 1 || (end && Players.getMyPlayer().isMoving())) {
                return false;
            }
            end = true;
        } else {
            end = false;
        }
        Walking.walkTo(next, useRun);
        for (int i = 0; i < 10 && Players.getMyPlayer().isMoving(); i++, Condition.sleep(Random.nextInt(150, 200))) {
            ;
        }
        return true;
    }

    private Tile getNext() {
        Tile[] tiles = getTiles();
        for (int i = tiles.length - 1; i >= 0; --i) {
            if (Calculations.isOnMap(tiles[i])) {
                return tiles[i];
            }
        }
        return Walking.getClosestTileOnMap(tiles[tiles.length - 1]);
    }

    public Tile[] getReversedTiles() {
        Tile[] tiles = getTiles();
        if (tiles == null) {
            return null;
        }
        Tile[] t = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            t[tiles.length - 1 - i] = tiles[i];
        }
        return t;
    }

}