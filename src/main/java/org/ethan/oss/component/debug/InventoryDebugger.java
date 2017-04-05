package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.enums.Tab;
import org.ethan.oss.api.methods.Inventory;
import org.ethan.oss.api.methods.Item;
import org.parabot.environment.api.utils.Filter;
import org.parabot.osscape.api.methods.Game;

import java.awt.*;

public class InventoryDebugger extends Debugger<Item> {

    @Override
    public Item[] elements() {
        return Inventory.getAllItems(new Filter<Item>() {
            @Override
            public boolean accept(Item i) {
                return i.isValid() && i != null;
            }
        });
    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugInventory() && Game.isLoggedIn() && Tab.INVENTORY.isOpen();
    }

    @Override
    public void render(Graphics2D graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();

        for (Item i : refresh()) {
            if (i.isValid()) {
                graphics.setColor(Color.GREEN);
                Point point = i.getCentralPoint();
                graphics.setFont(new Font("default", Font.BOLD, 14));
                String id = String.valueOf(i.getId());
                graphics.drawString(id,
                        point.x - (metrics.stringWidth(String.valueOf(i.getId())) / 2), point.y + 5);
                graphics.drawRect(i.getArea().x, i.getArea().y, i.getArea().width, i.getArea().height);
            }
        }
    }

}