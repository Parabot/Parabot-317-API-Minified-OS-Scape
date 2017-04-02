package org.ethan.oss.api.pathfinder;

import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.wrappers.Tile;

import java.awt.*;
import java.util.ArrayList;

/**
 * A path determined by some path finding algorithm. A series of steps from
 * the starting location to the target location. This includes a step for the
 * initial location.
 *
 * @author Kevin Glass
 */
public class Path {
    /**
     * The list of steps building up this path
     */
    private ArrayList<Step> steps = new ArrayList<Step>();

    private boolean appendBase = false;

    /**
     * Create an empty path
     */
    public Path() {
        this(false);
    }

    public Path(boolean appendBase) {
        this.appendBase = appendBase;
    }

    /**
     * Get the length of the path, i.e. the number of steps
     *
     * @return The number of steps in this path
     */
    public int getLength() {
        return steps.size();
    }

    /**
     * Get the step at a given index in the path
     *
     * @param index The index of the step to retrieve. Note this should
     *              be >= 0 and < getLength();
     *
     * @return The step information, the position on the map.
     */
    public Step getStep(int index) {
        return steps.get(index);
    }

    /**
     * Get the x coordinate for the step at the given index
     *
     * @param index The index of the step whose x coordinate should be retrieved
     *
     * @return The x coordinate at the step
     */
    public int getX(int index) {
        return getStep(index).x;
    }

    /**
     * Get the y coordinate for the step at the given index
     *
     * @param index The index of the step whose y coordinate should be retrieved
     *
     * @return The y coordinate at the step
     */
    public int getY(int index) {
        return getStep(index).y;
    }

    /**
     * Append a step to the path.
     *
     * @param x The x coordinate of the new step
     * @param y The y coordinate of the new step
     */
    public void appendStep(int x, int y) {
        if (appendBase) {
            x = x + Game.getBaseX();
            y = y + Game.getBaseY();
        }
        steps.add(new Step(x, y));
    }

    /**
     * Prepend a step to the path.
     *
     * @param x The x coordinate of the new step
     * @param y The y coordinate of the new step
     */
    public void prependStep(int x, int y) {
        if (appendBase) {
            x = x + Game.getBaseX();
            y = y + Game.getBaseY();
        }
        steps.add(0, new Step(x, y));
    }

    /**
     * Check if this path contains the given step
     *
     * @param x The x coordinate of the step to check for
     * @param y The y coordinate of the step to check for
     *
     * @return True if the path contains the given step
     */
    public boolean contains(int x, int y) {
        if (appendBase) {
            x = x + Game.getBaseX();
            y = y + Game.getBaseY();
        }
        return steps.contains(new Step(x, y));
    }

    public Point[] toPoints() {
        return toPoints(1);
    }

    public Point[] toPoints(int step) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < steps.size(); i += step) {
            if (i >= steps.size()) {
                points.add(new Point(steps.get(steps.size() - 1).getX(), steps.get(steps.size() - 1).getY()));
                break;
            }
            points.add(new Point(steps.get(i).getX(), steps.get(i).getY()));
        }
        return points.toArray(new Point[points.size()]);
    }

    public Tile[] toTiles(int step) {
        ArrayList<Tile> points = new ArrayList<Tile>();
        for (int i = 0; i < steps.size(); i += step) {
            points.add(new Tile(steps.get(i).getX(), steps.get(i).getY()));
        }
        points.add(new Tile(steps.get(steps.size() - 1).getX(), steps.get(steps.size() - 1).getY()));
        return points.toArray(new Tile[points.size()]);
    }

    /**
     * A single step within the path
     *
     * @author Kevin Glass
     */
    public class Step {
        /**
         * The x coordinate at the given step
         */
        private int x;
        /**
         * The y coordinate at the given step
         */
        private int y;

        /**
         * Create a new step
         *
         * @param x The x coordinate of the new step
         * @param y The y coordinate of the new step
         */
        public Step(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Get the x coordinate of the new step
         *
         * @return The x coodindate of the new step
         */
        public int getX() {
            return x;
        }

        /**
         * Get the y coordinate of the new step
         *
         * @return The y coodindate of the new step
         */
        public int getY() {
            return y;
        }

        /**
         * @see Object#hashCode()
         */
        public int hashCode() {
            return x * y;
        }

        /**
         * @see Object#equals(Object)
         */
        public boolean equals(Object other) {
            if (other instanceof Step) {
                Step o = (Step) other;

                return (o.x == x) && (o.y == y);
            }

            return false;
        }
    }
}