package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface SceneTile {
    /**
     * @TODO: Make #getGameObjects
     *
     * @return
     */
    GameObject[] getGameObject();

    /**
     * @TODO: Make #getBoundaryObjects
     *
     * @return
     */
    BoundaryObject[] getBoundaryObject();
}
