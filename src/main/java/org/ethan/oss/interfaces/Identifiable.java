package org.ethan.oss.interfaces;

public interface Identifiable {

    public int getId();

    public static interface Query<Q> {
        public Q id(final int... ids);
    }

}