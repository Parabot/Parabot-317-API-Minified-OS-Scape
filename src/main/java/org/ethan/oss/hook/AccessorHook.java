package org.ethan.oss.hook;

/**
 * @author JKetelaar
 */
public class AccessorHook {
    private String accessor;
    private String clazz;

    public AccessorHook(String accessor, String clazz) {
        this.accessor = accessor;
        this.clazz = clazz;
    }

    public String getClazz() {
        return clazz;
    }

    public String getAccessor() {
        return accessor;
    }
}
