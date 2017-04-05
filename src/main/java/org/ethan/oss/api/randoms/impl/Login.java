package org.ethan.oss.api.randoms.impl;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.input.Keyboard;
import org.ethan.oss.api.input.Mouse;
import org.parabot.osscape.api.methods.Game;
import org.ethan.oss.api.randoms.RandomEvent;
import org.ethan.oss.script.ScriptEngine;
import org.ethan.oss.utils.Condition;
import org.ethan.oss.utils.Random;
import org.parabot.environment.scripts.Script;

import java.awt.*;

public class Login extends RandomEvent {

    private final Rectangle    USER_EXIST = new Rectangle(398, 278, 129, 12);
    private final Rectangle    USER_CANCEL = new Rectangle(397, 307, 135, 30);
    private       boolean      clicked = false;
    private       String       name = "Login";
    private       ServerEngine engine = ServerEngine.getInstance();

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
        return Game.getLoginState().equals(Game.LoginState.STATE_LOG_IN_SCREEN);
    }

    @Override
    public void solve() {

        if (!clicked) {
            setStatus("Clicking Cancel");
            Mouse.click(new Point(USER_CANCEL.x + Random.nextInt(0, USER_CANCEL.width), USER_CANCEL.y + Random.nextInt(0, USER_CANCEL.height)), true);

            setStatus("Clicking Existing User");
            Mouse.click(new Point(USER_EXIST.x + Random.nextInt(0, USER_EXIST.width), USER_EXIST.y + Random.nextInt(0, USER_EXIST.height)), true);
            Condition.sleep(Random.nextInt(2000, 4000));
            clicked = true;
        } else {
            setStatus("Entering Username.");
            if (!engine.getPass().equals("null") && !engine.getUser().equals("null")) {
                Keyboard.sendText(engine.getUser(), true, Random.nextInt(75, 95), Random.nextInt(110, 125));
                setStatus("Entering Password.");
                Keyboard.sendText(engine.getPass(), true, Random.nextInt(75, 95), Random.nextInt(110, 125));
            } else {
                ScriptEngine.getInstance().setScriptState(Script.STATE_STOPPED);
                reset();
            }
            for (int i = 0; i < 25 && active(); i++, Condition.sleep(Random.nextInt(100, 150))) {
                ;
            }
            clicked = false;
        }
    }

    @Override
    public void reset() {
        name = "Login";
        clicked = false;
    }

}