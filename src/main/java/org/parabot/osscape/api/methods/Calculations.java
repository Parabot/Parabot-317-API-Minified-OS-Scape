package org.parabot.osscape.api.methods;

import org.ethan.oss.api.interactive.Camera;
import org.ethan.oss.api.wrappers.Tile;
import org.parabot.core.Context;
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

}