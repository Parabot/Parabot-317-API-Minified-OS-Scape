package org.ethan.oss.hook;

public class FieldHook {
    String clazz;
    String field;
    int    mult;
    String into;

    public FieldHook(String clazz, String field, int getterMult, String into) {
        this.clazz = clazz;
        this.field = field;
        this.mult = getterMult;
        this.into = into;
    }

    public String getClazz() {
        return clazz;
    }

    public String getField() {
        return field;
    }

    public int getMult() {
        return mult;
    }

    public String getInto() {
        return into;
    }
}