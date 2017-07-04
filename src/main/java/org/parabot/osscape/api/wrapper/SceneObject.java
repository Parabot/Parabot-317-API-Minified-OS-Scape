package org.parabot.osscape.api.wrapper;

import org.parabot.osscape.accessors.GameObject;
import org.parabot.osscape.accessors.Renderable;
import org.parabot.osscape.api.interfaces.Interactable;
import org.parabot.osscape.api.interfaces.Locatable;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Models;
import org.parabot.osscape.api.wrapper.walking.Walking;

import java.awt.*;

/**
 * @author JKetelaar
 */
public class SceneObject implements Locatable, Interactable {

    private org.parabot.osscape.accessors.SceneObject accessor;
    private Type                                      type;
    private ObjectDefinition                          definition;

    public SceneObject(GameObject accessor, int type) {
        new SceneObject(accessor, Type.getForID(type));
    }

    public SceneObject(GameObject accessor, Type type) {
        this.accessor = accessor;
        this.type = type;
    }

    @Override
    public boolean isOnScreen() {
        return false;
    }

    @Override
    public Point getPointOnScreen() {
        return null;
    }

    @Override
    public int distanceTo() {
        return 0;
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return 0;
    }

    @Override
    public int distanceTo(Tile tile) {
        return 0;
    }

    @Override
    public boolean turnTo() {
        return false;
    }

    @Override
    public Tile getLocation() {
        return new Tile(Game.getBaseX() + getLocalX(), Game.getBaseY() + getLocalY(), getPlane());
    }

    @Override
    public void draw(Graphics2D g, Color color) {

    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public boolean canReach() {
        return false;
    }

    public ObjectDefinition getObjectDefinition() {
        if (this.definition == null) {
            this.definition = ObjectDefinition.getObjectDefinition(this.getID());
        }

        return this.definition;
    }

    public Model getModel() {
        try {
            int gridX = 0;
            int gridY = 0;
            if (type.equals(Type.GAME_OBJECT)) {
                gridX = getWorldX();
                gridY = getWorldY();
            } else if (type.equals(Type.BOUNDARY)) {
                gridX = getLocalX();
                gridY = getLocalY();
            }

            int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().getX() - Game.getBaseX()][getLocation().getY()
                    - Game.getBaseY()];
            if (this.getObjectDefinition().getName() != null && this.getObjectDefinition().getName().toLowerCase().contains("fishing")) {
                tileByte = 0;
            }
            int      z          = tileByte == 1 ? 210 : 0;
            Object[] renderable = new Object[]{ getRender(), null };
            if (renderable[0] != null) {
                return new Model((org.parabot.osscape.accessors.Model) renderable[0], 0, gridX, gridY, z);
            }
            if (renderable[1] != null) {
                return new Model((org.parabot.osscape.accessors.Model) renderable[1], 0, gridX, gridY, z);
            }

            return new Model(Models.get(renderable[0]), 0, gridX, gridY, z);
        } catch (Exception ignored) {

        }
        return null;
    }

    public Renderable getRender() {
        return accessor.getRender();
    }

    public int getID() {
        return (accessor.getID() >> 14) & 0x7FFF;
    }

    public int getFlags() {
        return accessor.getFlags();
    }

    public int getPlane() {
        return accessor.getPlane();
    }

    public int getHeight() {
        return accessor.getHeight();
    }

    public int getOrientation() {
        return accessor.getOrientation();
    }

    public int getLocalX() {
        return accessor.getLocalX();
    }

    public int getLocalY() {
        return accessor.getLocalY();
    }

    public int getOffsetX() {
        return accessor.getOffsetX();
    }

    public int getOffsetY() {
        return accessor.getOffsetY();
    }

    public int getWorldX() {
        return accessor.getWorldX();
    }

    public int getWorldY() {
        return accessor.getWorldY();
    }

    @Override
    public Point getInteractPoint() {
        return null;
    }

    @Override
    public boolean interact(String action, String option) {
        return false;
    }

    @Override
    public boolean interact(String action) {
        return false;
    }

    @Override
    public boolean click(boolean left) {
        return false;
    }

    @Override
    public boolean click() {
        return false;
    }

    public enum Type {
        BOUNDARY(0),
        GAME_OBJECT(1);

        private int id;

        Type(int id) {
            this.id = id;
        }

        public static Type getForID(int id) {
            for (Type type : values()) {
                if (type.getId() == id) {
                    return type;
                }
            }

            return null;
        }

        public int getId() {
            return id;
        }
    }
}
