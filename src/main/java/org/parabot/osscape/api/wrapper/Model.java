package org.parabot.osscape.api.wrapper;

import org.ethan.oss.utils.PolygonUtils;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.osscape.api.methods.Calculations;
import org.parabot.osscape.api.methods.Game;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author JKetelaar
 */
public class Model {

    private org.parabot.osscape.accessors.Model accessor;

    private int   orientation;
    private int[] orginal_x;
    private int[] orginal_z;
    private int[] trianglesX;
    private int[] trianglesZ;
    private int[] trianglesY;
    private int[] verticesX;
    private int[] verticesZ;
    private int[] verticesY;
    private int   gridX;
    private int   gridY;
    private int   z;

    public Model(org.parabot.osscape.accessors.Model accessor) {
        this.accessor = accessor;

        this.orientation = 0;
        this.gridY = 0;
        this.gridX = 0;
        this.z = 0;
        set(getVertX(), getVertY(), getVertZ(), getTriX(), getTriY(), getTriZ(),
                this.orientation);
    }

    public Model(org.parabot.osscape.accessors.Model accessor, int orientation, int x, int y, int z) {
        this.orientation = orientation;
        this.gridY = y;
        this.gridX = x;
        this.z = z;

        this.orientation = orientation;
        this.verticesX = accessor.getModelVertX();
        this.verticesY = accessor.getModelVertY();
        this.verticesZ = accessor.getModelVertZ();
        this.trianglesX = accessor.getModelTriX();
        this.trianglesY = accessor.getModelTriY();
        this.trianglesZ = accessor.getModelTriZ();

        if (orientation != 0) {
            orginal_x = new int[verticesX.length];
            orginal_z = new int[verticesZ.length];
            orginal_x = Arrays.copyOfRange(this.verticesX, 0, verticesX.length);
            orginal_z = Arrays.copyOfRange(this.verticesZ, 0, verticesZ.length);
            verticesX = new int[orginal_x.length];
            verticesZ = new int[orginal_z.length];
            int theta = orientation & 0x3fff;
            int sin   = Calculations.SINE[theta];
            int cos   = Calculations.COSINE[theta];
            for (int i = 0; i < orginal_x.length; ++i) {
                verticesX[i] = (orginal_x[i] * cos + orginal_z[i] * sin >> 15) >> 1;
                verticesZ[i] = (orginal_z[i] * cos - orginal_x[i] * sin >> 15) >> 1;
            }
        }
    }

    public void set(int[] verticesX, int[] verticesY, int[] verticesZ, int[] trianglesX, int[] trianglesY,
                    int[] trianglesZ, int orientation) {
        this.verticesX = new int[verticesX.length];
        this.verticesY = new int[verticesY.length];
        this.verticesZ = new int[verticesZ.length];
        this.trianglesX = new int[trianglesX.length];
        this.trianglesY = new int[trianglesY.length];
        this.trianglesZ = new int[trianglesZ.length];

        this.verticesX = Arrays.copyOfRange(verticesX, 0, verticesX.length);
        this.verticesY = Arrays.copyOfRange(verticesY, 0, verticesY.length);
        this.verticesZ = Arrays.copyOfRange(verticesZ, 0, verticesZ.length);
        this.trianglesX = Arrays.copyOfRange(trianglesX, 0, trianglesX.length);
        this.trianglesY = Arrays.copyOfRange(trianglesY, 0, trianglesY.length);
        this.trianglesZ = Arrays.copyOfRange(trianglesZ, 0, trianglesZ.length);

        this.orientation = orientation;

        if (orientation != 0) {
            orginal_x = new int[verticesX.length];
            orginal_z = new int[verticesZ.length];
            orginal_x = Arrays.copyOfRange(verticesX, 0, verticesX.length);
            orginal_z = Arrays.copyOfRange(verticesZ, 0, verticesZ.length);
            verticesX = new int[orginal_x.length];
            verticesZ = new int[orginal_z.length];
            rotate();
        }
    }

    public void rotate() {
        int theta = orientation & 0x3fff;
        int sin   = Calculations.SINE[theta];
        int cos   = Calculations.COSINE[theta];
        for (int i = 0; i < orginal_x.length; ++i) {
            verticesX[i] = (orginal_x[i] * cos + orginal_z[i] * sin >> 15) >> 1;
            verticesZ[i] = (orginal_z[i] * cos - orginal_x[i] * sin >> 15) >> 1;
        }
    }

    public void draw(Graphics2D graphics, Color color) {
        if (Game.isLoggedIn() && isOnScreen()) {
            graphics.setColor(color);
            Polygon[] tangles = getTriangles();
            for (Polygon triangle : tangles) {
                graphics.draw(triangle);
            }

            graphics.setColor(Color.YELLOW);

        }
    }

    public Polygon[] getTriangles() {
        LinkedList<Polygon> polygons = new LinkedList<>();

        int[] indices1 = getXTriangles();
        int[] indices2 = getYTriangles();
        int[] indices3 = getZTriangles();

        int[] xPoints = getXVertices();
        int[] yPoints = getYVertices();
        int[] zPoints = getZVertices();

        int len = indices1.length;

        for (int i = 0; i < len; ++i) {
            Point p1 = Calculations.tileToCanvas(gridX + xPoints[indices1[i]], gridY + zPoints[indices1[i]],
                    -yPoints[indices1[i]] + z);
            Point p2 = Calculations.tileToCanvas(gridX + xPoints[indices2[i]], gridY + zPoints[indices2[i]],
                    -yPoints[indices2[i]] + z);
            Point p3 = Calculations.tileToCanvas(gridX + xPoints[indices3[i]], gridY + zPoints[indices3[i]],
                    -yPoints[indices3[i]] + z);
            if (p1 != null && p2 != null && p3 != null) {
                if (p1.x >= 0 && p2.x >= 0 && p3.x >= 0) {
                    polygons.add(new Polygon(new int[]{ p1.x, p2.x, p3.x }, new int[]{ p1.y, p2.y, p3.y }, 3));
                }
            }
        }

        return polygons.toArray(new Polygon[polygons.size()]);
    }

    public int getTrianglesLength() {
        if (getXTriangles().length > 0) {
            return getXTriangles().length;
        }

        return -1;
    }

    public Point getRandomPoint() {
        Polygon[] triangles = getTriangles();
        if (triangles.length > 0) {
            for (int i = 0; i < 100; i++) {
                Polygon p = triangles[Random.nextInt(0, triangles.length)];
                Point point = new Point(p.xpoints[Random.nextInt(0, p.xpoints.length)],
                        p.ypoints[Random.nextInt(0, p.ypoints.length)]);
                if (Utilities.inViewport(point)) {
                    return point;
                }
            }
        }

        return new Point(-1, -1);
    }

    public Point getCenterPoint() {
        Polygon[] triangles = getTriangles();
        if (triangles.length > 0) {
            Polygon p     = triangles[Random.nextInt(0, triangles.length)];
            Point   point = PolygonUtils.polygonCenterOfMass(p);
            if (Utilities.inViewport(point)) {
                return point;
            }
        }

        return new Point(-1, -1);
    }

    public boolean contains(int x, int y) {
        for (final Polygon polygon : getTriangles()) {
            if (polygon.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    public int[] getXTriangles() {
        if (trianglesX.length > 0) {
            return trianglesX;
        }

        return new int[]{ -1 };
    }

    public int[] getYTriangles() {
        if (trianglesY.length > 0) {
            return trianglesY;
        }

        return new int[]{ -1 };
    }

    public int[] getZTriangles() {
        if (trianglesZ.length > 0) {
            return trianglesZ;
        }

        return new int[]{ -1 };
    }

    public int[] getXVertices() {
        if (verticesX.length > 0) {
            return verticesX;
        }
        return new int[]{ -1 };
    }

    public int[] getYVertices() {
        if (verticesY.length > 0) {
            return verticesY;
        }
        return new int[]{ -1 };
    }

    public int[] getZVertices() {
        if (verticesZ.length > 0) {
            return verticesZ;
        }
        return new int[]{ -1 };
    }

    public boolean isOnScreen() {
        return Utilities.inViewport(getRandomPoint());
    }

    private int[] getVertX() {
        int[] i = accessor.getModelVertX();
        if (i.length > 0) {
            return i;
        }
        return new int[]{ -1 };
    }

    private int[] getVertY() {
        int[] i = accessor.getModelVertY();
        if (i.length > 0) {
            return i;
        }
        return new int[]{ -1 };
    }

    private int[] getVertZ() {
        int[] i = accessor.getModelVertZ();
        if (i.length > 0) {
            return i;
        }
        return new int[]{ -1 };
    }

    private int[] getTriX() {
        int[] i = accessor.getModelTriX();
        if (i.length > 0) {
            return i;
        }
        return new int[]{ -1 };
    }

    private int[] getTriY() {
        int[] i = accessor.getModelTriY();
        if (i.length > 0) {
            return i;
        }
        return new int[]{ -1 };
    }

    private int[] getTriZ() {
        int[] i = accessor.getModelTriZ();
        if (i.length > 0) {
            return i;
        }
        return new int[]{ -1 };
    }
}
