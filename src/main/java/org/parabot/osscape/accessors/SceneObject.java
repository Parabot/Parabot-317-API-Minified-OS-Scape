package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface SceneObject {
    Renderable getRender();

    int getID();

    int getFlags();

    int getPlane();

    int getHeight();

    int getOrientation();

    int getLocalX();

    int getLocalY();

    int getOffsetX();

    int getOffsetY();

    int getWorldX();

    int getWorldY();
}
