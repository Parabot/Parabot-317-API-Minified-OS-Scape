package org.ethan.oss.api.input;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.methods.Calculations;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InternalMouse implements MouseListener, MouseMotionListener {

    private final MouseListener       mouseListenerDispatcher;
    private final MouseMotionListener mouseMotionDispatcher;
    private final ServerEngine engine = ServerEngine.getInstance();
    private Component component;
    private int       clientX;
    private int       clientY;
    private int  clientPressX    = -1;
    private int  clientPressY    = -1;
    private long clientPressTime = -1;
    private boolean clientPressed;
    private MouseAlgorithm mouseAlgorithm = new LMouse();

    public InternalMouse() {
        this.component = engine.getCanvas();
        this.mouseListenerDispatcher = component.getMouseListeners()[0];
        this.mouseMotionDispatcher = component.getMouseMotionListeners()[0];

        component.addMouseListener(this);
        component.addMouseMotionListener(this);

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clientX = e.getX();
        clientY = e.getY();
        mouseListenerDispatcher.mouseClicked(new MouseEvent(component, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, clientX, clientY, 1, false, e.getButton()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        press(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clientPressX = e.getX();
        clientPressY = e.getY();
        clientPressTime = System.currentTimeMillis();
        clientPressed = false;
        release(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        Point p = engine.getCanvas().getMousePosition();
        if (p != null && p.x > 0 && p.y > 0) {
            if (!ServerEngine.getInstance().isDisableMouse()) {
                clientX = p.x;
                clientY = p.y;
                move(clientX, clientY);
            }
        } else if (arg0.getX() > 0 && arg0.getY() > 0) {
            clientX = arg0.getX();
            clientY = arg0.getY();
            move(clientX, clientY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = engine.getCanvas().getMousePosition();
        if (p != null && p.x > 0 && p.y > 0 && engine.getCanvas() != null && Utilities.isPointValid(p)) {
            if (!ServerEngine.getInstance().isDisableMouse()) {
                clientX = p.x;
                clientY = p.y;
                move(clientX, clientY);
            }
        } else if (e.getX() > 0 && e.getY() > 0 && Utilities.isPointValid(new Point(e.getX(), e.getY()))) {
            clientX = e.getX();
            clientY = e.getY();
        }
        move(clientX, clientY);
    }

    public int getX() {
        return clientX;
    }

    public int getY() {
        return clientY;
    }

    public int getPressX() {
        return clientPressX;
    }

    public int getPressY() {
        return clientPressY;
    }

    public long getPressTime() {
        return clientPressTime;
    }

    public boolean isPressed() {
        return clientPressed;
    }

    public Point getLocation() {
        return new Point(getX(), getY());
    }

    public void hop(int x, int y) {
        clientX = x;
        clientY = y;
        mouseMotionDispatcher.mouseMoved(
                new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
    }

    public void move(int x, int y) {
        if (ServerEngine.getInstance().isUseRandomMouse()) {
            Point destination   = new Point(x, y);
            Point mousePosition = getLocation();
            for (int i = 0; i < 5 && mousePosition.distance(destination) > 2; i++) {
                if (mousePosition.distance(destination) > 20 && ServerEngine.getInstance().isUseRandomMouse()) {
                    Point[] path = mouseAlgorithm.makeMousePath(mousePosition.x, mousePosition.y, destination.x,
                            destination.y);

                    for (Point p : path) {
                        mousePosition = p;
                        hop(mousePosition.x, mousePosition.y);
                        Condition.sleep(Random.nextInt(1, Random.nextInt(2, 3)));
                    }
                } else {
                    Point difference = new Point((int) (destination.getX() - mousePosition.getX()),
                            (int) (destination.getY() - mousePosition.getY()));
                    for (double Current = 0; Current < 1; Current += (4
                            / Math.sqrt(Math.pow(difference.getX(), 2) + Math.pow(difference.getY(), 2)))) {
                        mousePosition = new Point((int) mousePosition.getX() + (int) (difference.getX() * Current),
                                (int) mousePosition.getY() + (int) (difference.getY() * Current));
                        hop(mousePosition.x, mousePosition.y);
                        if (Calculations.distanceBetween(mousePosition, destination) < 10) {
                            Condition.sleep(Random.nextInt(2, 4));
                        } else {
                            Condition.sleep(Random.nextInt(1, 2));
                        }
                    }
                }
                Condition.sleep(Random.nextInt(40, 50));
                mousePosition = getLocation();
            }
        } else {
            clientX = x;
            clientY = y;
            mouseMotionDispatcher.mouseMoved(
                    new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
        }
    }

    public void click(int x, int y, int button) {
        press(x, y, button);
        Condition.sleep(Random.nextInt(20, 100));
        release(x, y, button);
        mouseListenerDispatcher.mouseClicked(new MouseEvent(component, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, clientX, clientY, 1, false, button));
    }

    public void press(int x, int y, int button) {
        mouseListenerDispatcher.mousePressed(new MouseEvent(component, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false, button));
    }

    public void release(int x, int y, int button) {
        mouseListenerDispatcher.mouseReleased(new MouseEvent(component, MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, x, y, 1, false, button));
    }

    public void click(boolean left) {
        click(clientX, clientY, (left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));
    }

    public boolean drag(int x1, int y1, int x2, int y2) {
        move(x1, y1);
        press(getX(), getY(), MouseEvent.BUTTON2);
        move(x2, y2);
        release(getX(), getY(), MouseEvent.BUTTON2);
        return getX() == x2 && getY() == y2 && !isPressed();
    }

    public boolean dragMouse(int x1, int y1, int x2, int y2) {
        move(x1, y1);
        press(getX(), getY(), MouseEvent.BUTTON2);
        move(x2, y2);
        release(getX(), getY(), MouseEvent.BUTTON2);

        return getX() == x2 && getY() == y2 && !isPressed();
    }

}