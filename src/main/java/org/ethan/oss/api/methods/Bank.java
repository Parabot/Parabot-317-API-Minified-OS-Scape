package org.ethan.oss.api.methods;

import org.ethan.oss.api.input.Keyboard;
import org.ethan.oss.api.input.Mouse;
import org.ethan.oss.api.interactive.Npcs;
import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.NPC;
import org.ethan.oss.api.wrappers.WidgetChild;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.environment.api.utils.Filter;
import org.parabot.osscape.api.methods.Game;

import java.util.ArrayList;

public class Bank {

    private static final int BANK_INTERFACE = 12, BANK_ITEMS_PAD = 12, BANK_TITLE = 4, BANK_FULL_SPACE = 5,
            BANK_USED_SPACE                 = 3, BANK_INNER_INTERFACE = 3, BANK_CLOSE = 11, BANK_DEPOSIT_INVENTORY = 25,
            BANK_DEPOSIT_WORN_ITEMS         = 27;

    public static Item[] getAllItems(Filter<Item> filter) {
        ArrayList<Item> list = new ArrayList<Item>();
        if (!Game.isLoggedIn() || !Bank.isOpen()) {
            return list.toArray(new Item[list.size()]);
        }

        WidgetChild parent = Widgets.get(BANK_INTERFACE, BANK_ITEMS_PAD);

        if (!parent.isVisible()) {
            return list.toArray(new Item[list.size()]);
        }
        WidgetChild[] children = parent.getChildren();
        if (children == null) {
            return list.toArray(new Item[list.size()]);
        }

        for (int bankI = 0; bankI < children.length; bankI++) {
            WidgetChild widgetChild = children[bankI];
            if (widgetChild.getItemId() != 6512 && widgetChild.getItemId() != -1) {
                Item item = new Item(widgetChild.getItemId(), widgetChild.getItemStack(), bankI, Item.Type.BANK,
                        widgetChild.getArea());
                if (item.isValid() && (filter == null || filter.accept(item))) {
                    list.add(item);
                }
            }
        }

        return list.toArray(new Item[list.size()]);
    }

    public static Item[] getAllItems() {
        return getAllItems(null);
    }

    public static Item nil() {
        return new Item(-1, -1, -1, Item.Type.BANK, null);
    }

    public static Item getItem(Filter<Item> filter) {
        Item[] allItems = getAllItems(filter);
        return allItems.length > 0 ? allItems[0] : nil();
    }

    public static Item getItem(final int... ids) {
        if (ids == null) {
            return nil();
        }
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
    }

    public static Item getItem(final String... names) {
        if (names == null) {
            return nil();
        }
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
    }

    public static boolean contains(final Filter<Item> filter) {
        return getItem(filter).isValid();
    }

    public static boolean contains(final String... names) {
        return getItem(names).isValid();
    }

    public static boolean contains(final int... ids) {
        return getItem(ids).isValid();
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

    public static int getCount(Filter<Item> filter) {
        final Item item = getItem(filter);
        return item.isValid() ? item.getStackSize() : 0;
    }

    public static int getCount() {
        Item[] items = getAllItems(null);
        if (items == null) {
            return 0;
        }
        int count = 0;
        for (Item item : items) {
            count = count + item.getStackSize();
        }
        return count;
    }

    public static int getCount(final int... ids) {
        Item[] items = getAllItems(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
        if (items == null) {
            return 0;
        }
        int count = 0;
        for (Item item : items) {
            count = count + item.getStackSize();
        }
        return count;
    }

    public static int getCount(final String... names) {
        Item[] items = getAllItems(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
        if (items == null) {
            return 0;
        }
        int count = 0;
        for (Item item : items) {
            count = count + item.getStackSize();
        }
        return count;
    }

    public static int getEmptySpace() {
        if (Widgets.get(BANK_INTERFACE) == null) {
            return -1;
        }
        return getFullSpace() - getUsedSpace();
    }

    public static int getFullSpace() {
        if (Widgets.get(BANK_INTERFACE) == null) {
            return -1;
        }
        return Integer.parseInt(Widgets.get(BANK_INTERFACE, BANK_FULL_SPACE).getText());
    }

    public static int getUsedSpace() {
        if (Widgets.get(BANK_INTERFACE) == null) {
            return -1;
        }
        return Integer.parseInt(Widgets.get(BANK_INTERFACE, BANK_USED_SPACE).getText());
    }

    public static boolean isFull() {
        return !isOpen() && getUsedSpace() == getFullSpace();
    }

    public static boolean isOpen() {
        WidgetChild iFace = Widgets.get(BANK_INTERFACE, BANK_TITLE);
        if (iFace == null) {
            return false;
        }

        return iFace.isVisible() && iFace.getText() != null && iFace.getText().toLowerCase().contains("bank")
                && iFace.getWidth() > 0;
    }

    public static boolean close() {
        if (!isOpen()) {
            return true;
        }
        WidgetChild closeButton = Widgets.get(BANK_INTERFACE, BANK_INNER_INTERFACE).getChild(BANK_CLOSE);
        return closeButton.isVisible() && closeButton.click();
    }

    public static boolean depositInventory() {
        if (isOpen()) {
            final WidgetChild depositInventoryButton = Widgets.get(BANK_INTERFACE, BANK_DEPOSIT_INVENTORY);
            return depositInventoryButton != null && depositInventoryButton.isVisible()
                    && depositInventoryButton.click();
        }
        return false;
    }

    public static boolean depositWornItems() {
        if (isOpen()) {
            final WidgetChild depositWornItems = Widgets.get(BANK_INTERFACE, BANK_DEPOSIT_WORN_ITEMS);
            return depositWornItems != null && depositWornItems.isVisible() && depositWornItems.click();
        }
        return false;
    }

    public static boolean deposit(final int[] ids, final int amount) {
        return deposit(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        }, amount);
    }

    public static boolean deposit(final String[] names, final int amount) {
        return deposit(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        }, amount);
    }

    public static boolean deposit(final String name, final int amount) {
        return deposit(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && item.getName().equalsIgnoreCase(name);
            }
        }, amount);
    }

    public static boolean deposit(Filter<Item> filter, int amount) {
        Item[] items = getAllItems(filter);
        if (items == null || items.length == 0) {
            return false;
        }
        for (Item item : items) {
            if (item.isValid()) {
                deposit(item.getId(), amount);
            }
        }
        return true;
    }

    public static boolean deposit(final int id, int amount) {
        if (!isOpen()) {
            return false;
        }

        Item item = Inventory.getRandomItem(id);
        if (!item.isValid()) {
            return false;
        }

        final int a = Inventory.getCount(id);
        if (amount > a) {
            amount = a;
        }

        if (!Menu.isOpen()) {
            Mouse.move(item.getInteractPoint());
        } else if (Menu.isOpen() && !Menu.contains("Withdraw-" + amount)) {
            Menu.interact("Cancel");
            Condition.wait(new Condition.Check() {
                public boolean poll() {
                    return !Menu.isOpen();
                }
            }, 100, 20);
            Mouse.move(item.getInteractPoint());
        }

        if (amount == 0 || (amount == a && item.interact("Deposit-All"))
                || (Menu.contains("Deposit-" + amount) && item.interact("Deposit-" + amount))) {
            return true;
        }

        if (!item.interact("Deposit-X")) {
            return false;
        }
        Condition.wait(new Condition.Check() {
            public boolean poll() {
                return Widgets.canEnterAmount();
            }
        }, 100, 20);
        if (Widgets.canEnterAmount()) {
            Condition.sleep(Random.nextInt(150, 750));
            Keyboard.sendText("" + amount, true);
        }
        return false;

    }

    public static boolean withdraw(final int id, int amount) {
        if (!isOpen()) {
            return false;
        }

        boolean        butOne  = false;
        final String[] choices = { "All", "All-but-1" };
        String         choice  = "All";
        Item           item    = Bank.getItem(id);
        if (!item.isValid()) {
            return false;
        }

        final int a = Bank.getCount(id);
        if (amount > a) {
            amount = a;
            butOne = true;
        }
        if (butOne) {
            int rnd = new java.util.Random().nextInt(choices.length);
            choice = choices[rnd];
        }
        if (!Menu.isOpen()) {
            Mouse.move(item.getInteractPoint());
        } else if (Menu.isOpen() && !Menu.contains("Withdraw-" + amount)) {
            Menu.interact("Cancel");
            Condition.wait(new Condition.Check() {
                public boolean poll() {
                    return !Menu.isOpen();
                }
            }, 100, 20);
            Mouse.move(item.getInteractPoint());
        }

        if (a == amount ? item.interact("Withdraw-" + choice)
                : (Menu.contains("Withdraw-" + amount) ? !item.interact("Withdraw-" + amount)
                : !item.interact("Withdraw-X"))) {
            return false;
        }

        if (a != amount && !Menu.contains("Withdraw-" + amount)) {
            Condition.wait(new Condition.Check() {
                public boolean poll() {
                    return Widgets.canEnterAmount();
                }
            }, 100, 20);
            if (Widgets.canEnterAmount()) {

                Condition.sleep(Random.nextInt(150, 750));
                Keyboard.sendText("" + amount, true);
            }
        }
        return true;

    }

    public static boolean withdraw(final int[] ids, final int amount) {
        return withdraw(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        }, amount);
    }

    public static boolean withdraw(final String[] names, final int amount) {
        return withdraw(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        }, amount);
    }

    public static boolean withdraw(final String name, final int amount) {
        return withdraw(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && item.getName().equalsIgnoreCase(name);
            }
        }, amount);
    }

    public static boolean withdraw(Filter<Item> filter, int amount) {
        Item[] items = getAllItems(filter);
        if (items == null) {
            return false;
        }
        for (Item item : items) {
            if (item.isValid()) {
                withdraw(item.getId(), amount);
            }
        }
        return true;
    }

    public static boolean depositAll() {
        if (isOpen()) {
            final WidgetChild depositInventory = Widgets.get(BANK_INTERFACE, 29);
            return depositInventory != null && depositInventory.isVisible()
                    && depositInventory.interact("Deposit inventory");
        }
        return false;
    }

    public static boolean depositAllExcept(final String... names) {
        return depositAllExcept(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && item.getName() != null && Utilities.inArray(item.getName(), names);
            }
        });
    }

    public static boolean depositAllExcept(final int... ids) {
        return depositAllExcept(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.isValid() && Utilities.inArray(item.getId(), ids);
            }
        });
    }

    public static boolean depositAllExcept(Filter<Item> filter) {
        boolean deposit = false;
        for (Item item : Inventory.getAllItems()) {

            if (item.isValid() && (filter == null || !filter.accept(item))) {

                deposit(item.getId(), 9999999);

                deposit = true;
            }
        }
        return deposit;
    }

    public static boolean open() {
        //GameObject o = GameObjects.getNearbyBank();
        NPC n = Npcs.getNearbyBanker();
        if (!Bank.isOpen()) {
            if (n.isValid()) {
                n.interact("Bank");
                Condition.wait(new Condition.Check() {
                    public boolean poll() {
                        return Bank.isOpen();
                    }
                }, 100, 20);
                return true;
            }
        }

        return false;
    }
}
