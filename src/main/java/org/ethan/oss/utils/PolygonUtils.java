package org.ethan.oss.utils;

import java.awt.*;

/**
 * Thus is a utility class for performing useful functions for manipulating and
 * finding information about polygons, including computing areas, centroids,
 * testing whether a point is inside or outside of a polygon, and for rotating a
 * poloygon by a given angle.
 * <p>
 * <b>See Also</b> org.shodor.math11.Line
 */

public class PolygonUtils {

    /**
     * Computes the area of any two-dimensional polygon.
     *
     * @param polygon The polygon to compute the area of input as an array of points
     * @param N       The number of points the polygon has, first and last point
     *                inclusive.
     *
     * @return The area of the polygon.
     */
    public static double PolygonArea(Point[] polygon, int N) {

        int    i, j;
        double area = 0;

        for (i = 0; i < N; i++) {
            j = (i + 1) % N;
            area += polygon[i].x * polygon[j].y;
            area -= polygon[i].y * polygon[j].x;
        }

        area /= 2.0;
        return (Math.abs(area));
    }

    /**
     * Finds the centroid of a polygon with integer verticies.
     *
     * @param pg The polygon to find the centroid of.
     *
     * @return The centroid of the polygon.
     */

    public static Point polygonCenterOfMass(Polygon pg) {

        if (pg == null) {
            return null;
        }

        int     N       = pg.npoints;
        Point[] polygon = new Point[N];

        for (int q = 0; q < N; q++) {
            polygon[q] = new Point(pg.xpoints[q], pg.ypoints[q]);
        }

        double cx = 0, cy = 0;
        double A  = PolygonArea(polygon, N);
        int    i, j;

        double factor = 0;
        for (i = 0; i < N; i++) {
            j = (i + 1) % N;
            factor = (polygon[i].x * polygon[j].y - polygon[j].x * polygon[i].y);
            cx += (polygon[i].x + polygon[j].x) * factor;
            cy += (polygon[i].y + polygon[j].y) * factor;
        }
        factor = 1.0 / (6.0 * A);
        cx *= factor;
        cy *= factor;
        return new Point((int) Math.abs(Math.round(cx)), (int) Math.abs(Math
                .round(cy)));
    }

    /**
     * Rotates a polygon by an angle alpha. <b>The polygon is passed by
     * reference.</b>
     *
     * @param pg       The polygon to be rotated
     * @param rotAngle The rotation angle in <b>radians</b>.
     * @param centroid The centroid of the polygon.
     * @param original When rotating a polygon with a mouse, the original must be
     *                 maintained.
     */

    public static void rotatePolygon(Polygon pg, double rotAngle,
                                     Point centroid, Polygon original) {

        double x, y;
        for (int i = 0; i < pg.npoints; i++) {
            if (original != null) {
                x = original.xpoints[i] - centroid.x;
                y = original.ypoints[i] - centroid.y;
            } else {
                x = pg.xpoints[i] - centroid.x;
                y = pg.ypoints[i] - centroid.y;
            }
            pg.xpoints[i] = centroid.x
                    + (int) Math.round(((x * Math.cos(rotAngle)) - (y * Math
                    .sin(rotAngle))));
            pg.ypoints[i] = centroid.y
                    + (int) Math.round(((x * Math.sin(rotAngle)) + (y * Math
                    .cos(rotAngle))));
        }
    }

    /**
     * Rotates the polygon and returns it's center of mass.
     *
     * @param pg       The polygon to rotate around it's center of mass. Verticies
     *                 are integer points. Used mainly for physical coordinate
     *                 rotation.
     * @param rotangle -
     *                 Rotation angle in radians.
     */
    public static Point rotatePolygon(Polygon pg, double rotAngle) {

        double x, y;
        Point  centroid = polygonCenterOfMass(pg);
        for (int i = 0; i < pg.npoints; i++) {
            x = pg.xpoints[i] - centroid.x;
            y = pg.ypoints[i] - centroid.y;
            pg.xpoints[i] = centroid.x
                    + (int) Math.round(((x * Math.cos(rotAngle)) - (y * Math
                    .sin(rotAngle))));
            pg.ypoints[i] = centroid.y
                    + (int) Math.round(((x * Math.sin(rotAngle)) + (y * Math
                    .cos(rotAngle))));
        }
        return centroid;
    }

    /**
     * Polygon.contains(Point) seems to not consistantly return the right value,
     * so here is a rewrite.
     *
     * @param pg The polygon to test for insideness
     * @param p  The point to test for insideness.
     *
     * @return true if p is inside the polygon, <tt>pg</tt>,and false if it
     * is outside the polygon.
     */
    public static boolean insidePoly(Polygon pg, Point p) {
        double angle = 0;
        Point  p1    = null, p2 = null;
        for (int i = 0; i < pg.npoints; i++) {
            p1 = new Point(pg.xpoints[i] - p.x, pg.ypoints[i] - p.y);
            p2 = new Point(pg.xpoints[(i + 1) % pg.npoints] - p.x,
                    pg.ypoints[(i + 1) % pg.npoints] - p.y);
            angle += angle2D(p1, p2);
        }
        return Math.abs(angle) >= Math.PI;
    }

    public static double angle2D(Point p1, Point p2) {
        double dtheta = Math.atan2(p2.y, p2.x) - Math.atan2(p1.y, p1.x);
        while (dtheta > Math.PI) {
            dtheta -= 2.0 * Math.PI;
        }
        while (dtheta < -1.0 * Math.PI) {
            dtheta += 2.0 * Math.PI;
        }
        return dtheta;
    }

    public static boolean polygonsEqual(Polygon p1, Polygon p2) {
        if (p1 == null || p2 == null || p1.npoints != p2.npoints) {
            return false;
        }
        boolean equal = true;
        for (int i = 0; i < p1.xpoints.length; i++) {
            if (p1.xpoints[i] != p2.xpoints[i]
                    || p1.ypoints[i] != p2.ypoints[i]) {
                equal = false;
            }
        }
        return equal;
    }

    public static double getLength(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.x - p2.x), 2)
                + Math.pow((p1.y - p2.y), 2));
    }

    public static double lawOfCosines(double a, double b, double c) {
        return lawOfCosines(a, b, c, false);
    }

    public static double lawOfCosines(double a, double b, double c,
                                      boolean returnDegrees) {

        double arg = (a * a + b * b - c * c) / (2.0 * a * b);
        if (Math.abs(arg) > 1) {
            return 0.000001;
        }
        double interiorAngle = Math.abs(Math.acos(arg));
        return (returnDegrees ? 180.0 * interiorAngle / Math.PI : interiorAngle);
    }

}