package org.parabot.osscape.api.wrapper;

import org.ethan.oss.api.input.Mouse;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.osscape.Loader;
import org.parabot.osscape.accessors.Actor;
import org.parabot.osscape.accessors.Npc;
import org.parabot.osscape.api.interfaces.Interactable;
import org.parabot.osscape.api.interfaces.Locatable;
import org.parabot.osscape.api.methods.*;
import org.parabot.osscape.api.wrapper.walking.Walking;
import org.parabot.osscape.api.wrapper.walking.pathfinder.Path;
import org.parabot.osscape.api.wrapper.walking.pathfinder.impl.RSMapPathFinder;

import java.awt.*;

/**
 * @author JKetelaar
 */
public abstract class Character implements Locatable, Interactable {
    private org.parabot.osscape.accessors.Actor accessor;
    private int                                 index;

    public Character(org.parabot.osscape.accessors.Actor accessor, int index) {
        this.accessor = accessor;
        this.index = index;
    }

    public int getLocalX() {
        return accessor.getWorldX();
    }

    public int getLocalY() {
        return accessor.getWorldY();
    }

    public int getX() {
        return ((getLocalX() >> 7) + Game.getBaseX());
    }

    public int getY() {
        return ((getLocalY() >> 7) + Game.getBaseY());
    }

    public int getQueueSize() {
        return accessor.getQueueSize();
    }

    public boolean isMoving() {
        return getQueueSize() > 0;
    }

    public int getHealth() {
        int hp = accessor.getHealth();
        if (hp == -1) {
            return 100;
        } else {
            return hp;
        }
    }

    public int getMaxHealth() {
        int hp = accessor.getMaxHealth();
        if (hp == -1) {
            return 100;
        } else {
            return hp;
        }
    }

    public int getOrientation() {
        return accessor.getActorOrientation();
    }

    public int getAnimation() {
        return accessor.getAnimation();
    }

    //iIiIIiiIII = groundItem object rend shit
    @Override
    public boolean isOnScreen() {
        Model m = getModel();
        return m != null && Utilities.inViewport(m.getRandomPoint());
    }

    @Override
    public Point getPointOnScreen() {
        return getLocation().getPointOnScreen();
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(getLocation());
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return (int) Calculations.distanceBetween(getLocation(), tile);
    }

    @Override
    public void draw(Graphics2D g, Color color) {
        getModel().draw(g, color);
    }

    @Override
    public void draw(Graphics2D g) {
        draw(g, Color.WHITE);
    }

    @Override
    public boolean turnTo() {
        return Camera.turnTo(this);
    }

    @Override
    public Tile getLocation() {
        return new Tile(getX(), getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Character other = (Character) obj;
        if (accessor == null) {
            if (other.accessor != null) {
                return false;
            }
        } else if (!accessor.equals(other.accessor)) {
            return false;
        }

        return index == other.index;
    }

    @Override
    public boolean canReach() {
        RSMapPathFinder pf   = new RSMapPathFinder();
        Path            path = pf.getPath(getX(), getY(), RSMapPathFinder.FULL);
        return path != null && path.getLength() > 0;
    }

    @Override
    public Point getInteractPoint() {
        Model bounds = getModel();
        if (bounds != null) {
            final Point p = bounds.getRandomPoint();
            if (bounds.contains(p)) {

                return p;
            } else {

                return bounds.getRandomPoint();
            }
        }
        return getPointOnScreen();
    }

    @Override
    public boolean interact(String action) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 2000) {
            if (this.isOnScreen()) {
                Point ip = null;
                ip = this.getInteractPoint();
                if (Mouse.getLocation().distance(ip) <= 5) {
                    ip = this.getInteractPoint();
                    Mouse.move(ip);
                    Condition.sleep(75);
                } else {
                    Mouse.move(ip);
                    Condition.sleep(75);
                }

                if (!org.parabot.osscape.api.methods.Menu.isOpen() && org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {

                    int index = org.parabot.osscape.api.methods.Menu.index(action, this.getName());
                    if (index == 0) {
                        if (org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
                            Condition.sleep(Random.nextInt(80, 150));
                            Mouse.click(true);
                            return true;
                        }
                    } else {
                        Mouse.click(false);
                        Condition.wait(new Condition.Check() {
                            public boolean poll() {
                                return org.parabot.osscape.api.methods.Menu.isOpen();
                            }
                        }, 100, 20);
                    }
                }
                if (org.parabot.osscape.api.methods.Menu.isOpen() && org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
                    int   index = org.parabot.osscape.api.methods.Menu.index(action, this.getName());
                    Point p     = org.parabot.osscape.api.methods.Menu.getSuitablePoint(index);
                    if (p.x > 5 && p.y > 5) {
                        Mouse.move(p.x, p.y);
                    }
                    if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
                        Condition.sleep(Random.nextInt(60, 100));
                        Mouse.click(true);
                        return true;
                    }
                } else if (org.parabot.osscape.api.methods.Menu.isOpen() && !org.parabot.osscape.api.methods.Menu.contains(action, this.getName())) {
                    int   index = org.parabot.osscape.api.methods.Menu.index("Cancel");
                    Point p     = org.parabot.osscape.api.methods.Menu.getSuitablePoint(index);
                    if (p.x > 5 && p.y > 5) {
                        Mouse.move(p.x, p.y);
                    }
                    if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
                        Condition.sleep(Random.nextInt(60, 100));
                        Mouse.click(true);
                        return true;
                    }
                }
            } else {
                if (this.distanceTo() >= 6) {
                    Walking.walkTo(this.getLocation(), true);
                } else {
                    this.turnTo();
                }
            }
        }
        return false;

    }

    @Override
    public boolean interact(String action, String option) {
        return false;

    }

    public abstract String getName();

    public boolean hasAction(String action) {
        if (this instanceof Npc) {
            String[] actions = ((Npc) this).getNpcDef().getNpcActions();
            for (String s : actions) {
                if (s != null && s.length() > 0) {
                    if (s.equalsIgnoreCase(action)) {
                        return true;
                    }
                }
            }
        } else if (this instanceof org.parabot.osscape.accessors.Player) {
            return false;
        }
        return false;
    }

    @Override
    public boolean click(boolean left) {
        Point interactingPoint = this.getInteractPoint();
        Model bounds           = getModel();
        for (int i = 0; i < 3; i++) {
            if (bounds == null || bounds.contains(Mouse.getLocation())) {
                Mouse.click(left);
                return true;
            }
            if (!bounds.contains(interactingPoint)) {
                interactingPoint = this.getInteractPoint();
            }
            Mouse.move(interactingPoint);
        }
        return false;
    }

    @Override
    public boolean click() {
        return click(true);
    }

    public boolean isInteractingWithUs() {
        if (!Game.isLoggedIn()) {
            return false;
        }

        if (this.getInteracting().equals(Players.getMyPlayer())) {
            return true;
        }
        return false;
    }

    public boolean isInteractingWithActor(org.parabot.osscape.api.wrapper.Character actor) {
        if (!Game.isLoggedIn()) {
            return false;
        }

        if (this.getInteracting().equals(actor)) {
            return true;
        }
        return false;
    }

    public boolean isInCombat() {
        int LoopCycleStatus = Game.getGameCycle() - 130;
        int hitCycle        = accessor.getCombatCycle();

        return LoopCycleStatus < hitCycle;
    }

    public String getOverHeadText() {
        return accessor.getSpokenText();
    }

    public boolean isInCombatWith(org.parabot.osscape.api.wrapper.Character actor) {
        if (!Game.isLoggedIn()) {
            return false;
        }

        if (this.getInteracting().equals(actor)) {
            if (actor.getInteracting().equals(this)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return
     *
     * @TODO
     */
    public final Character getInteracting() {
        int index = this.accessor.getInteractingIndex();
        if (index != -1 && index < 32768) {
            return new org.parabot.osscape.api.wrapper.Npc(Loader.getClient().getLocalNpcs()[index], index);
        } else if (index >= 32768) {
            index -= 32768;
            try {
                if (Loader.getClient().getLocalPlayers()[index] == null) {
                    return Players.getMyPlayer();
                }
                return new Player(Loader.getClient().getLocalPlayers()[index], index);
            } catch (Throwable t) {
                return Players.getMyPlayer();
            }
        }

        return null;
    }

    public Model getModel() {
        try {
            org.parabot.osscape.accessors.Model model = Models.get(accessor);

            if (model == null) {
                return null;
            }

            int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().getX() - Game.getBaseX()][getLocation().getY()
                    - Game.getBaseY()];
            if (this instanceof Npc) {
                Npc npc = (Npc) this;
                if (npc.getNpcDef().getNpcName() != null && npc.getNpcDef().getNpcName().toLowerCase().contains("fishing")) {
                    tileByte = 0;
                }
            }

            return new Model(model, getOrientation(), getLocalX(), getLocalY(), tileByte == 1 ? 210 : 0);
        } catch (Exception ignored) {
        }
        return null;
    }

    public Actor getAccessor() {
        return accessor;
    }
}
