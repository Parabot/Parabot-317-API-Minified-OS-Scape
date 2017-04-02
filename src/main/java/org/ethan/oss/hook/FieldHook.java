package org.ethan.oss.hook;

public class FieldHook {
    String clazz;
    String field;
    int    mult;

    public FieldHook(String clazz, String field, int getterMult) {
        this.clazz = clazz;
        this.field = field;
        this.mult = getterMult;

    }

}