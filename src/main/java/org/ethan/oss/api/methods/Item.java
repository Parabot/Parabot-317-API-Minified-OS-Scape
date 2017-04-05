package org.ethan.oss.api.methods;

import org.ethan.oss.api.definitions.ItemDefinition;
import org.ethan.oss.api.enums.Tab;
import org.ethan.oss.api.input.Mouse;
import org.parabot.osscape.api.interfaces.Identifiable;
import org.parabot.osscape.api.interfaces.Interactable;
import org.parabot.osscape.api.interfaces.Nameable;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;

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
            menuIndex = Menu.index(action, option);
            Point interactPoint = getInteractPoint();
            if (interactPoint == null) {
                return false;
            }
            if (menuIndex > -1 && getArea().contains(Mouse.getLocation())) {
                break;
            }
            if (Menu.isOpen() && menuIndex == -1) {
                Menu.interact("Cancel");
            }
            Mouse.move(interactPoint);
            Condition.sleep(Random.nextInt(100, 150));
        }
        return menuIndex > -1 && Menu.interact(action, option);
    }

    @Override
    public boolean interact(String action) {
        if (!this.isValid() || this == null) {
            return false;
        }

        if (!Tab.INVENTORY.isOpen()) {
            Tab.INVENTORY.open();
            Condition.wait(new Condition.Check() {
                public boolean poll() {
                    return Tab.INVENTORY.isOpen();
                }
            }, 100, 20);
        }
        if (Menu.isOpen() && !Menu.contains(action, this.getName())) {
            Menu.interact("Cancel");
        }

        Point ip = null;
        if (this.isValid()) {
            ip = this.getInteractPoint();
        }

        if (Mouse.getLocation().distance(ip) <= 5 && !Menu.contains(action, this.getName())) {
            ip = this.getInteractPoint();
            Mouse.move(ip);
        } else {
            if (!Menu.contains(action, this.getName())) {
                Mouse.move(ip);
            }
        }

        if (this.isValid()) {
            if (!Menu.isOpen() && Menu.contains(action, this.getName())) {
                int index = Menu.index(action, this.getName());
                if (index == 0) {
                    if (Menu.contains(action, this.getName())) {
                        Condition.sleep(Random.nextInt(20, 100));
                        Mouse.click(true);
                        return true;
                    }
                } else {
                    Mouse.click(false);
                    Condition.wait(new Condition.Check() {
                        public boolean poll() {
                            return Menu.isOpen();
                        }
                    }, 100, 20);
                }
            }

            if (Menu.isOpen() && Menu.contains(action, this.getName())) {
                Menu.interact(action, this.getName());
            } else if (Menu.isOpen() && !Menu.contains(action, this.getName())) {
                Menu.interact("Cancel");
                Condition.wait(new Condition.Check() {
                    public boolean poll() {
                        return !Menu.isOpen();
                    }
                }, 100, 20);
            }
        }

        return false;

    }

    @Override
    public boolean click(boolean left) {
        Mouse.click(getInteractPoint(), left);
        return true;
    }

    @Override
    public boolean click() {
        Mouse.click(getInteractPoint(), true);
        return true;
    }

    public boolean click(Point p) {
        Mouse.click(p, true);
        return true;
    }

    @Override
    public String getName() {
        if (itemDefinition == null) {
            itemDefinition = new ItemDefinition(id);
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