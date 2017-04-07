package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.methods.Bank;
import org.parabot.osscape.api.wrapper.Item;
import org.parabot.environment.api.utils.Filter;

import java.awt.*;

public class BankDebugger extends Debugger<Item> {

    private Filter<Item> filter = new Filter<Item>() {
        @Override
        public boolean accept(Item i) {
            return i.isValid() && i.getId() != 6512;
        }
    };

    @Override
    public Item[] elements() {
        return Bank.getAllItems(filter);
    }

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugBank() && Bank.isOpen();
    }

    @Override
    public void render(Graphics2D graphics) {
        final FontMetrics metrics = graphics.getFontMetrics();
        for (Item item : refresh()) {
            if (item.isValid()) {
                graphics.setColor(Color.GREEN);
                Point point = item.getCentralPoint();
                graphics.setFont(new Font("default", Font.BOLD, 14));
                String id = String.valueOf(item.getId());
                graphics.drawString(id,
                        point.x - (metrics.stringWidth(String.valueOf(item.getId())) / 2), point.y + 5);

            }
        }
    }
}