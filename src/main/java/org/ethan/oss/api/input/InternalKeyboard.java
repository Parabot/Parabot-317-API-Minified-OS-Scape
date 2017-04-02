package org.ethan.oss.api.input;

import org.ethan.oss.ServerEngine;
import org.parabot.core.Context;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InternalKeyboard implements KeyListener {

    private final Component   component;
    private final KeyListener keyDispatcher;

    @SuppressWarnings("unused")
    private KeyListener[] keyboardlistener;

    public InternalKeyboard() {
        this.component = ServerEngine.getInstance().getCanvas();
        this.keyboardlistener = component.getKeyListeners();
        this.keyDispatcher = component.getKeyListeners()[0];
        for (final KeyListener keyListener : component.getKeyListeners()) {
            component.removeKeyListener(keyListener);
        }
        component.addKeyListener(this);
    }

    public final KeyEvent create(int id, int keyCode, char c) {
        return new KeyEvent(component, id, System.currentTimeMillis(), 0, keyCode, c,
                id != KeyEvent.KEY_TYPED ? KeyEvent.KEY_LOCATION_STANDARD : KeyEvent.KEY_LOCATION_UNKNOWN);
    }

    public void press(KeyEvent c) {
        component.dispatchEvent(c);
    }

    public void type(KeyEvent c) {
        component.dispatchEvent(c);
    }

    public void release(KeyEvent c) {
        component.dispatchEvent(c);
    }

    public void press(char c) {
        press(create(KeyEvent.KEY_PRESSED, (int) c, c));
    }

    public void type(char c) {
        component.dispatchEvent(create(KeyEvent.KEY_TYPED, 0, c));
    }

    public void release(char c) {
        component.dispatchEvent(create(KeyEvent.KEY_RELEASED, (int) c, c));
    }

    public KeyEvent createNew(KeyEvent old) {
        return new KeyEvent(component, old.getID(), old.getWhen(), old.getModifiers(), old.getKeyCode(),
                old.getKeyChar(), old.getKeyLocation());
    }

    public Component getFakeSource() {
        return Context.getInstance().getApplet();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyDispatcher.keyPressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {

        keyDispatcher.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyDispatcher.keyTyped(createNew(e));
    }

}