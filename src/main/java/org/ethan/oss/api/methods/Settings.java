package org.ethan.oss.api.methods;

import org.ethan.oss.reflection.ReflWrapper;

public class Settings extends ReflWrapper {
    private static int[] masklookup = new int[32];

    static {
        int i = 2;
        for (int j = 0; j < 32; j++) {
            masklookup[j] = (i - 1);
            i += i;
        }
    }

    public static int[] getAll() {

        return (int[]) getFieldValue("GameSettings", null);
    }

    public static int get(int a) {
        int[] settings = getAll();
        if (settings.length <= a) {
            return 0;
        }
        return settings[a];
    }

    public static String hex(int paramInt) {
        return Integer.toHexString(get(paramInt));
    }

    public static int bit(int paramInt1, int paramInt2) {
        return get(paramInt1) >> paramInt2 & 0x1;
    }

    public static int varbit(int paramInt1, int paramInt2, int paramInt3) {
        int i = masklookup[(paramInt3 - paramInt2)];
        return get(paramInt1) >> paramInt2 & i;
    }
}

