package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface BoundaryObject {
    Object getBoundryRender();

    Object getBoundryRender2();

    int getBoundryID();

    int getBoundryFlags();

    int getBoundryLocalX();

    int getBoundryLocalY();

    int getBoundryPlane();

    int getBoundryOrientation();

    int getBoundryHeight();
}
