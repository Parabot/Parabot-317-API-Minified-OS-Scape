package org.parabot.osscape.api.wrapper.walking.pathfinder;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 *
 * @author Kevin Glass
 */
public class ClosestHeuristic implements AStarHeuristic {
    /**
     * @see AStarHeuristic#getCost(TileBasedMap, int, int, int, int)
     */
    public float getCost(TileBasedMap map, int x, int y, int tx, int ty) {
        float dx = tx - x;
        float dy = ty - y;

        //return (float) (Math.sqrt((dx * dx) + (dy * dy)));
        return Math.abs(dx) + Math.abs(dy); // Total amount of steps required.
    }

}