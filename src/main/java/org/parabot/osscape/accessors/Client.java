package org.parabot.osscape.accessors;

public interface Client {
    Player[] getLocalPlayers();

    Player getLocalPlayer();

    int getCameraZ();
}
