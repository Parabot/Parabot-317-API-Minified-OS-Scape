package org.ethan.oss.api.interactive;

import org.parabot.osscape.api.methods.Game;
import org.ethan.oss.api.wrappers.NPC;
import org.ethan.oss.api.wrappers.Tile;
import org.ethan.oss.reflection.ReflWrapper;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class Npcs extends ReflWrapper {

    public static NPC[] getAll(final String... names) {
        if (names == null) {
            return getAll();
        }
        return getAll(new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                return npc.isValid() && npc.getName() != null && Utilities.inArray(npc.getName(), names);
            }
        });
    }

    public static NPC[] getAll(Filter<NPC> filter) {
        List<NPC> list = new ArrayList<NPC>();
        if (!Game.isLoggedIn()) {
            return list.toArray(new NPC[list.size()]);
        }
        final Object[] objects = (Object[]) getFieldValue("LocalNpcs", null);
        for (Object npc : objects) {
            if (npc != null) {
                NPC wrapper = new NPC(npc);
                if ((filter == null || filter.accept(wrapper))) {
                    list.add(wrapper);
                }
            }
        }
        return list.toArray(new NPC[list.size()]);
    }

    public static NPC[] getAll() {
        return getAll(new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                return true;
            }
        });
    }

    public static NPC getNearest(Filter<NPC> filter) {
        return getNearest(Players.getLocal().getLocation(), filter);
    }

    public static NPC getNearest(Tile location, Filter<NPC> filter) {
        NPC closet   = new NPC(null);
        int distance = 9999;
        for (NPC npc : getAll(filter)) {
            if (npc.isValid() && distance > npc.distanceTo(location)) {
                closet = npc;
                distance = npc.distanceTo(location);
            }
        }
        return closet;
    }

    public static NPC getNearest(final int... ids) {
        if (!Game.isLoggedIn()) {
            return new NPC(null);
        }
        return getNearest(Players.getLocal().getLocation(), new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                return npc.isValid() && Utilities.inArray(npc.getId(), ids);
            }
        });
    }

    public static NPC getNearest(final String... names) {
        if (!Game.isLoggedIn()) {
            return new NPC(null);
        }
        return getNearest(Players.getLocal().getLocation(), new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                return npc.isValid() && Utilities.inArray(npc.getName(), names);
            }
        });
    }

    public static NPC getNext(Filter<NPC> npcFilter) {
        NPC[] npcs = Npcs.getAll(npcFilter);
        if (npcs == null || npcs.length < 1) {
            return nil();
        }
        final int NpcIndex = Random.nextInt(0, npcs.length);
        return npcs[NpcIndex];
    }

    public static NPC getNext(final String... names) {
        return getNext(new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                return npc.isValid() && npc.getName() != null && Utilities.inArray(npc.getName(), names);
            }
        });
    }

    public static NPC getNext(final int... ids) {
        return getNext(new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                return npc.isValid() && Utilities.inArray(npc.getId(), ids);
            }
        });
    }

    public static NPC getNearbyBanker() {
        NPC current = null;
        for (NPC n : Npcs.getAll()) {
            if (Game.isLoggedIn() && n != null && n.isValid() && n.getLocation().distanceTo() < 15) {
                if (n.hasAction("Bank") && (current == null || current.getLocation().distanceTo() > Players.getLocal()
                        .distanceTo(n.getLocation()))) {
                    current = n;
                }
            }
        }

        return current;
    }

    public static NPC nil() {
        return new NPC(null);
    }
}