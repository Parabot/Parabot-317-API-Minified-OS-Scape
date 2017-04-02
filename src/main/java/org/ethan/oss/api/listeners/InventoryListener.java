package org.ethan.oss.api.listeners;

import org.ethan.oss.api.methods.Item;

public interface InventoryListener {

    void onItem(Item item);

}