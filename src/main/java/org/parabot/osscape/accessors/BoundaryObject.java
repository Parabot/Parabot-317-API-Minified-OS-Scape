package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface BoundaryObject {
    Renderable getBoundryRender();

    Renderable getBoundryRender2();

    int getBoundryID();

    int getBoundryFlags();

    int getBoundryLocalX();

    int getBoundryLocalY();

    int getBoundryPlane();

    int getBoundryOrientation();

    int getBoundryHeight();
}
