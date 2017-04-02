package org.ethan.oss.api.wrappers;

import org.ethan.oss.api.callbacks.ModelCallBack;
import org.ethan.oss.api.input.Mouse;
import org.ethan.oss.api.interactive.Camera;
import org.ethan.oss.api.interactive.Players;
import org.ethan.oss.api.methods.Calculations;
import org.ethan.oss.api.methods.Game;
import org.ethan.oss.api.methods.Menu;
import org.ethan.oss.api.methods.Walking;
import org.ethan.oss.api.pathfinder.Path;
import org.ethan.oss.api.pathfinder.impl.RSMapPathFinder;
import org.ethan.oss.interfaces.Interactable;
import org.ethan.oss.interfaces.Locatable;
import org.ethan.oss.reflection.ReflWrapper;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;

import java.awt.*;

public class Actor extends ReflWrapper implements Locatable, Interactable {

    private final Object raw;

    public Actor(Object raw) {
        this.raw = raw;
    }

    protected Object getRaw() {
        return raw;
    }

    public int getLocalX() {
        if (!isValid()) {
            return -1;
        }
        return (int) getFieldValue("WorldX", raw);
    }

    public int getLocalY() {
        if (!isValid()) {
            return -1;
        }
        return (int) getFieldValue("WorldY", raw);
    }

    public int getX() {
        if (!isValid()) {
            return -1;
        }
        return ((((int) getFieldValue("WorldX", raw)) >> 7) + (int) Game.getBaseX());
    }

    public int getY() {
        if (!isValid()) {
            return -1;
        }
        return ((((int) getFieldValue("WorldY", raw)) >> 7) + (int) Game.getBaseY());
    }

    public int getQueueSize() {
        if (!isValid()) {
            return -1;
        }
        return (int) getFieldValue("QueueSize", raw);
    }

    public boolean isMoving() {
        if (!isValid()) {
            return false;
        }
        return getQueueSize() > 0;
    }

    public int getHealth() {
        if (!isValid()) {
            return -1;
        }
        int hp = (int) getFieldValue("Health", raw);
        if (hp == -1) {
            return 100;
        } else {
            return hp;
        }
    }

    public int getMaxHealth() {
        if (!isValid()) {
            return -1;
        }
        int hp = (int) getFieldValue("MaxHealth", raw);
        if (hp == -1) {
            return 100;
        } else {
            return hp;
        }
    }

    public int getOrientation() {
        if (!isValid()) {
            return -1;
        }
        return (int) getFieldValue("ActorOrientation", raw);
    }

    public int getAnimation() {
        if (raw == null) {
            return -1;
        }
        return (int) getFieldValue("Animation", raw);
    }

    public boolean isValid() {
        return getRaw() != null;
    }

    //iIiIIiiIII = groundItem object rend shit
    @Override
    public boolean isOnScreen() {
        Model m = getModel();
        if (m == null) {
            return false;
        }
        if (Utilities.inViewport(m.getRandomPoint())) {
            return true;
        }
        return false;
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
        return (int) Calculations.distanceBetween(getLocation(), locatable.getLocation());
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
    public boolean equals(Object a) {
        if (a != null && a instanceof Actor) {
            Actor   t = (Actor) a;
            boolean x = this.getLocation().equals(t.getLocation()) && this.getAnimation() == t.getAnimation();
            if (t instanceof Player && this instanceof Player) {
                Player j = (Player) t;
                return x & j.getName().equals(((Player) this).getName());
            } else if (t instanceof NPC && this instanceof NPC) {
                NPC j = (NPC) t;
                return x & j.getId() == (((NPC) this).getId());
            }
            return false;
        }
        return false;
    }

    @Override
    public String toString() {

        return "TODO";
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

    @SuppressWarnings("unused")
    private void getInts() {
        getFieldValues(raw);
    }

    @Override
    public boolean interact(String action) {
        if (!this.isValid() || this == null) {
            return false;
        }
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 2000) {
            if (this.isOnScreen()) {
                Point ip = null;
                if (this.isValid()) {
                    ip = this.getInteractPoint();
                }
                if (Mouse.getLocation().distance(ip) <= 5) {
                    ip = this.getInteractPoint();
                    Mouse.move(ip);
                    Condition.sleep(75);
                } else {
                    Mouse.move(ip);
                    Condition.sleep(75);
                }

                if (this.isValid()) {
                    if (!Menu.isOpen() && Menu.contains(action, this.getName())) {

                        int index = Menu.index(action, this.getName());
                        if (index == 0) {
                            if (Menu.contains(action, this.getName())) {
                                Condition.sleep(Random.nextInt(80, 150));
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
                        int   index = Menu.index(action, this.getName());
                        Point p     = Menu.getSuitablePoint(index);
                        if (p.x > 5 && p.y > 5) {
                            Mouse.move(p.x, p.y);
                        }
                        if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
                            Condition.sleep(Random.nextInt(60, 100));
                            Mouse.click(true);
                            return true;
                        }
                    } else if (Menu.isOpen() && !Menu.contains(action, this.getName())) {
                        int   index = Menu.index("Cancel");
                        Point p     = Menu.getSuitablePoint(index);
                        if (p.x > 5 && p.y > 5) {
                            Mouse.move(p.x, p.y);
                        }
                        if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
                            Condition.sleep(Random.nextInt(60, 100));
                            Mouse.click(true);
                            return true;
                        }
                    }
                }
            } else {
                if (this.distanceTo() >= 6) {
                    Walking.walkTo(this.getLocation(), true);
                    continue;
                } else {
                    this.turnTo();
                    continue;
                }
            }
        }
        return false;

    }

    @Override
    public boolean interact(String action, String option) {
        return false;

    }

    public String getName() {
        String name = null;
        if (this instanceof NPC) {
            name = ((NPC) this).getName();
        } else if (this instanceof Player) {
            name = ((Player) this).getName();
        }
        return name;
    }

    public boolean hasAction(String action) {
        if (this instanceof NPC) {
            String[] actions = ((NPC) this).getActions();
            for (String s : actions) {
                if (s != null && s.length() > 0) {
                    if (s.equalsIgnoreCase(action)) {
                        return true;
                    }
                }
            }
        } else if (this instanceof Player) {
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
            if (bounds == null || !bounds.contains(interactingPoint)) {
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
        if (!isValid() || !Game.isLoggedIn()) {
            return false;
        }

        if (this.getInteracting().isValid() && Players.getLocal().isValid()) {
            if (this.getInteracting().equals(Players.getLocal())) {
                return true;
            }
        }
        return false;
    }

    public boolean isInteractingWithActor(Actor actor) {
        if (!isValid() || !Game.isLoggedIn()) {
            return false;
        }

        if (this.getInteracting().isValid()) {
            if (this.getInteracting().equals(actor)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCombat() {
        if (raw == null) {
            return false;
        }
        int LoopCycleStatus = Game.getGameCycle() - 130;
        int hitCycle        = (int) getFieldValue("CombatCycle", raw);

        return LoopCycleStatus < hitCycle;
    }

    public String getOverHeadText() {
        if (raw == null) {
            return null;
        }
        return (String) getFieldValue("SpokenText", raw);
    }

    public boolean isInCombatWith(Actor actor) {
        if (!isValid() || !Game.isLoggedIn() || !actor.getInteracting().isValid() || !this.getInteracting().isValid()) {
            return false;
        }

        if (actor.getInteracting().isValid() && this.getInteracting().isValid()) {
            if (this.getInteracting().equals(actor)) {
                if (actor.getInteracting().equals(this)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Actor getInteracting() {
        if (raw == null) {
            return null;
        }
        int interactingIndex = (int) getFieldValue("InteractingIndex", raw);
        if (interactingIndex == -1) {
            return new Actor(null);
        }
        if (interactingIndex < 32768) {
            Object[] localNpcs = (Object[]) getFieldValue("LocalNpcs", null);
            if (localNpcs.length > interactingIndex) {
                return new NPC(localNpcs[interactingIndex]);
            }
        } else {
            interactingIndex -= 32768;
            Object[] localPlayers = (Object[]) getFieldValue("LocalPlayers", null);
            if (localPlayers.length > interactingIndex) {
                return new Player(localPlayers[interactingIndex]);
            }
        }
        return new Actor(null);
    }

    public Model getModel() {
        try {
            if (raw == null) {
                return null;
            }

            Model model = ModelCallBack.get(raw);

            if (model == null) {
                return null;
            }

            int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().x - Game.getBaseX()][getLocation().y
                    - Game.getBaseY()];
            if (this instanceof NPC) {
                NPC npc = (NPC) this;
                if (npc.isValid() && npc.getName() != null && npc.getName().toLowerCase().contains("fishing")) {
                    tileByte = 0;
                }
            }

            return new Model(model, getOrientation(), getLocalX(), (int) getLocalY(), tileByte == 1 ? 210 : 0);
        } catch (Exception e) {

        }
        return null;
    }

}