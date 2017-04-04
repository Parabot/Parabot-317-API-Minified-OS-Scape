package org.parabot.osscape.api.interfaces;

public interface Nameable {

    public String getName();

    public static interface Query<Q> {
        public Q name(final String... names);
    }

}

