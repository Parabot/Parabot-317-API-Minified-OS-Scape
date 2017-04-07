package org.ethan.oss.script;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.listeners.*;
import org.ethan.oss.api.randoms.RandomEvent;
import org.ethan.oss.api.randoms.RandomEventHandler;
import org.parabot.environment.scripts.Script;
import org.parabot.osscape.api.interfaces.PaintListener;

import java.util.ArrayList;
import java.util.List;

public class ScriptEngine {
    public static ScriptEngine INSTANCE = new ScriptEngine();
    public RandomEventHandler randomEventHandler;
    public Script script = null;
    private Thread            randomEventsThread;

    private List<MessageListener> messageListeners = new ArrayList<MessageListener>();
    private PaintListener paintListener;
    private boolean       scriptPaused;

    public static ScriptEngine getInstance() {
        return INSTANCE;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(final Script script) {
        this.script = script;
    }

    public int getScriptState() {
        if (this.getScript() != null) {
            return getScript().getState();
        }
        return -1;
    }

    public void setScriptState(int state) {
        if (this.getScript() != null) {
            getScript().setState(state);
        }
    }

    public void unload() {
        ServerEngine.getInstance().getCanvas().getPaintListeners().remove(paintListener);
        clearMessageListeners();
        paintListener = null;
        this.script = null;
        this.randomEventsThread.interrupt();
        this.randomEventsThread = null;
        // if(ServerEngine.getInstance().isDisableMouse()) {
        // ServerEngine.getInstance().setDisableMouse(false);
        // ServerEngine.getInstance().getBotMenu().dMouse.setText("Disable
        // Mouse");
        // ServerEngine.getInstance().getBotMenu().dMouse.setName("Disable
        // Mouse");
        // }
    }

    public void init() {
        if (script == null) {
            throw new RuntimeException("Script is null");
        }
        if (script instanceof MessageListener) {
            addMessageListener((MessageListener) script);
        }

        if (script instanceof PaintListener) {
            paintListener = (PaintListener) script;
            ServerEngine.getInstance().getCanvas().getPaintListeners().add(paintListener);
        }
        if (randomEventHandler == null) {
            randomEventHandler = new RandomEventHandler();
            ServerEngine.getInstance().getCanvas().getPaintListeners().add(randomEventHandler);
        }
        for (RandomEvent randomEvent : randomEventHandler.randomEvents) {
            if (randomEvent != null) {
                randomEvent.setEnabled(true);
            }
        }
        this.randomEventsThread = new Thread(randomEventHandler);
        this.randomEventsThread.start();
        // if(!ServerEngine.getInstance().isDisableMouse()) {
        // ServerEngine.getInstance().setDisableMouse(true);
        // ServerEngine.getInstance().getBotMenu().dMouse.setText("Enable
        // Mouse");
        // ServerEngine.getInstance().getBotMenu().dMouse.setName("Enable
        // Mouse");
        // }
    }

    public void dispatch(MessageEvent event) {
        for (final MessageListener messageListener : messageListeners) {
            messageListener.messageReceived(event);
        }
    }

    public boolean isScriptPaused() {
        return scriptPaused;
    }

    public void setScriptPaused(boolean scriptPaused) {
        this.scriptPaused = scriptPaused;
    }

    public RandomEventHandler getRandomEventHandler() {
        return randomEventHandler;
    }

    public void addMessageListener(MessageListener messageListener) {
        messageListeners.add(messageListener);
    }

    public void removeMessageListener(MessageListener messageListener) {
        messageListeners.remove(messageListener);
    }

    public void clearMessageListeners() {
        messageListeners.clear();
    }
}
