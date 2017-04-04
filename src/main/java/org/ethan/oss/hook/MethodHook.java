package org.ethan.oss.hook;

public class MethodHook {
    String clazz;
    String field;
    String desc;

    public MethodHook(String clazz, String field, String desc) {
        this.clazz = clazz;
        this.field = field;
        this.desc = desc;

    }

    public String getClazz() {
        return clazz;
    }

    public String getField() {
        return field;
    }

    public String getDesc() {
        return desc;
    }
}