package org.ethan.oss.api.callbacks;

import org.ethan.oss.api.listeners.MessageEvent;
import org.ethan.oss.script.ScriptEngine;

public class MessageCallBack {

    public static final void messageListenerHook(int type, String name, String message) {
        final MessageEvent messageEvent = new MessageEvent(type, name, message);
        ScriptEngine.getInstance().dispatch(messageEvent);
    }
}
