package org.parabot.osscape.api.methods;

import org.ethan.oss.api.enums.Tab;
import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.parabot.osscape.Loader;
import org.parabot.osscape.accessors.Client;
import org.parabot.osscape.accessors.Widget;
import org.parabot.osscape.accessors.WidgetNode;

/**
 * @author JKetelaar
 */
public class Game {

    private static final Client accessor = Loader.getClient();

    public static Widget[][] getWidgets(){
        return accessor.getWidgets();
    }

    public static int getBaseX() {
        return accessor.getBaseX();
    }

    public static int getBaseY() {
        return accessor.getBaseY();
    }

    public static int getPlane() {
        return accessor.getPlane();
    }

    public static int getEnergy() {
        return accessor.getEnergy();
    }

    public static int getMapOffset() {
        return accessor.getMapOffset();
    }

    public static int getMapScale() {
        return accessor.getMapScale();
    }

    public static int getMapAngle() {
        return accessor.getMapAngle();
    }

    public static int getCameraScale() {
        return accessor.getCameraScale();
    }

    public static int getViewportWidth() {
        return accessor.getViewportWidth();
    }

    public static int getViewportHeight() {
        return accessor.getViewportHeight();
    }

    public static byte[][][] getTileSettings() {
        return accessor.getTileSettings();
    }

    public static int[][][] getTileHeights() {
        return accessor.getTileHeights();
    }

    public static Settings getSettings() {
        return new Settings(accessor.getGameSettings());
    }

    public static boolean isUsingSpecialAttack() {
        return getSettings().get(301) == 1;
    }

    public static int getMenuX() {
        return accessor.getMenuX();
    }

    public static int getMenuY() {
        return accessor.getMenuY();
    }

    public static int getMenuWidth() {
        return accessor.getMenuWidth();
    }

    public static int getMenuHeight() {
        return accessor.getMenuHeight();
    }

    public static int getMenuCount() {
        return accessor.getMenuCount();
    }

    public static int[] getWidgetPositionX(){
        return accessor.getWidgetPositionX();
    }

    public static int[] getWidgetPositionY(){
        return accessor.getWidgetPositionY();
    }

    public static WidgetNode getWidgetNodeCache(){
        return accessor.getWidgetNodeCache();
    }

    public static int getCameraPitch(){
        return accessor.getCameraPitch();
    }

    public static int getCameraYaw(){
        return accessor.getCameraYaw();
    }

    public static int getCameraX(){
        return accessor.getCameraX();
    }

    public static int getCameraY(){
        return accessor.getCameraY();
    }

    public static int getCameraZ(){
        return accessor.getCameraZ();
    }

    public static boolean isMenuOpen() {
        return accessor.getIsMenuOpen();
    }

    public static String[] getMenuActions() {
        return accessor.getMenuActions();
    }

    public static String[] getMenuOptions() {
        return accessor.getMenuOptions();
    }

    public static int getSpecialAttackPercent() {
        if (getSettings().get(300) > 0) {
            return getSettings().get(300) / 10;
        }
        return 0;
    }

    public static int getGameState() {
        return accessor.getLoginState();
    }

    public static LoginState getLoginState() {
        return LoginState.getStateForStateId(accessor.getLoginState());
    }

    public static int getCurrentWorld() {
        return accessor.getCurrentWorld();
    }

    public static int getGameCycle() {
        return accessor.getLoginState();
    }

    public static boolean isRunning() {
        return getSettings().get(173) > 0;
    }

    public static void enableRunning() {
        WidgetChild c = Widgets.get(160, 21);
        if (getEnergy() > 0) {
            if (c != null) {
                if (c.isVisible()) {
                    c.interact("Toggle Run");
                }
            }
        }
    }

    public static boolean isLoggedIn() {
        return getLoginState().equals(LoginState.STATE_LOGGED_IN);
    }

    public static Tab getCurrentTab() {
        final int           WIDGET_PARENT = 548;
        final WidgetChild[] children      = Widgets.get(WIDGET_PARENT).getChildren();
        if (children != null && children.length != 0) {
            for (WidgetChild p : children) {
                if (p.getTextureID() != -1 && p.getActions() != null) {
                    String[] actions = p.getActions();
                    for (Tab tab : Tab.values()) {
                        for (String action : actions) {
                            if (tab.getName().equalsIgnoreCase(action)) {
                                return tab;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Client getAccessor() {
        return accessor;
    }

    public enum LoginState {

        STATE_LOGGED_IN(30),
        STATE_LOG_IN_SCREEN(10),
        CONNECTING(20);

        private int stateId;

        LoginState(int stateId) {
            this.stateId = stateId;
        }

        private static LoginState getStateForStateId(int stateId) {
            for (LoginState state : LoginState.values()) {
                if (state.getStateId() == stateId) {
                    return state;
                }
            }

            return null;
        }

        public int getStateId() {
            return stateId;
        }
    }
}
