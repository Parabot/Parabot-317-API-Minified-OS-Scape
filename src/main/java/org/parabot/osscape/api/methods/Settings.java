package org.parabot.osscape.api.methods;

import org.parabot.osscape.accessors.Client;

/**
 * @author JKetelaar
 */
public class Settings {

    private final int[] settings;

    public Settings(int[] settings) {
        this.settings = settings;
    }

    public Settings(Client accessor) {
        this.settings = accessor.getGameSettings();
    }

    public int[] getAll() {

        return settings;
    }

    public int get(int a) {
        int[] settings = getAll();
        if (settings.length <= a) {
            return 0;
        }
        return settings[a];
    }
}
