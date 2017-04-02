package org.ethan.oss.api.input;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;

import java.awt.event.KeyEvent;

public class Keyboard {

    public static void pressEnter() {
        press(KeyEvent.VK_ENTER);
        release(KeyEvent.VK_ENTER);
    }

    public static void press(int event) {
        ServerEngine.getInstance().getKeyboard().press(ServerEngine.getInstance().getKeyboard().create(KeyEvent.KEY_PRESSED, event, (char) event));
    }

    public static void release(int event) {
        ServerEngine.getInstance().getKeyboard().release(ServerEngine.getInstance().getKeyboard().create(KeyEvent.KEY_RELEASED, event, (char) event));
    }

    public static void type(char c) {
        ServerEngine.getInstance().getKeyboard().type(c);
    }

    public static void sendText(String text, boolean pressEnter, int minSleep, int maxSleep) {
        for (int i = 0; i < text.toCharArray().length; i++) {
            type(text.toCharArray()[i]);
            Condition.sleep(Random.nextInt(minSleep, maxSleep));
        }
        if (pressEnter) {
            pressEnter();
        }
    }

    public static void sendText(String text, boolean pressEnter) {
        for (int i = 0; i < text.toCharArray().length; i++) {
            type(text.toCharArray()[i]);
            Condition.sleep(100);
        }

        if (pressEnter) {
            pressEnter();
        }
    }
}