package org.ethan.oss.api.randoms;

import org.ethan.oss.api.randoms.impl.Login;
import org.ethan.oss.api.randoms.impl.ModDetection;
import org.ethan.oss.api.randoms.impl.WelcomeScreen;
import org.ethan.oss.interfaces.PaintListener;
import org.ethan.oss.script.ScriptEngine;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.parabot.environment.scripts.Script;

import java.awt.*;

public class RandomEventHandler implements Runnable, PaintListener {

    public static final int loginHandler = 0;

    public final RandomEvent[] randomEvents;
    private final Color   BACKGROUND_COLOR = new Color(Color.black.getRed(), Color.black.getGreen(),
            Color.black.getBlue(), 40);
    public        boolean isActive         = false;
    public  RandomEvent activeEvent;
    private Login       login;

    public RandomEventHandler() {
        randomEvents = new RandomEvent[]{
                login = new Login(), new WelcomeScreen(), new ModDetection()
        };
    }

    public static void enableRandom(int i, boolean enable) {
        RandomEvent randomEvent = ScriptEngine.getInstance().getRandomEventHandler().randomEvents[i];
        randomEvent.setEnabled(enable);
    }

    @Override
    public void run() {
        while (ScriptEngine.getInstance().getScript().getState() == Script.STATE_RUNNING) {
            try {
                for (RandomEvent randomEvent : randomEvents) {
                    if (randomEvent != null && randomEvent.isEnabled() && randomEvent.active()) {
                        if (!randomEvent.getName().equals("Random Behavior")) {
                            System.out.println("Started RandomEvent: " + randomEvent.getName());
                        }
                        setActive(true);
                        activeEvent = randomEvent;
                        ScriptEngine.getInstance().getScript().setState(Script.STATE_PAUSE);
                        while (randomEvent.active() && ScriptEngine.getInstance().getScript().getState() == Script.STATE_PAUSE) {

                            randomEvent.solve();
                            Condition.sleep(Random.nextInt(50, 100));
                        }
                        ScriptEngine.getInstance().getScript().setState(Script.STATE_RUNNING);
                        activeEvent = null;
                        setActive(false);
                        randomEvent.reset();
                        if (!randomEvent.getName().equals("Random Behavior")) {
                            System.out.println("Completed RandomEvent: " + randomEvent.getName());
                        }
                    }
                }
                Condition.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void render(Graphics2D graphics) {
        if (!ScriptEngine.getInstance().isScriptPaused() || ScriptEngine.getInstance().isScriptPaused()) {
            if (activeEvent != null && !activeEvent.getName().equals("Random Behavior")) {
                graphics.setColor(BACKGROUND_COLOR);
                graphics.fillRect(0, 0, 765, 503);
                graphics.setColor(Color.WHITE);
                graphics.drawString("Random: " + activeEvent.getName(), 351, 20);
                graphics.drawString("Author: " + activeEvent.getAuthor(), 351, 35);

                if (activeEvent instanceof PaintListener) {
                    ((PaintListener) activeEvent).render(graphics);
                }
            }

        }
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}