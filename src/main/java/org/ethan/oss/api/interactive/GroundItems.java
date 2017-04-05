package org.ethan.oss.api.interactive;

import org.ethan.oss.api.wrappers.GroundItem;
import org.parabot.osscape.api.wrapper.Tile;
import org.ethan.oss.reflection.ReflWrapper;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.environment.api.utils.Filter;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;

import java.util.ArrayList;
import java.util.List;

public class GroundItems extends ReflWrapper {

    public static GroundItem[] getAll(Filter<GroundItem> filter) {
        List<GroundItem> groundItems = new ArrayList<GroundItem>();

        Object[][][] groundArrayObjects = (Object[][][]) getFieldValue("GroundItems", null);

        int z = Game.getPlane();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Object nl = groundArrayObjects[z][x][y];
                if (nl != null) {
                    Object holder  = getFieldValue("Head", nl);
                    Object curNode = getFieldValue("Next", holder);
                    while (curNode != null && curNode != holder && curNode != getFieldValue("Head", nl)) {
                        GroundItem groundItem = new GroundItem(curNode, new Tile(Game.getBaseX() + x, Game.getBaseY() + y, Game.getPlane()));
                        if (filter == null || filter.accept(groundItem)) {
                            groundItems.add(groundItem);
                        }
                        curNode = getFieldValue("Next", curNode);
                    }
                }
            }
        }
        return groundItems.toArray(new GroundItem[groundItems.size()]);
    }

    public static GroundItem[] getAll(final String... names) {
        return getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && groundItem.getName() != null && Utilities.inArray(groundItem.getName(), names);
            }
        });
    }

    public static GroundItem[] getAll(final int... ids) {
        return getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
            }
        });
    }

    public static GroundItem[] getAll() {
        return getAll(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return true;
            }
        });
    }

    public static GroundItem getNearest(Filter<GroundItem> filter) {
        return getNearest(Players.getMyPlayer().getLocation(), filter);
    }

    public static GroundItem getNearest(Tile start, Filter<GroundItem> filter) {
        GroundItem closet   = new GroundItem(null, null);
        int        distance = 16;
        for (GroundItem groundItem : getAll(filter)) {
            if (groundItem.isValid() && distance > groundItem.distanceTo(start)) {
                closet = groundItem;
                distance = groundItem.distanceTo(start);
            }
        }
        return closet;
    }

    public static GroundItem getNearest(final int... ids) {
        return getNearest(Players.getMyPlayer().getLocation(), new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
            }
        });
    }

    public static GroundItem getNearest(final String... names) {
        return getNearest(Players.getMyPlayer().getLocation(), new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getName(), names);
            }
        });
    }

    public static GroundItem getAt(final Tile tile) {
        return getNearest(Players.getMyPlayer().getLocation(), new Filter<GroundItem>() {

            @Override
            public boolean accept(GroundItem obj) {
                return obj != null && tile.equals(obj.getLocation());
            }
        });
    }

    public static GroundItem getNext(Filter<GroundItem> filter) {
        GroundItem[] groundItems = getAll(filter);
        if (groundItems == null || groundItems.length < 1) {
            return nil();
        }
        return groundItems[Random.nextInt(0, groundItems.length)];
    }

    public static GroundItem getNext(final String... names) {
        return getNext(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && groundItem.getName() != null && Utilities.inArray(groundItem.getName(), names);
            }
        });
    }

    public static GroundItem getNext(final int... ids) {
        return getNext(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
            }
        });
    }

    public static GroundItem nil() {
        return new GroundItem(null, null);
    }

}