package org.ethan.oss.reflection;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.hook.Hook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ReflWrapper {

    public static Object getFieldValue(String str, Object instance) {

        try {

            Class<?> c     = ServerEngine.getInstance().loadClass(Hook.getInstance().getClass(str, true));
            Field    field = c.getDeclaredField(Hook.getInstance().getField(str, true));
            field.setAccessible(true);
            if (Hook.getInstance().getMult(str) != -1) {
                Integer decoded = (int) field.get(instance) * Hook.getInstance().getMult(str);
                return decoded;
            } else {
                return field.get(instance);
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getFieldValue(String clazz, String f, Object instance) {

        try {

            Class<?> c     = ServerEngine.getInstance().loadClass(clazz);
            Field    field = c.getDeclaredField(f);
            field.setAccessible(true);
            return field.get(instance);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getFieldValue(String clazz, String f, int mult, Object instance) {

        try {

            Class<?> c     = ServerEngine.getInstance().loadClass(clazz);
            Field    field = c.getDeclaredField(f);
            field.setAccessible(true);
            if (mult != -1) {
                Integer decoded = (int) field.get(instance) * mult;
                return decoded;
            } else {
                return field.get(instance);
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getFieldValues(Object obj) {
        try {
            Class<?> c = ServerEngine.getInstance().loadClass(Hook.getInstance().getClass("getAnimation", true));
            for (Field m : c.getDeclaredFields()) {
                if (m.getType().equals(int.class)) {
                    m.setAccessible(true);
                    int i = m.getInt(obj);
                    if (i == 0) {
                        System.out.println(m.getName() + " = " + i);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }

    public Object invokeMethod(String name, int id, Object obj) {
        try {
            Class<?> c = ServerEngine.getInstance().loadClass(Hook.getInstance().getClass("getItemName", true));
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(Hook.getInstance().getField("getItemName", true))) {
                    if (m.getParameterCount() == 1) {
                        m.setAccessible(true);
                        Object i = m.invoke(obj, id);
                        return i;
                    }

                }
            }
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object invokeObjectMethod(int id, Object obj) {
        try {
            Class<?> c = ServerEngine.getInstance().loadClass(Hook.getInstance().getClass("transform2", false));
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(Hook.getInstance().getField("transform2", false))) {
                    if (m.getParameterCount() == 1) {
                        m.setAccessible(true);
                        Object i = m.invoke(obj, id);
                        return i;
                    }

                }
            }
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setField(String name, Object set, Object instance) {
        try {
            Class<?> c = ServerEngine.getInstance().loadClass(Hook.getInstance().getClass(name, true));
            Field    f = c.getDeclaredField(Hook.getInstance().getField(name, true));
            if (f != null) {
                f.setAccessible(true);
                f.set(instance, set);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}