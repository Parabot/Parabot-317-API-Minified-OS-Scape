package org.parabot.osscape.accessors;

public interface Actor {
    String getSpokenText();

    int getCombatCycle();

    int getActorOrientation();

    int getQueueSize();

    int getAnimation();

    int getMaxHealth();

    int getWorldX();

    int getWorldY();

    int getHealth();

    int getInteractingIndex();
}

