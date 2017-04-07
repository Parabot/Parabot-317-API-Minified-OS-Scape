package org.parabot.osscape.accessors;

public interface Client {
    linkedList[][][] getGroundItems();

    byte[][][] getTileSettings();

    int[][][] getTileHeights();

    Widget[][] getWidgets();

    Player[] getLocalPlayers();

    Npc[] getLocalNpcs();

    int[] getWidgetWidths();

    int[] getWidgetHeights();

    int[] getExperiences();

    int[] getRealLevels();

    int[] getCurrentLevels();

    int[] getGameSettings();

    String[] getMenuActions();

    String[] getMenuOptions();

    Player getLocalPlayer();

    Object getCanvas();

    Region getRegion();

    WidgetNode getWidgetNodeCache();

    int getCameraZ();

    int getCameraX();

    int getCameraY();

    int getCameraYaw();

    int getDestinationY();

    int getDestinationX();

    int getViewportHeight();

    int getViewportWidth();

    int getCameraScale();

    int getMenuHeight();

    int getMenuWidth();

    int getMenuX();

    int getMenuY();

    int getMapAngle();

    int getMenuCount();

    int getPlayerIndex();

    int getLoginState();

    boolean getIsMenuOpen();

    int getBaseY();

    int getBaseX();

    int getPlane();

    int getLoopCycle();

    int getEnergy();

    int getCurrentWorld();

    int getTick();

    int[] getWidgetPositionY();

    int getWeight();

    int[] getWidgetPositionX();

    int getCameraPitch();

    int getMapScale();

    int getMapOffset();
}
