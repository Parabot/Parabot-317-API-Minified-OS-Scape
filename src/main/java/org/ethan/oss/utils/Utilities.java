package org.ethan.oss.utils;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.methods.Calculations;
import org.parabot.osscape.api.methods.Game;
import org.ethan.oss.component.debug.PlayerDebugger;
import org.ethan.oss.hook.Hook;
import org.ethan.oss.interfaces.StatePredicate;
import org.ethan.oss.parameters.OSScapeParameters;
import org.parabot.api.io.Directories;
import org.parabot.core.Context;

import java.applet.Applet;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

import static java.lang.Thread.currentThread;

public class Utilities {

    public static String getContentDirectory() {
        return Directories.getCachePath().getAbsolutePath();
    }

    public static String getContentName() {

        return "/gamepack.jar";
    }

    public static boolean inArray(String string, String[] strings) {
        if (string == null) {
            return false;
        }
        for (String s : strings) {
            if (s != null && s.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inArray(int i, int[] array) {
        for (int j : array) {
            if (j == i) {
                return true;
            }
        }
        return false;
    }

    public static URL getJarURL() {
        try {
            if (ServerEngine.getInstance().getDownloader() != null) {
                if (ServerEngine.getInstance().getDownloader().isFinished()) {
                    return new File(getContentDirectory() + getContentName()).toURI().toURL();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void handleOSScape(OSScapeParameters params, ClassLoader cl) {
        try {
            Class<?> loaderClass = cl.loadClass(Hook.getInstance().getClass("Loader", true));

            Object loaderClassInst = loaderClass.newInstance();

            Field loader = loaderClass.getDeclaredField(Hook.getInstance().getField("Loader", true));
            loader.setAccessible(true);
            loader.set(null, loaderClassInst);

            Field paramsField = loaderClass.getDeclaredField(Hook.getInstance().getField("Params", true));
            paramsField.setAccessible(true);
            paramsField.set(null, params.paramsString);

            Field prop = loaderClass.getDeclaredField(Hook.getInstance().getField("Properties", true));
            prop.setAccessible(true);

            prop.set(null, params.prop);

            Field ip = loaderClass.getDeclaredField(Hook.getInstance().getField("IpAddress", true));
            ip.setAccessible(true);
            ip.set(null, "93.158.237.2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void populateCheckedList(String fileName) {
        try {
            try {
                String line = ServerEngine.getInstance().getBr(fileName).readLine();
                while (line != null) {
                    if (!ServerEngine.getInstance().getPreCheckedNames().contains(line)) {
                        ServerEngine.getInstance().getPreCheckedNames().add(line);
                        System.out.println("added: " + line);
                    }
                    line = ServerEngine.getInstance().getBr(fileName).readLine();

                }
            } finally {
                PlayerDebugger.populated = true;
                ServerEngine.getInstance().getBr(fileName).close();
                ServerEngine.getInstance().getFstream(fileName).close();
                ServerEngine.getInstance().setBr(null);
                ServerEngine.getInstance().setFstream(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAccount(List<String> names2) {
        try {
            FileWriter fw = new FileWriter(getContentDirectory() + "/test.txt", true);

            for (String pass : names2) {
                fw.write(pass + System.lineSeparator());

            }
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public static boolean isPointValid(Point point) {
        final Rectangle GAME_SCREEN = new Rectangle(0, 0, Context.getInstance().getApplet().getWidth(),
                Context.getInstance().getApplet().getHeight());
        return GAME_SCREEN.contains(point);
    }

    public static boolean pointInViewport(final int x, final int y) {
        if (!Game.isLoggedIn()) {
            return false;
        }
        return Calculations.RESIZED_VIEWPORT.contains(new Point(x, y));
    }

    public static Dimension dimensions() {
        final Applet applet = Context.getInstance().getApplet();
        return applet != null ? new Dimension(applet.getWidth(), applet.getHeight()) : new Dimension(-1, -1);
    }

    public static Point generatePoint(Shape region) {
        Rectangle r = region.getBounds();
        double    x, y;
        do {
            x = r.getX() + r.getWidth() * Math.random();
            y = r.getY() + r.getHeight() * Math.random();
        } while (!region.contains(x, y));

        return new Point((int) x, (int) y);
    }

    public static boolean inViewport(final Point p) {
        if (!Game.isLoggedIn()) {
            return false;
        }
        return pointInViewport(p.x, p.y);
    }

    public static boolean sleepUntil(StatePredicate predicate, long timeOut) {
        BasicTimer timer   = new BasicTimer(timeOut);
        boolean    success = !predicate.apply();
        while (success && !currentThread().isInterrupted() && !timer.finished()) {
            try {
                Thread.sleep(Random.nextInt(10, 20));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
            success = !predicate.apply();
        }
        return !success;
    }

    public static boolean sleepWhile(StatePredicate predicate, long timeOut) {
        BasicTimer timer   = new BasicTimer(timeOut);
        boolean    success = predicate.apply();
        while (success && !currentThread().isInterrupted() && !timer.finished()) {
            try {
                Thread.sleep(Random.nextInt(10, 20));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
            success = predicate.apply();
        }
        return !success;
    }
}
