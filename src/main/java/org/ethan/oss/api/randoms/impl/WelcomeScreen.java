package org.ethan.oss.api.randoms.impl;

import org.ethan.oss.api.input.Mouse;
import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.randoms.RandomEvent;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;

import java.awt.*;

public class WelcomeScreen extends RandomEvent {

    private final Rectangle WELCOME = new Rectangle(272, 293, 227, 89);
    private       String    name    = "WelcomeScreen";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return "Ethan";
    }

    @Override
    public boolean active() {
        return Widgets.getWidgetWithText("CLICK HERE TO PLAY") != null && Widgets.getWidgetWithText("CLICK HERE TO PLAY").isVisible();
    }

    @Override
    public void solve() {
        Mouse.move(Utilities.generatePoint(WELCOME));
        Condition.sleep(Random.nextInt(50, 650));
        Mouse.click(true);
    }

    @Override
    public void reset() {
        name = "WelcomeScreen";
    }

}