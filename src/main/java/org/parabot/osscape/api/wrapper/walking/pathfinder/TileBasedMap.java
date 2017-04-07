package org.parabot.osscape.api.wrapper.walking.pathfinder;

/**
 * Created by IntelliJ IDEA.
 * User: Johan
 * Date: 2010-feb-24
 * Time: 16:19:45
 * To change this template use File | Settings | File Templates.
 */
public interface TileBasedMap {
    /**
     * Get the width of the tile map. The slightly odd name is used
     * to distiguish this method from commonly used names in game maps.
     *
     * @return The number of tiles across the map
     */
    public int getWidthInTiles();

    /**
     * Get the height of the tile map. The slightly odd name is used
     * to distiguish this method from commonly used names in game maps.
     *
     * @return The number of tiles down the map
     */
    public int getHeightInTiles();

    /**
     * Notification that the path finder visited a given tile. This is
     * used for debugging new heuristics.
     *
     * @param x The x coordinate of the tile that was visited
     * @param y The y coordinate of the tile that was visited
     */
    public void pathFinderVisited(int plane, int x, int y);

    /**
     * Check if the given location is blocked, i.e. blocks movement of
     * the supplied mover.
     *
     * @param x The x coordinate of the tile to check
     * @param y The y coordinate of the tile to check
     *
     * @return True if the location is blocked
     */
    public boolean blocked(int plane, int x, int y, int direction);

    public boolean free(int plane, int x, int y);

    public boolean solid(int plane, int x, int y);

    public int getDirection(int plane, int x, int y);

    public int getBlock(int plane, int x, int y);

    public boolean isWalkable(int plane, int sx, int sy, int tx, int ty);

    /**
     * Get the cost of moving through the given tile. This can be used to
     * make certain areas more desirable. A simple and valid implementation
     * of this method would be to return 1 in all cases.
     *
     * @param sx The x coordinate of the tile we're moving from
     * @param sy The y coordinate of the tile we're moving from
     * @param tx The x coordinate of the tile we're moving to
     * @param ty The y coordinate of the tile we're moving to
     *
     * @return The relative cost of moving across the given tile
     */
    public float getCost(int plane, int sx, int sy, int tx, int ty);
}