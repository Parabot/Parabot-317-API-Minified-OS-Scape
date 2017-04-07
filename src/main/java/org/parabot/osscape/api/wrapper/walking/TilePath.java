package org.parabot.osscape.api.wrapper.walking;

import org.parabot.osscape.api.methods.Calculations;
import org.parabot.osscape.api.wrapper.Tile;

import java.awt.*;

public final class TilePath {
    private Tile[] tiles;

    public TilePath(Tile[] tiles) {
        this.tiles = tiles;
    }

    /**
     * Gets this path's tiles
     *
     * @return path tiles
     */
    public final Tile[] getTiles() {
        return tiles;
    }

    /**
     * Gets next tile to walk to
     *
     * @return tile
     */
    public final Tile getNextTile() {
        Tile next = null;
        for (int x = 0; x < tiles.length; x++) {
            if (Calculations.isOnMap(tiles[x])) {
                next = tiles[x];
            }
        }
        return next;
    }

    /**
     * Determines if this path can be walked down
     *
     * @return <b>true</b> if path is valid
     */
    public final boolean isValid() {
        return getNextTile() != null;
    }

    /**
     * Determines if player has reached end of path
     *
     * @return <b>true</b> if player has reached end of path
     */
    public final boolean hasReached() {
        return tiles[tiles.length - 1].distanceTo() < 5;
    }

    /**
     * Walks down the path
     */
    public final void traverse(boolean enableRun) {
        final Tile next = getNextTile();
        if (next == null) {
            return;
        }
        Walking.walkTo(next, enableRun);
    }

    public final void traverse() {
        traverse(true);
    }

    /**
     * Draws the path
     *
     * @param graphics
     */
    public final void draw(final Graphics graphics) {
        for (int x = 0; x < tiles.length - 1; x++) {
            Point p = Calculations.tileToMap(tiles[x]);
            graphics.setColor(Color.red);
            graphics.fillRect(p.x - 2, p.y - 2, 4, 4);
            Point p1 = Calculations.tileToMap(tiles[x + 1]);
            graphics.fillRect(p1.x - 2, p1.y - 2, 4, 4);
            graphics.drawLine(p.x, p.y, p1.x, p1.y);
        }
    }

    /**
     * Reverses the current path
     *
     * @return The reversed path
     */
    public final TilePath reverse() {
        Tile[] newTiles = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            newTiles[i] = tiles[tiles.length - i - 1];
        }
        return new TilePath(newTiles);
    }
}