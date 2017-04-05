package org.parabot.osscape.api.methods;

import org.ethan.oss.api.interactive.Camera;
import org.ethan.oss.api.methods.Walking;
import org.parabot.osscape.api.wrapper.Tile;
import org.parabot.core.Context;
import org.parabot.environment.api.utils.Timer;
import org.parabot.osscape.api.interfaces.Locatable;
import org.parabot.osscape.api.wrapper.Player;

import java.applet.Applet;
import java.awt.*;

public class Calculations {

    public static int[]     SINE             = new int[2048];
    public static int[]     COSINE           = new int[2048];
    public static Rectangle RESIZED_VIEWPORT = new Rectangle(5, 5, 509, 332);

    static {
        for (int i = 0; i < SINE.length; i++) {
            SINE[i] = ((int) (65536.0D * Math.sin(i * 0.0030679615D)));
            COSINE[i] = ((int) (65536.0D * Math.cos(i * 0.0030679615D)));
        }
    }

    public static int angleToTile(Tile t) {
        Tile me = Players.getMyPlayer().getLocation();

        int angle = (int) Math.toDegrees(Math.atan2(t.getY() - me.getY(), t.getX() - me.getX()));
        return angle >= 0 ? angle : 360 + angle;
    }

    public static int distanceBetween(int x, int y, int x1, int y1) {
        return (int) Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
    }

    public static int distanceBetween(Point a, Point b) {
        return (int) distanceBetween(a.x, a.y, b.x, b.y);
    }

    public static int distanceBetween(Tile a, Tile b) {
        return (int) distanceBetween(a.getX(), a.getY(), b.getX(), b.getY());
    }

    public static int distanceTo(Tile a) {
        final Tile loc = Players.getMyPlayer().getLocation();
        return (int) distanceBetween(a.getX(), a.getY(), loc.getX(), loc.getY());
    }

    public static Point tileToMap(Tile tile) {
        int    xMapTile = tile.getX() - Game.getBaseX();
        int    yMapTile = tile.getY() - Game.getBaseY();
        Player myPlayer = Players.getMyPlayer();

        return worldToMap((xMapTile * 4 + 2) - myPlayer.getLocalX() / 32,
                (yMapTile * 4 + 2) - myPlayer.getLocalY() / 32);
    }

    public static int distanceTo(Locatable a) {
        final Tile loc = Players.getMyPlayer().getLocation();
        return distanceBetween(a.getLocation().getX(), a.getLocation().getY(), loc.getX(), loc.getY());
    }

    public static Point worldToMap(int regionX, int regionY) {
        int mapScale  = Game.getMapScale();
        int mapOffset = Game.getMapOffset();
        int angle     = Game.getMapAngle();
        int i         = mapOffset + angle & 2047;
        int j         = regionX * regionX + regionY * regionY;

        if (j <= 6400) {
            int sin = Calculations.SINE[i] * 256 / (mapScale + 256);
            int cos = Calculations.COSINE[i] * 256 / (mapScale + 256);

            int xMap = regionY * sin + regionX * cos >> 16;
            int yMap = regionY * cos - regionX * sin >> 16;

            return new Point(644 + xMap, 85 - yMap);
        }
        return new Point(-1, -1);
    }

    public static final Point worldToCanvas(int regionX, int regionY, int regionZ) {
        if ((regionY < 128) || (regionZ < 128) || (regionY > 13056) || (regionZ > 13056)) {
            return null;
        }
        int i = getTileHeight(Game.getPlane(), regionY, regionZ) - regionX;

        regionX -= Camera.getX();
        i -= Camera.getZ();
        regionY -= Camera.getY();
        int j  = SINE[Camera.getPitch2()];
        int k  = COSINE[Camera.getPitch2()];
        int m  = SINE[Camera.getYaw()];
        int n  = COSINE[Camera.getYaw()];
        int i1 = regionZ * m + regionY * n >> 16;
        regionZ = regionZ * n - regionY * m >> 16;
        regionY = i1;
        i1 = i * k - regionZ * j >> 16;
        regionZ = i * j + regionZ * k >> 16;
        i = i1;
        if (regionZ >= 50) {
            int scale  = Game.getCameraScale();
            int width  = Game.getViewportWidth();
            int height = Game.getViewportHeight();
            int i2     = regionY * scale / regionZ + width / 2;
            int i3     = scale * i1 / regionZ + height / 2;
            return new Point(i2, i3);
        }
        return null;
    }

    public static final Point tileToCanvas(int regionX, int regionY, int regionZ) {
        if ((regionX < 128) || (regionY < 128) || (regionX > 13056) || (regionY > 13056)) {
            return null;
        }
        int i = getTileHeight(Game.getPlane(), regionX, regionY) - regionZ;
        regionX -= Camera.getX();
        i -= Camera.getZ();
        regionY -= Camera.getY();
        int j  = SINE[Camera.getPitch2()];
        int k  = COSINE[Camera.getPitch2()];
        int m  = SINE[Camera.getYaw()];
        int n  = COSINE[Camera.getYaw()];
        int i1 = regionY * m + regionX * n >> 16;
        regionY = regionY * n - regionX * m >> 16;
        regionX = i1;
        i1 = i * k - regionY * j >> 16;
        regionY = i * j + regionY * k >> 16;
        i = i1;
        if (regionY >= 50) {
            int scale  = Game.getCameraScale();
            int width  = Game.getViewportWidth();
            int height = Game.getViewportHeight();
            int i2     = regionX * scale / regionY + width / 2;
            int i3     = scale * i1 / regionY + height / 2;
            return new Point(i2, i3);
        }
        return null;
    }

    public static final int getTileHeight(int regionX, int regionY, int regionZ) {
        int i = regionY >> 7;
        int j = regionZ >> 7;
        if ((i < 0) || (j < 0) || (i > 103) || (j > 103)) {
            return 0;
        }
        int              k       = regionX;
        final byte[][][] meta    = Game.getTileSettings();
        final int[][][]  heights = Game.getTileHeights();
        if ((k < 3) && ((meta[1][i][j] & 0x2) == 2)) {
            k++;
        }
        int m = heights[k][i][j] * (128 - (regionY & 0x7F)) + heights[k][(i + 1)][j] * (regionY & 0x7F) >> 7;
        int n = heights[k][i][(j + 1)] * (128 - (regionY & 0x7F))
                + heights[k][(i + 1)][(j + 1)] * (regionY & 0x7F) >> 7;
        return m * (128 - (regionZ & 0x7F)) + n * (regionZ & 0x7F) >> 7;
    }

    public static void drawViewport(Graphics2D g, Color color) {
        g.setColor(color);
        g.drawRect(RESIZED_VIEWPORT.x, RESIZED_VIEWPORT.y, RESIZED_VIEWPORT.width, RESIZED_VIEWPORT.height);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g.fillRect(RESIZED_VIEWPORT.x, RESIZED_VIEWPORT.y, RESIZED_VIEWPORT.width, RESIZED_VIEWPORT.height);
    }

    public static Point tileToCanvas(int regionX, int regionY, double paramDouble1, double paramDouble2, int regionZ) {
        return tileToCanvas((int) ((regionX - Game.getBaseX() + paramDouble1) * 128.0D),
                (int) ((regionY - Game.getBaseY() + paramDouble2) * 128.0D), regionZ);
    }

    public static Point tileToCanvas(Tile paramGameTile, double paramDouble1, double paramDouble2, int paramInt) {
        return tileToCanvas((int) ((paramGameTile.getX() - Game.getBaseX() + paramDouble1) * 128.0D),
                (int) ((paramGameTile.getY() - Game.getBaseY() + paramDouble2) * 128.0D), paramInt);
    }

    public static Dimension dimensions() {
        final Applet applet = (Applet) Context.getInstance().getApplet();
        return applet != null ? new Dimension(applet.getWidth(), applet.getHeight()) : new Dimension(-1, -1);
    }

    public static boolean isOnMap(Tile tile) {
        return Calculations.distanceTo(tile) < 16;
    }


    /**
     * @param startX       the startX (0 < startX < 104)
     * @param startY       the startY (0 < startY < 104)
     * @param destX        the destX (0 < destX < 104)
     * @param destY        the destY (0 < destY < 104)
     * @param findAdjacent if it's an object, it will find path which touches it.
     * @return The distance of the shortest path to the destination; or -1 if no valid path to the destination was
     * found.
     */
    public static int dijkstraDist(final int startX, final int startY, final int destX, final int destY, final boolean findAdjacent) {
        try {
            final int[][] prev = new int[104][104];
            final int[][] dist = new int[104][104];
            final int[] path_x = new int[4000];
            final int[] path_y = new int[4000];
            for (int xx = 0; xx < 104; xx++) {
                for (int yy = 0; yy < 104; yy++) {
                    prev[xx][yy] = 0;
                    dist[xx][yy] = 99999999;
                }
            }
            int curr_x = startX;
            int curr_y = startY;
            prev[startX][startY] = 99;
            dist[startX][startY] = 0;
            int path_ptr = 0;
            int step_ptr = 0;
            path_x[path_ptr] = startX;
            path_y[path_ptr++] = startY;
            final int blocks[][] = Walking.getCollisionFlags(Game.getPlane());
            final int pathLength = path_x.length;
            boolean foundPath = false;
            while (step_ptr != path_ptr) {
                curr_x = path_x[step_ptr];
                curr_y = path_y[step_ptr];
                if (Math.abs(curr_x - destX) + Math.abs(curr_y - destY) == (findAdjacent ? 1 : 0)) {
                    foundPath = true;
                    break;
                }
                step_ptr = (step_ptr + 1) % pathLength;
                final int cost = dist[curr_x][curr_y] + 1;
                // south
                if (curr_y > 0 && prev[curr_x][curr_y - 1] == 0 && (blocks[curr_x][curr_y - 1] & 0x1280102) == 0) {
                    path_x[path_ptr] = curr_x;
                    path_y[path_ptr] = curr_y - 1;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x][curr_y - 1] = 1;
                    dist[curr_x][curr_y - 1] = cost;
                }
                // west
                if (curr_x > 0 && prev[curr_x - 1][curr_y] == 0 && (blocks[curr_x - 1][curr_y] & 0x1280108) == 0) {
                    path_x[path_ptr] = curr_x - 1;
                    path_y[path_ptr] = curr_y;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x - 1][curr_y] = 2;
                    dist[curr_x - 1][curr_y] = cost;
                }
                // north
                if (curr_y < 104 - 1 && prev[curr_x][curr_y + 1] == 0 && (blocks[curr_x][curr_y + 1] &
                        0x1280120) == 0) {
                    path_x[path_ptr] = curr_x;
                    path_y[path_ptr] = curr_y + 1;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x][curr_y + 1] = 4;
                    dist[curr_x][curr_y + 1] = cost;
                }
                // east
                if (curr_x < 104 - 1 && prev[curr_x + 1][curr_y] == 0 && (blocks[curr_x + 1][curr_y] &
                        0x1280180) == 0) {
                    path_x[path_ptr] = curr_x + 1;
                    path_y[path_ptr] = curr_y;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x + 1][curr_y] = 8;
                    dist[curr_x + 1][curr_y] = cost;
                }
                // south west
                if (curr_x > 0 && curr_y > 0 && prev[curr_x - 1][curr_y - 1] == 0 && (blocks[curr_x - 1][curr_y - 1] &
                        0x128010E) == 0 && (blocks[curr_x - 1][curr_y] & 0x1280108) == 0 && (blocks[curr_x
                        ][curr_y - 1] & 0x1280102) == 0) {
                    path_x[path_ptr] = curr_x - 1;
                    path_y[path_ptr] = curr_y - 1;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x - 1][curr_y - 1] = 3;
                    dist[curr_x - 1][curr_y - 1] = cost;
                }
                // north west
                if (curr_x > 0 && curr_y < 104 - 1 && prev[curr_x - 1][curr_y + 1] == 0 && (blocks[curr_x - 1][curr_y + 1] & 0x1280138) == 0 && (blocks[curr_x - 1][curr_y] & 0x1280108) ==
                        0 && (blocks[curr_x][curr_y + 1] & 0x1280120) == 0) {
                    path_x[path_ptr] = curr_x - 1;
                    path_y[path_ptr] = curr_y + 1;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x - 1][curr_y + 1] = 6;
                    dist[curr_x - 1][curr_y + 1] = cost;
                }
                // south east
                if (curr_x < 104 - 1 && curr_y > 0 && prev[curr_x + 1][curr_y - 1] == 0 && (blocks[curr_x +
                        1][curr_y - 1] & 0x1280183) == 0 && (blocks[curr_x + 1][curr_y] & 0x1280180) == 0 && (blocks[curr_x][curr_y - 1] & 0x1280102) == 0) {
                    path_x[path_ptr] = curr_x + 1;
                    path_y[path_ptr] = curr_y - 1;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x + 1][curr_y - 1] = 9;
                    dist[curr_x + 1][curr_y - 1] = cost;
                }
                // north east
                if (curr_x < 104 - 1 && curr_y < 104 - 1 && prev[curr_x + 1][curr_y + 1] == 0 && (blocks[curr_x
                        + 1][curr_y + 1] & 0x12801E0) == 0 && (blocks[curr_x + 1][curr_y] & 0x1280180) == 0 && (blocks[curr_x][curr_y + 1] & 0x1280120) == 0) {
                    path_x[path_ptr] = curr_x + 1;
                    path_y[path_ptr] = curr_y + 1;
                    path_ptr = (path_ptr + 1) % pathLength;
                    prev[curr_x + 1][curr_y + 1] = 12;
                    dist[curr_x + 1][curr_y + 1] = cost;
                }
            }

            return foundPath ? dist[curr_x][curr_y] : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Uses Dijkstra path finding algorithm to find shortest path returns false if path isn't found.
     *
     * @param from Start Tile.
     * @param to   Destination Tile.
     * @return True if Path is found.
     */
    public static boolean foundPath(Tile from, Tile to) {
        return dijkstraDist(from.getX(), from.getY(), to.getX(), to.getY(), false) != -1;
    }

    /**
     * Uses Dijkstra path finding algorithm to find shortest path returns false if path isn't found.
     *
     * @param from     Start Tile.
     * @param to       Destination Tile.
     * @param isObject Is destination an  Object.
     * @return True if Path is found.
     */
    public static boolean foundPath(Tile from, Tile to, boolean isObject) {
        return dijkstraDist(from.getX(), from.getY(), to.getX(), to.getY(), isObject) != -1;
    }

    /**
     * Gets the Shortest Path distance between two tiles.
     *
     * @param from Start Tile.
     * @param to   Destination Tile.
     * @return Shortest Path distance between two tiles using Dijkstra algorithm, returns -1 if path isn't found.
     */
    public static int pathDistanceBetween(Tile from, Tile to) {
        if (foundPath(from, to)) {
            return dijkstraDist(from.getX(), from.getY(), to.getX(), to.getY(), false);
        }

        return -1;
    }

    /**
     * Gets the Shortest Path distance between two tiles.
     *
     * @param from     Start Tile.
     * @param to       Destination Tile.
     * @param isObject Is destination an Object.
     * @return Shortest Path distance between two tiles using Dijkstra algorithm, returns -1 if path isn't found.
     */
    public static int pathDistanceBetween(Tile from, Tile to, boolean isObject) {
        if (foundPath(from, to)) {
            return dijkstraDist(from.getX(), from.getY(), to.getX(), to.getY(), isObject);
        }

        return -1;
    }

    /**
     * Gets the shortest distance to a tile.
     *
     * @param tile Destination Tile.
     * @return Shortest distance to tile.
     */
    public static int pathDistanceTo(Tile tile) {
        return pathDistanceBetween(Players.getMyPlayer().getLocation(), tile);
    }

    /**
     * Gets the shortest distance to a tile.
     *
     * @param tile     Destination Tile.
     * @param isObject Is destination an Object.
     * @return Shortest distance to tile.
     */
    public static int pathDistanceTo(Tile tile, boolean isObject) {
        return pathDistanceBetween(Players.getMyPlayer().getLocation(), tile, isObject);
    }

    /**
     * Compares Coordinate data from the first and second tile data.
     *
     * @param first  First tile.
     * @param second Second tile.
     * @return True if Coordinate data from first and second tiles match.
     */
    public static boolean isSameTile(Tile first, Tile second) {
        return distanceBetween(first, second) == 0 && first.getZ() == second.getZ();
    }

    /**
     * Checks if player is at the destination tile.
     *
     * @param destination destination tile.
     * @return true if players location equals destination tile.
     */
    public static boolean atTile(Tile destination) {
        return isSameTile(Players.getMyPlayer().getLocation(), destination);
    }

    /**
     * Get's the difference between start and current.
     *
     * @param start   Starting value.
     * @param current Current value.
     * @return difference between start and current.
     */
    public static int gained(int start, int current) {
        return start - current;
    }

    /**
     * Get's the hourly difference between start and current.
     *
     * @param runtime Timer used for calculating hourly difference.
     * @param start   Starting value.
     * @param current Current value.
     * @return Hourly difference between start and current.
     */
    public static int gainedPerHour(Timer runtime, int start, int current) {
        return runtime.getPerHour(gained(start, current));
    }
}