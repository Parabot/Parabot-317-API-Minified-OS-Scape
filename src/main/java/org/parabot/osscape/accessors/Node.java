package org.parabot.osscape.accessors;

/**
 * @author JKetelaar
 */
public interface Node {
    Node getNext();

    Node getPrev();

    byte getUID();
}
