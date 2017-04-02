package org.ethan.oss.api.listeners;

import org.ethan.oss.api.methods.Inventory;
import org.ethan.oss.api.methods.Item;
import org.ethan.oss.script.ScriptEngine;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.parabot.environment.scripts.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryMonitor implements Runnable {

    private final List<InventoryListener> listeners = new ArrayList<>();
    private Item[] lastItems;

    public InventoryMonitor() {
        lastItems = Inventory.getAllItems();
    }

    public void addListener(InventoryListener... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    @Override
    public void run() {
        System.out.println("Monitioring Inventory...");
        while (ScriptEngine.getInstance().getScript() != null && ScriptEngine.getInstance().getScript().getState() != Script.STATE_STOPPED) {

            final Item[] currentItems = Inventory.getAllItems();
            for (Item item : currentItems) {
                int previous = getCount(lastItems, item.getId());
                int current  = getCount(currentItems, item.getId());
                if (current != previous) {
                    for (InventoryListener listener : listeners) {
                        listener.onItem(new Item(item.getId(), current - previous, item.getIndex(), Item.Type.INVENTORY, item.getArea()));
                    }
                    break;
                }
            }
            lastItems = currentItems;
            Condition.sleep(Random.nextInt(500, 600));
        }
    }

    private int getCount(Item[] array, int itemId) {
        int count = 0;
        for (Item item : array) {
            if (item != Inventory.nil() && itemId == item.getId()) {
                count += item.getStackSize();
            }
        }
        return count;
    }
}