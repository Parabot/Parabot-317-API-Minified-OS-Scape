package org.ethan.oss.api.methods;

import org.ethan.oss.api.enums.Tab;
import org.ethan.oss.api.input.Keyboard;
import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.environment.api.utils.Filter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Inventory {

    private static final int WIDGET_INVENTORY_INDEX = 149;
    private static final int WIDGET_INVENTORY_SLOTS = 0;

    private static final int[] dropPattern = { 0, 4, 8, 12, 16, 20, 24, 25, 21, 17, 13, 9, 5, 1, 2, 6, 10, 14, 18, 22,
            26, 27, 23, 19, 15, 11, 7, 3 };

    public static Item[] getAllItems(Filter<Item> filter) {
        java.util.List<Item> list = new ArrayList<>();
        if (!Game.isLoggedIn()) {
            return list.toArray(new Item[list.size()]);
        }
        final WidgetChild child = Widgets.get(WIDGET_INVENTORY_INDEX, WIDGET_INVENTORY_SLOTS);
        if (!child.isVisible()) {
            return list.toArray(new Item[list.size()]);
        }
        final int[] contentIds = child.getSlotContentIds();
        final int[] stackSizes = child.getStackSizes();
        if (contentIds == null || stackSizes == null) {
            return list.toArray(new Item[list.size()]);
        }
        for (int itemIndex = 0; itemIndex < contentIds.length; itemIndex++) {
            Item item = new Item(contentIds[itemIndex] - 1, stackSizes[itemIndex], itemIndex, Item.Type.INVENTORY,
                    getSlotArea(itemIndex));
            if (item.isValid() && (filter == null || filter.accept(item))) {
                list.add(item);
            }
        }
        return list.toArray(new Item[list.size()]);
    }

    public static Item[] getAllItems() {
        return getAllItems(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return true;
            }
        });
    }

    public static Item getItem(Filter<Item> filter) {
        Item[] items = getAllItems(filter);

        if (items == null || items.length == 0) {
            return nil();
        }

        return items[0];
    }

    public static void dropAll(boolean shiftDrop) {
        if (!Tab.INVENTORY.isOpen()) {
            Tab.INVENTORY.open();
            Condition.sleep(Random.nextInt(75, 300));
        }
        if (shiftDrop) {
            Keyboard.press(KeyEvent.VK_SHIFT);
        }
        for (int i = 0; i < dropPattern.length; i++) {
            final Item itemAt = getItemAt(dropPattern[i]);
            if (itemAt.isValid()) {
                if (shiftDrop) {
                    itemAt.click();
                } else {
                    itemAt.interact("Drop");
                }
            }
        }
        if (shiftDrop) {
            Keyboard.release(KeyEvent.VK_SHIFT);
        }
    }

    public static void dropAllExcept(boolean shiftDrop, int... id) {
        if (!Tab.INVENTORY.isOpen()) {
            Tab.INVENTORY.open();
            Condition.sleep(Random.nextInt(75, 300));
        }
        if (shiftDrop) {
            Keyboard.press(KeyEvent.VK_SHIFT);
        }
        for (int i = 0; i < dropPattern.length; i++) {
            final Item itemAt = getItemAt(dropPattern[i]);
            if (itemAt.isValid() && (id == null || !Utilities.inArray(itemAt.getId(), id))) {
                if (shiftDrop) {
                    itemAt.click();
                } else {
                    itemAt.interact("Drop");
                }
            }
        }
        if (shiftDrop) {
            Keyboard.release(KeyEvent.VK_SHIFT);
        }
    }

    public static void dropAllExcept(boolean shiftDrop, String... name) {
        if (!Tab.INVENTORY.isOpen()) {
            Tab.INVENTORY.open();
            Condition.sleep(Random.nextInt(75, 300));
        }
        if (shiftDrop) {
            Keyboard.press(KeyEvent.VK_SHIFT);
        }
        for (int i = 0; i < dropPattern.length; i++) {
            final Item itemAt = getItemAt(dropPattern[i]);
            if (itemAt.isValid() && (name == null || !Utilities.inArray(itemAt.getName(), name))) {
                if (shiftDrop) {
                    itemAt.click();
                } else {
                    itemAt.interact("Drop");
                }
            }
        }
        if (shiftDrop) {
            Keyboard.release(KeyEvent.VK_SHIFT);
        }
    }

    public static Item getRandomItem(Filter<Item> filter) {
        Item[] items = getAllItems(filter);

        if (items == null || items.length <= 0) {
            return nil();
        }

        int rand = new java.util.Random().nextInt(items.length);
        return items[rand];
    }

    public static Item nil() {
        return new Item(-1, -1, -1, Item.Type.INVENTORY, null);
    }

    public static Item getItem(final int... ids) {
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
    }

    public static Item getRandomItem(final int... ids) {
        return getRandomItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
    }

    public static Item getItem(final String... names) {
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
    }

    public static Item getRandomItem(final String... names) {
        return getRandomItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
    }

    public static int getUsedSpace() {
        return getAllItems().length;
    }

    public static int getFreeSpace() {
        return 28 - getAllItems().length;
    }

    public static boolean isFull() {
        return getUsedSpace() == 28;
    }

    public static boolean isEmpty() {
        return getUsedSpace() == 0;
    }

    public static int getCount(boolean countStackSize, Filter<Item> filter) {
        int count = 0;
        for (Item item : getAllItems(filter)) {
            count = count + (countStackSize ? item.getStackSize() : 1);
        }
        return count;
    }

    public static int getCount(boolean countStackSize, final String... names) {
        if (names == null) {
            return 0;
        }
        return getCount(countStackSize, new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
    }

    public static int getCount(boolean countStackSize) {
        int count = 0;
        for (Item i : getAllItems()) {
            if (i.isValid()) {
                count = count + (countStackSize ? i.getStackSize() : 1);
            }
        }
        return count;
    }

    public static int getCount(boolean countStackSize, final int... ids) {
        if (ids == null) {
            return 0;
        }
        return getCount(countStackSize, new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
    }

    public static int getCount(final int... ids) {
        return getCount(false, ids);
    }

    public static int getCount(final String... names) {
        return getCount(false, names);
    }

    public static int getCount(Filter<Item> filter) {
        return getCount(false, filter);
    }

    public static boolean contains(Filter<Item> filter) {
        return getItem(filter).isValid();
    }

    public static boolean contains(int... ids) {
        return getItem(ids).isValid();
    }

    public static boolean contains(String... names) {
        return getItem(names).isValid();
    }

    public static boolean containsAll(final int... ids) {
        if (ids == null) {
            return false;
        }
        for (int id : ids) {
            if (!contains(id)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsAll(final String... names) {
        if (names == null) {
            return false;
        }

        for (String name : names) {
            if (!contains(name)) {
                return false;
            }
        }

        return true;
    }

    public static Item getItemAt(final int index) {
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.getIndex() == index;
            }
        });
    }

    public static Rectangle getSlotArea(int slot) {
        int x = 563 + ((slot % 4) * 42), y = 213 + ((int) Math.floor((float) slot / (float) 4) * 36);
        return new Rectangle(x, y, 31, 31);
    }

}