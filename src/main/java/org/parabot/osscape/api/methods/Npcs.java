package org.parabot.osscape.api.methods;

import org.parabot.environment.api.utils.Filter;
import org.parabot.osscape.Loader;
import org.parabot.osscape.accessors.Client;
import org.parabot.osscape.api.wrapper.Npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author JKetelaar
 */
public class Npcs {
    private static final Comparator<Npc> NEAREST_SORTER = new Comparator<Npc>() {

        @Override
        public int compare(Npc n1, Npc n2) {
            return n1.distanceTo() - n2.distanceTo();
        }

    };

    private static final Filter<Npc> ALL_FILTER = new Filter<Npc>() {

        @Override
        public boolean accept(Npc n) {
            return true;
        }

    };

    /**
     * Gets all Npcs except local Npc
     *
     * @param filter
     *
     * @return all Npcs
     */
    public static final Npc[] getNpcs(final Filter<Npc> filter) {
        final Client                              client  = Loader.getClient();
        ArrayList<Npc>                            npcList = new ArrayList<>();
        final org.parabot.osscape.accessors.Npc[] accNpcs = client.getLocalNpcs();
        for (int i = 0; i < accNpcs.length; i++) {
            if (accNpcs[i] == null) {
                continue;
            }
            final Npc npc = new Npc(accNpcs[i], i);
            if (filter.accept(npc)) {
                npcList.add(npc);
            }
        }

        return npcList.toArray(new Npc[npcList.size()]);
    }

    /**
     * Gets all Npcs
     *
     * @return all Npcs
     */
    public static final Npc[] getNpcs() {
        return getNpcs(ALL_FILTER);
    }

    /**
     * Gets the closest npc which matches the given filter
     *
     * @param filter
     *
     * @return closest npc
     */
    public static final Npc getClosest(final Filter<Npc> filter) {
        Npc[] npcs = getNearest(filter);
        if (npcs == null || npcs.length == 0) {
            return null;
        }

        return npcs[0];
    }

    /**
     * Gets the closest npc which matches the given ids
     *
     * @param ids
     *
     * @return closest npc
     */
    public static final Npc getClosest(int... ids) {
        Npc[] npcs = getNearest(ids);
        if (npcs == null || npcs.length == 0) {
            return null;
        }

        return npcs[0];
    }

    /**
     * Returns array with the first index to be the nearest Npc
     *
     * @param filter
     *
     * @return nearest Npcs
     */
    public static final Npc[] getNearest(final Filter<Npc> filter) {
        final Npc[] npcs = getNpcs(filter);
        Arrays.sort(npcs, NEAREST_SORTER);

        return npcs;
    }

    /**
     * Gets nearest npcs which hold given id(s)
     *
     * @param ids
     *
     * @return array of npcs with the first index to be the nearest
     */
    public static final Npc[] getNearest(final int... ids) {
        final Npc[] npcs = getNpcs(new Filter<Npc>() {

            @Override
            public boolean accept(Npc npc) {
                if (npc.getDef() != null) {
                    for (final int id : ids) {
                        if (id == npc.getDef().getId()) {
                            return true;
                        }
                    }
                }

                return false;
            }

        });
        Arrays.sort(npcs, NEAREST_SORTER);

        return npcs;
    }

    /**
     * Returns array with the first index to be the nearest Npc
     *
     * @return nearest Npcs
     */
    public static final Npc[] getNearest() {
        return getNearest(ALL_FILTER);
    }

    public static Npc getNearbyBanker() {
        Npc current = null;
        for (Npc n : Npcs.getNpcs()) {
            if (Game.isLoggedIn() && n != null && n.getLocation().distanceTo() < 15) {
                if (n.hasAction("Bank") && (current == null || current.getLocation().distanceTo() > Players.getMyPlayer()
                        .distanceTo(n.getLocation()))) {
                    current = n;
                }
            }
        }

        return current;
    }
}
