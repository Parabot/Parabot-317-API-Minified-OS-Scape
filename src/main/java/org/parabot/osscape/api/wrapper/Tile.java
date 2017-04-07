package org.parabot.osscape.api.wrapper;

import org.ethan.oss.api.input.Mouse;
import org.parabot.osscape.api.methods.Camera;
import org.parabot.osscape.api.wrapper.walking.Walking;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.PolygonUtils;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.osscape.api.interfaces.Interactable;
import org.parabot.osscape.api.interfaces.Locatable;
import org.parabot.osscape.api.methods.Calculations;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Menu;
import org.parabot.osscape.api.methods.Players;

import java.awt.*;

public class Tile implements Locatable, Interactable {
    private int x;
    private int y;
    private int z;

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = Game.getPlane();
    }

    public static Tile derive(Tile t, int x, int y) {
        return derive(t, x, y, Game.getPlane());
    }

    public static Tile derive(Tile t, int x, int y, int plane) {
        return new Tile(t.getX() + x, t.getY() + y, t.getZ() + plane);
    }

    /**
     * @return Integer : X Coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return Integer : Y Coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @return Integer : Z Coordinate
     */
    public int getZ() {
        return z;
    }

    public Polygon getBounds() {
        int     i            = 0;
        Point   localPoint1  = Calculations.tileToCanvas(new Tile(this.x, this.y), 0.0D, 0.0D, i);
        Point   localPoint2  = Calculations.tileToCanvas(new Tile(this.x, this.y), 1.0D, 0.0D, i);
        Point   localPoint3  = Calculations.tileToCanvas(new Tile(this.x, this.y), 1.0D, 1.0D, i);
        Point   localPoint4  = Calculations.tileToCanvas(new Tile(this.x, this.y), 0.0D, 1.0D, i);
        Polygon localPolygon = new Polygon();
        if (localPoint1 != null) {
            localPolygon.addPoint(localPoint1.x, localPoint1.y);
        }
        if (localPoint2 != null) {
            localPolygon.addPoint(localPoint2.x, localPoint2.y);
        }
        if (localPoint3 != null) {
            localPolygon.addPoint(localPoint3.x, localPoint3.y);
        }
        if (localPoint4 != null) {
            localPolygon.addPoint(localPoint4.x, localPoint4.y);
        }
        return localPolygon;
    }

    @Override
    public boolean isOnScreen() {
        return Utilities.inViewport(getPointOnScreen());
    }

    @Override
    public Point getPointOnScreen() {
        Polygon bounds = getBounds();
        Point   p      = new Point(-1, -1);
        if (bounds == null || !Utilities.inViewport(PolygonUtils.polygonCenterOfMass(bounds))) {
            return p;
        } else {
            return PolygonUtils.polygonCenterOfMass(bounds);
        }

    }

    @Override
    public boolean equals(Object a) {
        if (a != null && a instanceof Tile) {
            Tile t = (Tile) a;
            return t.x == this.x && t.y == this.y;
        }
        return false;
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return (int) Calculations.distanceBetween(getLocation(), tile);
    }

    @Override
    public boolean turnTo() {
        return Camera.turnTo(this);
    }

    @Override
    public Tile getLocation() {
        return this;
    }

    @Override
    public void draw(Graphics2D g, Color color) {
        g.setColor(color);
        Polygon bounds = getBounds();
        if (bounds == null) {
            return;
        }
        g.drawPolygon(bounds);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g.fillPolygon(bounds);
    }

    @Override
    public void draw(Graphics2D g) {
        draw(g, Color.WHITE);
    }

    @Override
    public String toString() {
        return "Tile: [ X: " + getX() + ", Y: " + getY() + ", Z: " + getZ() + "]";
    }

    @Override
    public Point getInteractPoint() {
        return getPointOnScreen();
    }

    @Override
    public boolean interact(String action, String option) {
        int menuIndex = -1;
        for (int i = 0; i < 5; i++) {
            menuIndex = Menu.index(action, option);
            Point interactPoint = getInteractPoint();
            if (menuIndex > -1) {
                break;
            }
            if (Menu.isOpen() && menuIndex == -1) {
                Menu.interact("Cancel");
            }
            Mouse.move(interactPoint);
            Condition.sleep(Random.nextInt(100, 150));
        }
        return menuIndex > -1 && Menu.interact(action, option);
    }

    @Override
    public boolean interact(String action) {
        return interact(action, null);
    }

    @Override
    public boolean click(boolean left) {
        Mouse.click(left);
        return true;
    }

    @Override
    public boolean click() {
        Mouse.click(getInteractPoint(), true);
        return true;
    }

    public boolean canReach(Tile loc) {
        return Calculations.dijkstraDist(loc.x - Game.getBaseX(), loc.y - Game.getBaseY(), x - Game.getBaseX(), y - Game.getBaseY(),
                true) != -1;
    }

    public boolean canReach() {
        return canReach(Players.getMyPlayer().getLocation());
    }

    public boolean isOnMap() {
        return Calculations.isOnMap(this);
    }

    public void clickOnMap() {
        if (isOnMap()) {
            Mouse.click(getPointOnMap(), true);
            Condition.sleep(Random.nextInt(100, 150));
        }
    }

    public Point getPointOnMap() {

        return Calculations.tileToMap(this);
    }

    public boolean isWalkable() {
        int[][] flags = Walking.getCollisionFlags(Game.getPlane());
        int     value = flags[(getX() - Game.getBaseX())][(getY() - Game.getBaseY())];
        return (value & 0x1280180) == 0 ^ (value & 0x1280180) == 128;
    }
}
