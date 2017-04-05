package org.parabot.osscape.api.methods;

import org.ethan.oss.api.enums.Tab;
import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.methods.Settings;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.parabot.osscape.Loader;
import org.parabot.osscape.accessors.Client;

/**
 * @author JKetelaar
 */
public class Game {

    private static final Client accessor = Loader.getClient();

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

    public static int getMapOffset(){
        return accessor.getMapOffset();
    }

    public static int getMapScale(){
        return accessor.getMapScale();
    }

    public static int getMapAngle(){
        return accessor.getMapAngle();
    }

    public static int getCameraScale(){
        return accessor.getCameraScale();
    }

    public static int getViewportWidth(){
        return accessor.getViewportWidth();
    }

    public static int getViewportHeight(){
        return accessor.getViewportHeight();
    }

    public static byte[][][] getTileSettings(){
        return accessor.getTileSettings();
    }

    public static int[][][] getTileHeights(){
        return accessor.getTileHeights();
    }

    public static boolean isUsingSpecialAttack() {
        return Settings.get(301) == 1;
    }

    public static int getSpecialAttackPercent() {
        if (Settings.get(300) > 0) {
            return Settings.get(300) / 10;
        }
        return 0;
    }

    public static int getGameState() {
        return accessor.getLoginState();
    }

    public static LoginState getLoginState(){
        return LoginState.getStateForStateId(accessor.getLoginState());
    }

    public static int getCurrentWorld() {
        return accessor.getCurrentWorld();
    }

    public static int getGameCycle() {
        return accessor.getLoginState();
    }

    public static boolean isRunning(){
        return Settings.get(173) > 0;
    }

    public static void enableRunning(){
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

        public int getStateId() {
            return stateId;
        }

        private static LoginState getStateForStateId(int stateId){
            for (LoginState state : LoginState.values()){
                if (state.getStateId() == stateId){
                    return state;
                }
            }

            return null;
        }
    }
}