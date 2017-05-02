package org.parabot.osscape.api.wrapper;

import org.ethan.oss.api.enums.Tab;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.osscape.api.interfaces.Identifiable;
import org.parabot.osscape.api.interfaces.Interactable;
import org.parabot.osscape.api.interfaces.Nameable;

import java.awt.*;

public class Item implements Identifiable, Nameable, Interactable {
    private int            index;
    private int            id;
    private int            stackSize;
    private Rectangle      area;
    private ItemDefinition itemDefinition;
    private Type           type;

    public Item(int id, int stackSize, int index, Type type, Rectangle area) {
        this.type = type;
        this.id = id;
        this.stackSize = stackSize;
        this.area = area;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Type getType() {
        return type;
    }

    public int getStackSize() {
        return stackSize;
    }

    public Rectangle getArea() {
        return area;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Point getInteractPoint() {
        Rectangle rect = area;
        if (rect == null) {
            return null;
        }
        return Utilities.generatePoint(rect);
    }

    @Override
    public boolean interact(String action, String option) {
        int menuIndex = -1;
        for (int i = 0; i < 5; i++) {
            menuIndex = org.parabot.osscape.api.methods.Menu.index(action, option);
            Point interactPoint = getInteractPoint();
            if (interactPoint == null) {
                return false;
            }
            if (menuIndex > -1 && getArea().contains(Mouse.getInstance().getPoint())) {
                break;
            }
            if (org.parabot.osscape.api.methods.Menu.isOpen() && menuIndex == -1) {
                org.parabot.osscape.api.methods.Menu.interact("Cancel");
            }
            Mouse.getInstance().moveMouse(interactPoint.x, interactPoint.y);
            Time.sleep(Random.nextInt(100, 150));
        }
        return menuIndex > -1 && org.parabot.osscape.api.methods.Menu.interact(action, option);
    }

    @Override
    public boolean interact(String action) {
        if (!Tab.INVENTORY.isOpen()) {
            Tab.INVENTORY.open();
            Condition.wait(new Condition.Check() {
                public boolean poll() {
                    return Tab.INVENTORY.isOpen();
                }
            }, 100, 20);
        }
        if (org.parabot.osscape.api.methods.Menu.isOpen() && !org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
            org.parabot.osscape.api.methods.Menu.interact("Cancel");
        }

        Point ip = this.getInteractPoint();

        if (Mouse.getInstance().getPoint().distance(ip) <= 5 && !org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
            ip = this.getInteractPoint();
            Mouse.getInstance().moveMouse(ip.x, ip.y);
        } else {
            if (!org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
                Mouse.getInstance().moveMouse(ip.x, ip.y);
            }
        }

        if (!org.parabot.osscape.api.methods.Menu.isOpen() && org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
            int index = org.parabot.osscape.api.methods.Menu.index(action, this.getName());
            if (index == 0) {
                if (org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
                    Condition.sleep(Random.nextInt(20, 100));
                    Mouse.getInstance().click(Mouse.getInstance().getPoint(), true);
                    return true;
                }
            } else {
                Mouse.getInstance().click(Mouse.getInstance().getPoint(), false);
                Condition.wait(new Condition.Check() {
                    public boolean poll() {
                        return org.parabot.osscape.api.methods.Menu.isOpen();
                    }
                }, 100, 20);
            }

            if (org.parabot.osscape.api.methods.Menu.isOpen() && org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
                org.parabot.osscape.api.methods.Menu.interact(action, this.getName());
            } else if (org.parabot.osscape.api.methods.Menu.isOpen() && !org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
                org.parabot.osscape.api.methods.Menu.interact("Cancel");
                Condition.wait(new Condition.Check() {
                    public boolean poll() {
                        return !org.parabot.osscape.api.methods.Menu.isOpen();
                    }
                }, 100, 20);
            }
        }

        return false;

    }

    @Override
    public boolean click(boolean left) {
        Mouse.getInstance().click(getInteractPoint(), left);
        return true;
    }

    @Override
    public boolean click() {
        Mouse.getInstance().click(getInteractPoint(), true);
        return true;
    }

    public boolean click(Point p) {
        Mouse.getInstance().click(p, true);
        return true;
    }

    @Override
    public String getName() {
        if (itemDefinition == null) {
            itemDefinition = ItemDefinition.getItemDefinition(id);
        }
        return itemDefinition.getName();
    }

    public boolean isValid() {
        return id > 0 && stackSize > 0;
    }

    public Point getCentralPoint() {
        return new Point((int) getArea().getCenterX(), (int) getArea().getCenterY());
    }

    public enum Type {
        INVENTORY, BANK, DEPOSIT_BOX, EQUIPMENT
    }
}