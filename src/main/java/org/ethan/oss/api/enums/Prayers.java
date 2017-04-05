package org.ethan.oss.api.enums;

import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.parabot.osscape.api.methods.Game;

public final class Prayers {
    public static final int PRAYER_TAB       = 541;
    public static final int PRAYER_INTERFACE = 7;

    public static WidgetChild getPrayer(Prayer p) {
        return Widgets.get(PRAYER_TAB).getChildren()[p.getInterface()];
    }

    public static void activate(Prayer p) {
        if (!Tab.PRAYER.isOpen()) {
            Tab.PRAYER.open();
        }
        getPrayer(p).interact("Activate");

    }

    public static void deactivate(Prayer p) {
        if (!Tab.PRAYER.isOpen()) {
            Tab.PRAYER.open();
        }
        getPrayer(p).interact("Deactivate");
    }

    public static boolean usingQuickPrayer() {
        return Game.getSettings().get(375) == 1;
    }

    public static void toggleQuickPrayer() {
        WidgetChild qp = Widgets.get(160, 14);
        if (qp != null) {
            if (usingQuickPrayer()) {
                qp.interact("Deactivate");
            } else {
                qp.interact("Activate");
            }
        }
    }

    public static boolean isActive(Prayer p) {
        return getPrayer(p).getActions()[0].equals("Deactivate");
    }

    public enum Modern implements Prayer {

        THICK_SKIN(0, 1, "Thick skin"), BURST_OF_STRENGTH(1, 4, "Burst of strength"), CLARITY_OF_THOUGHT(2, 7,
                "Clarity of thought"), SHARP_EYE(3, 8, "Sharp eye"), MYSTIC_WILL(4, 9, "Mystic will"), ROCK_SKIN(5, 10,
                "Rock skin"), SUPERHUMAN_STRENGTH(6, 13, "Superhuman strength"), IMPROVED_REFLEXES(7, 16,
                "Improved reflexes"), RAPID_RESTORE(8, 19, "Rapid restore"), RAPID_HEAL(11, 22,
                "Rapid heal"), PROTECT_ITEM_REGULAR(12, 25, "Protect item"), HAWK_EYE(11, 26,
                "Hawk eye"), MYSTIC_LORE(12, 27, "Mystic lore"), STEEL_SKIN(13, 28,
                "Steel skin"), ULTIMATE_STRENGTH(14, 31,
                "Ultimate strength"), INCREDIBLE_REFLEXES(15, 34,
                "Incredible reflexes"), PROTECT_FROM_MAGIC(16,
                37,
                "Protect from magic"), PROTECT_FROM_MISSILES(
                16, 40,
                "Protect from missiles"), PROTECT_FROM_MELEE(
                18, 43,
                "Protect from melee"), EAGLE_EYE(
                26, 44,
                "Eagle eye"), MYSTIC_MIGHT(
                27, 45,
                "Mystic might"), RETRIBUTION(
                22,
                46,
                "Retribution"), REDEMPTION(
                27,
                49,
                "Redemption"), SMITE(
                21,
                52,
                "Smite"), CHIVALRY(
                28,
                60,
                "Chivalry"), PIETY(
                29,
                70,
                "Piety");

        private int    index;
        private int    reqLvl;
        private String name;

        private Modern(int index, int reqLvl, String name) {
            this.index = index;
            this.reqLvl = reqLvl;
            this.name = name;
        }

        public int getInterface() {
            return this.index;
        }

        public int getRequiredLvl() {
            return this.reqLvl;
        }

        public String getName() {
            return this.name;
        }
    }

    public interface Prayer {

        public int getInterface();

        public int getRequiredLvl();

        public String getName();

    }
}