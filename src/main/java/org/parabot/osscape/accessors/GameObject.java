package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface GameObject {
    Object getInteractiveRender();

    int getInteractiveID();

    int getInteractiveFlags();

    int getInteractivePlane();

    int getInteractiveWorldX();

    int getInteractiveWorldY();

    int getInteractiveHeight();

    int getInteractiveOrientation();

    int getInteractiveLocalX();

    int getInteractiveLocalY();

    int getInteractiveOffsetX();

    int getInteractiveOffsetY();
}
