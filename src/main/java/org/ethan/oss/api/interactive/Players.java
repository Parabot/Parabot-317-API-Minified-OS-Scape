package org.ethan.oss.api.interactive;

import org.ethan.oss.api.wrappers.Player;
import org.ethan.oss.api.wrappers.Tile;
import org.ethan.oss.reflection.ReflWrapper;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class Players extends ReflWrapper {

    public static Player getLocal() {
        return new Player(getFieldValue("LocalPlayer", null));
    }

    public static Player[] getAll() {
        return getAll(null);
    }

    public static Player[] getAll(Filter<Player> filter) {
        List<Player>   list    = new ArrayList<Player>();
        final Object[] objects = (Object[]) getFieldValue("LocalPlayers", null);
        for (Object player : objects) {
            if (player != null) {
                Player wrapper = new Player(player);
                if ((filter == null || filter.accept(wrapper))) {
                    list.add(wrapper);
                }
            }
        }
        return list.toArray(new Player[list.size()]);
    }

    public static Player getNearest(Tile location, Filter<Player> filter) {
        Player closet   = new Player(null);
        int    distance = 9999;
        for (Player player : getAll(filter)) {
            if (distance > player.distanceTo(location)) {
                closet = player;
            }
        }
        return closet;
    }

    public static Player getNearest(Filter<Player> filter) {
        return getNearest(Players.getLocal().getLocation(), filter);
    }

    public static Player nil() {
        return new Player(null);
    }

}

