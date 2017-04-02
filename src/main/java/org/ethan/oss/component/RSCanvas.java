package org.ethan.oss.component;

import org.ethan.oss.component.debug.*;
import org.ethan.oss.interfaces.PaintListener;
import org.ethan.oss.utils.Condition;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RSCanvas extends java.awt.Canvas {

    private static final long                serialVersionUID = 1L;
    private final        BufferedImage       clientBuffer     = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
    private final        BufferedImage       gameBuffer       = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
    private final        int                 FRAMESTEP        = 1000 / 60;
    private              List<PaintListener> listeners        = new ArrayList<>();
    private long timeTaken = 0;
    private long beginTime = 0;

    public RSCanvas() {
        super();
        final Debugger<?>[] debuggers = { new TextDebugger(), new PlayerDebugger(),
                new NpcDebugger(), new InventoryDebugger(), new DebugMouse(), new ObjectDebugger(),
                new GroundItemDebugger(), new SettingsDebugger(), new BankDebugger(), new WidgetDebugger()

        };
        Collections.addAll(listeners, debuggers);

    }

    public Graphics getGraphics() {
        beginTime = System.currentTimeMillis();
        final Graphics graphics = clientBuffer.getGraphics();
        graphics.drawImage(gameBuffer, 0, 0, null);

        for (PaintListener listener : getPaintListeners()) {
            if (listener instanceof Debugger) {
                final Debugger<?> debug = (Debugger<?>) listener;
                if (debug.activate()) {
                    debug.render((Graphics2D) graphics);
                }
            } else {
                listener.render((Graphics2D) graphics);
            }

        }
        graphics.dispose();

        final Graphics2D rend = (Graphics2D) super.getGraphics();
        rend.drawImage(clientBuffer, 0, 0, null);
        timeTaken = System.currentTimeMillis() - beginTime;
        Condition.sleep((int) (FRAMESTEP - timeTaken));
        return gameBuffer.getGraphics();
    }

    public synchronized List<PaintListener> getPaintListeners() {
        return listeners;
    }

}