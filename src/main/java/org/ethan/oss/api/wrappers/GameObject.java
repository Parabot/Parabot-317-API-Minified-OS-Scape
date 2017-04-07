package org.ethan.oss.api.wrappers;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.callbacks.ModelCallBack;
import org.ethan.oss.api.definitions.ObjectDefinition;
import org.ethan.oss.api.input.Mouse;
import org.parabot.osscape.api.methods.Camera;
import org.parabot.osscape.api.wrapper.walking.Walking;
import org.ethan.oss.reflection.ReflWrapper;
import org.ethan.oss.utils.Condition;
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

public class GameObject extends ReflWrapper implements Identifiable, Nameable, Locatable, Interactable {

    private Object           raw;
    private Type             type;
    private Tile             tile;
    private int              id;
    private ObjectDefinition objectDefinition;

    public GameObject(Object raw, Type type, int x, int y, int z) {
        this.raw = raw;
        this.type = type;
        this.tile = new Tile(x, y, z);
    }

    public Type getType() {
        return type;
    }

    @Override
    public int getId() {
        if (raw == null) {
            return 0;
        }
        if (id == 0) {
            if (type.cato.equals("GameObject")) {
                id = ((int) getFieldValue("getInteractiveID", raw) >> 14) & 0x7FFF;
            } else if (type.cato.equals("Boundary")) {
                id = ((int) getFieldValue("getBoundryID", raw) >> 14) & 0x7FFF;
            }

        }
        return id;
    }

    public int getX() {
        return tile.getX();
    }

    public int getY() {
        return tile.getY();
    }

    public boolean isValid() {
        return raw != null;
    }

    public Point point() {
        return point(getHeight());
    }

    public Point point(int paramInt) {
        return Calculations.worldToCanvas(paramInt, tile.getX(), tile.getY());
    }

    public Tile getLocation() {
        return tile;
    }

    public Object getRender() {
        if (type.cato.equals("GameObject")) {
            return getFieldValue("getInteractiveRender", raw);
        } else if (type.cato.equals("Boundary")) {
            return getFieldValue("getBoundryRender", raw);
        }
        return null;
    }

    public int getHeight() {
        if (raw == null) {
            return 20;
        }
        Object renderable = getRender();
        if (renderable == null) {
            return 20;
        }
        return (int) getFieldValue("ModelHeight", renderable);
    }

    @Override
    public void draw(Graphics2D g, Color color) {
        Model model = getModel();
        if (model == null) {
            return;
        }
        model.draw(g, color);
    }

    @Override
    public void draw(Graphics2D g) {
        draw(g, Color.WHITE);
    }

    @Override
    public boolean isOnScreen() {
        Model m = getModel();
        if (m == null) {
            return false;
        }
        if (Utilities.inViewport(m.getRandomPoint())) {
            return true;
        }
        return false;
    }

    @Override
    public Point getPointOnScreen() {
        return getLocation().getPointOnScreen();
    }

    public Point getCenterPoint() {

        Model bounds = getModel();
        if (bounds != null) {
            System.out.println("Getting center point");
            return bounds.getCenterPoint();
        }

        return Calculations.tileToCanvas(getLocation(), 0.5, 0.5, getHeight());
    }

    @Override
    public Point getInteractPoint() {

        Model bounds = getModel();
        if (bounds != null) {
            return bounds.getRandomPoint();
        }
        return Calculations.tileToCanvas(getLocation(), 0.5, 0.5, getHeight());
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(this);
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(tile, locatable.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return Calculations.distanceBetween(tile, getLocation());
    }

    @Override
    public boolean turnTo() {
        return Camera.turnTo(this);
    }

    @Override
    public boolean interact(String action, String name) {
        return interact(action, name);

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
                    if (ServerEngine.getInstance().isGameObjectCenterInteract()) {
                        ip = this.getCenterPoint();
                    } else {
                        ip = this.getInteractPoint();
                    }
                }
                if (Mouse.getLocation().distance(ip) <= 5) {
                    if (ServerEngine.getInstance().isGameObjectCenterInteract()) {
                        ip = this.getCenterPoint();
                    } else {
                        ip = this.getInteractPoint();
                    }
                    Mouse.move(ip);
                    Condition.sleep(75);
                } else {
                    Mouse.move(ip);
                    Condition.sleep(75);
                }

                if (this.isValid()) {
                    if (!Menu.isOpen() && Menu.contains(action, this.getName())) {

                        int index = Menu.index(action, this.getName());
                        if (index == 0) {
                            if (Menu.contains(action, this.getName())) {
                                Condition.sleep(Random.nextInt(80, 150));
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
                            Condition.sleep(Random.nextInt(60, 100));
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
                            Condition.sleep(Random.nextInt(60, 100));
                            Mouse.click(true);
                            return true;
                        }
                    }
                }
            } else {
                if (this.distanceTo() >= 6) {
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
    public boolean click(boolean left) {
        Point interactingPoint = this.getInteractPoint();
        Model bounds           = getModel();
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

    public Model getModel() {
        try {
            int gridX = 0;
            int gridY = 0;
            if (type.cato.equals("GameObject")) {
                gridX = (int) getFieldValue("getInteractiveWorldX", raw);
                gridY = (int) getFieldValue("getInteractiveWorldY", raw);
            } else if (type.cato.equals("Boundary")) {
                gridX = (int) getFieldValue("getBoundryLocalX", raw);
                gridY = (int) getFieldValue("getBoundryLocalY", raw);
            }

            int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().getX() - Game.getBaseX()][getLocation().getY()
                    - Game.getBaseY()];
            if (isValid() && getName() != null && getName().toLowerCase().contains("fishing")) {
                tileByte = 0;
            }
            int      z          = tileByte == 1 ? 210 : 0;
            Object[] renderable = new Object[]{ getRender(), null };
            if (instanceOf(renderable[0])) {
                return new Model(new Model(renderable[0]), 0, gridX, gridY, z);
            }
            if (instanceOf(renderable[1])) {
                return new Model(new Model(renderable[1]), 0, gridX, gridY, z);
            }

            return renderable[0] != null && ModelCallBack.get(renderable[0]) != null
                    ? new Model(ModelCallBack.get(renderable[0]), 0, gridX, gridY, z) : null;
        } catch (Exception e) {

        }
        return null;
    }

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

    public boolean hasAction(String action, String[] actions) {
        for (String s : actions) {
            if (s != null && s.length() > 0) {
                if (s.equalsIgnoreCase(action)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        // if (objectDefinition == null) {
        // objectDefinition = new ObjectDefinition(getId());
        // }
        return null;
    }

    @Override
    public boolean canReach() {

        return false;
    }

    public String[] getActions() {
        if (objectDefinition == null) {
            objectDefinition = new ObjectDefinition(getId());
        }
        return objectDefinition.getActions();
    }

    public enum Type {
        INTERACTIVE("GameObject"), BOUNDARY("Boundary");

        String cato;

        Type(String cato) {
            this.cato = cato;
        }
    }

}
