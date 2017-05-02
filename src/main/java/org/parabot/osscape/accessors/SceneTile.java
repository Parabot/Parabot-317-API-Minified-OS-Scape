package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface SceneTile {
    /**
     * @return
     *
     * @TODO: Make #getGameObjects
     */
    GameObject[] getGameObject();

    /**
     * @return
     *
     * @TODO: Make #getBoundaryObjects
     */
    BoundaryObject[] getBoundaryObject();
}
