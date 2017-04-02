package org.ethan.oss.threads;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.input.InternalKeyboard;
import org.ethan.oss.api.input.InternalMouse;
import org.ethan.oss.utils.Condition;

public class HandleInputs implements Runnable {
    ServerEngine engine = ServerEngine.getInstance();

    @Override
    public void run() {
        System.out.println("Attempting to set mouse & keyboard.");

        while (engine.getCanvas() == null) {
            Condition.sleep(5);
        }
        Condition.wait(new Condition.Check() {
            public boolean poll() {
                return engine.getApplet().getComponents().length != 0;
            }
        }, 100, 20);

        engine.setMouse(new InternalMouse());
        engine.setKeyboard(new InternalKeyboard());
        System.out.println("Mouse & Keyboard set.");

    }

}