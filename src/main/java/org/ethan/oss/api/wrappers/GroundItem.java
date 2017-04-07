package org.ethan.oss.api.wrappers;

import org.parabot.osscape.api.wrapper.ItemDefinition;
import org.ethan.oss.api.input.Mouse;
import org.parabot.osscape.api.methods.Camera;
import org.parabot.osscape.api.wrapper.walking.Walking;
import org.ethan.oss.reflection.ReflWrapper;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.PolygonUtils;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.osscape.api.interfaces.Identifiable;
import org.parabot.osscape.api.interfaces.Interactable;
import org.parabot.osscape.api.interfaces.Locatable;
import org.parabot.osscape.api.interfaces.Nameable;
import org.parabot.osscape.api.methods.Calculations;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Menu;
import org.parabot.osscape.api.wrapper.Tile;

import java.awt.*;

public class GroundItem extends ReflWrapper implements Locatable, Identifiable, Nameable, Interactable {

    private int            id;
    private int            amount;
    private Tile           location;
    private ItemDefinition itemDefinition;
    private int            height;

    public GroundItem(Object raw, Tile tile) {
        if (raw != null) {

            location = tile;
            id = (int) getFieldValue("ItemID", raw);
            amount = (int) getFieldValue("StackSizes", raw);
            // TODO: Still null accessor
            itemDefinition = new ItemDefinition(null, id);
        } else {
            id = -1;
            amount = -1;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean interact(String action) {
        if (!this.isValid() || this == null) {
            return false;
        }
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 2000) {
            if (this.isOnScreen()) {
                Point ip = null;
                if (this.isValid()) {
                    ip = this.getPointOnScreen();
                }
                if (Mouse.getLocation().distance(ip) <= 5) {
                    ip = this.getPointOnScreen();
                    Mouse.move(ip);
                } else {
                    Mouse.move(ip);
                }

                if (this.isValid()) {
                    if (!Menu.isOpen() && Menu.contains(action, this.getName())) {

                        int index = Menu.index(action, this.getName());
                        if (index == 0) {
                            if (Menu.contains(action, this.getName())) {
                                Condition.sleep(Random.nextInt(20, 100));
                                Mouse.click(true);
                                return true;
                            }
                        } else {
                            Mouse.click(false);
                            Condition.wait(new Condition.Check() {
                                public boolean poll() {
                                    return Menu.isOpen();
                                }
                            }, 100, 20);
                        }
                    }
                    if (Menu.isOpen() && Menu.contains(action, this.getName())) {
                        int   index = Menu.index(action, this.getName());
                        Point p     = Menu.getSuitablePoint(index);
                        if (p.x > 5 && p.y > 5) {
                            Mouse.move(p.x, p.y);
                        }
                        if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
                            Condition.sleep(Random.nextInt(10, 100));
                            Mouse.click(true);
                            return true;
                        }
                    } else if (Menu.isOpen() && !Menu.contains(action, this.getName())) {
                        int   index = Menu.index("Cancel");
                        Point p     = Menu.getSuitablePoint(index);
                        if (p.x > 5 && p.y > 5) {
                            Mouse.move(p.x, p.y);
                        }
                        if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
                            Condition.sleep(Random.nextInt(10, 100));
                            Mouse.click(true);
                            return true;
                        }
                    }
                }
            } else {
                if (this.distanceTo() >= 8) {
                    Walking.walkTo(this.getLocation(), true);
                    continue;
                } else {
                    this.turnTo();
                    continue;
                }
            }
        }
        return false;

    }

    @Override
    public boolean interact(String action, String option) {
        return interact(action, option);
    }

    @Override
    public boolean click(boolean left) {

        Point   interactingPoint = this.getInteractPoint();
        Polygon bounds           = getBounds();
        for (int i = 0; i < 3; i++) {
            if (bounds == null || bounds.contains(Mouse.getLocation())) {
                Mouse.click(left);
                return true;
            }
            if (bounds == null || !bounds.contains(interactingPoint)) {
                interactingPoint = this.getInteractPoint();
            }
            Mouse.move(interactingPoint);
        }
        return false;
    }

    @Override
    public boolean click() {
        return click(true);
    }

    public Polygon getBounds() {
        int     i            = Game.getPlane();
        Point   localPoint1  = Calculations.tileToCanvas(this.getLocation(), 0.0D, 0.0D, i);
        Point   localPoint2  = Calculations.tileToCanvas(this.getLocation(), 1.0D, 0.0D, i);
        Point   localPoint3  = Calculations.tileToCanvas(this.getLocation(), 1.0D, 1.0D, i);
        Point   localPoint4  = Calculations.tileToCanvas(this.getLocation(), 0.0D, 1.0D, i);
        Polygon localPolygon = new Polygon();
        if (Utilities.inViewport(localPoint1) && Utilities.inViewport(localPoint2) && Utilities.inViewport(localPoint3)
                && Utilities.inViewport(localPoint4)) {
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
        return null;

    }
//
//	public Model getModel() {
//		try {
//			int x = this.getLocation().x - Game.getBaseX();
//			int y = this.getLocation().y - Game.getBaseY();
//			int z = Game.getPlane();
//			Object region = getFieldValue("Region", null);
//			Object[][][] tiles = (Object[][][]) getFieldValue("SceneTiles", region);
//			Object tile = tiles[z][x][y];
//
//			if (tile == null) {
//				System.out.println("TILE NULL");
//				return null;
//			}
//			Object layer = getFieldValue("GroundItemTiles", tile);
//			if (layer == null) {
//				System.out.println("LAYER NULL");
//				return null;
//			}
//			int gridX = (int) getFieldValue("GroundItemX", layer);
//			int gridY = (int) getFieldValue("GroundItemY", layer);
//			Object rend = null;
//			Object[] layers = { getFieldValue("TOP", layer), getFieldValue("MIDDLE", layer),
//					getFieldValue("BOTTOM", layer) };
//			 for (Object layer1 : layers) {
//		            if (layer1 == null) {
//		                continue;
//		            }
//		            rend = layer1;
//		            break;
//		        }
//
//		        if (rend == null) {
//		        	System.out.println("Rend == null");
//		            return null;
//		        }
//		        this.height = (int) getFieldValue("ModelHeight", rend);
//			return rend != null && ModelCallBack.get(rend) != null
//					? new Model(ModelCallBack.get(rend), 0, gridX, gridY, z) : null;
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		System.out.println("MODEL AT END NULL");
//		return null;
//	}

    public boolean instanceOf(Object first) {
        if (first == null) {
            return false;
        }
        try {
            Object obj = getFieldValue("ModelVertX", first);
            if (obj == null) {
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean isOnScreen() {
        if (location != null) {
            return location.isOnScreen();
        }
        return false;
    }

    @Override
    public Point getPointOnScreen() {
        Polygon bounds = getBounds();
        if (bounds != null) {
            return PolygonUtils.polygonCenterOfMass(bounds);
        }
        return new Point(-1, -1);
    }

    @Override
    public Point getInteractPoint() {
        return getPointOnScreen();
    }

    public int getHeight() {
        return this.height;
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
        return Calculations.distanceBetween(getLocation(), tile);
    }

    @Override
    public boolean turnTo() {
        return Camera.turnTo(this);
    }

    @Override
    public Tile getLocation() {
        return location;
    }

    @Override
    public void draw(Graphics2D g, Color color) {
        //	 getModel().draw(g, color);
    }

    @Override
    public void draw(Graphics2D g) {
        draw(g, Color.WHITE);
    }

    @Override
    public String getName() {
        return itemDefinition.getName();
    }

    public boolean isValid() {
        return (id) != -1;
    }

    public int getStackSize() {
        return amount;
    }

    @Override
    public boolean canReach() {

        return false;
    }
}
