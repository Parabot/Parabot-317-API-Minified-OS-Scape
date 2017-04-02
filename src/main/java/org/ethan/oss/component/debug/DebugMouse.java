package org.ethan.oss.component.debug;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.input.Mouse;
import org.parabot.core.Context;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class DebugMouse extends Debugger<Object> {
    private static final Deque<Point> h = new LinkedList<Point>();

    @Override
    public boolean activate() {
        return ServerEngine.getInstance().isDebugMouse();
    }

    @Override
    public void render(Graphics2D g) {
        final Point p = Mouse.getLocation();

        if (p.x == -1 || p.y == -1) {
            return;
        }
        drawMouse(g, p);
        h.offerFirst(p);
        if (h.size() < 3) {
            return;
        }
        final Graphics2D      g2 = (Graphics2D) g;
        final double          u  = 10;
        int                   i  = -1;
        final Iterator<Point> e  = h.iterator();
        Point                 a  = e.next();

        while (e.hasNext() && ++i < u) {
            final Point b = e.next();
            g2.setColor(new Color(255, 255, 255, 200 - (int) (i / u * 200d)));
            g2.drawLine(a.x, a.y, b.x, b.y);
            a = b;
        }

        if (i == u) {
            h.pollLast();
        }

        g.setColor(Color.yellow);
        g.drawString("Location: " + (int) Mouse.getLocation().getX() + " , " + (int) Mouse.getLocation().getY(), 10, 30);

    }

    @Override
    public Object[] elements() {
        return new Object[0]; // ignored
    }

    private void drawMouse(final Graphics g, Point p) {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

        Context.getInstance().getApplet().setCursor(blankCursor);

        Color Purple    = Color.WHITE;
        Color LowPurple = Color.WHITE;
        Color Indigo    = Color.BLACK.darker();

        ((Graphics2D) g).setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        Graphics2D spinG    = (Graphics2D) g.create();
        Graphics2D spinGRev = (Graphics2D) g.create();
        Graphics2D g2d      = (Graphics2D) g;
        spinGRev.setColor(Purple);
        spinGRev.rotate(System.currentTimeMillis() % 2000d / 2000d * (-360d)
                * 2 * Math.PI / 180.0, p.x, p.y);
        spinGRev.setStroke(new BasicStroke(1));
        spinGRev.drawLine(p.x - 8, p.y, p.x + 8, p.y);
        spinGRev.drawLine(p.x, p.y - 8, p.x, p.y + 8);
        spinG.setColor(LowPurple);
        spinG.rotate(System.currentTimeMillis() % 2000d / 2000d * (360d) * 2
                * Math.PI / 180.0, p.x, p.y);
        spinG.drawLine(p.x - 6, p.y, p.x + 6, p.y);
        spinG.drawLine(p.x, p.y - 6, p.x, p.y + 6);
        g2d.setColor(Indigo);
        g2d.drawOval(p.x - 10, p.y - 10, 20, 20);
    }

}