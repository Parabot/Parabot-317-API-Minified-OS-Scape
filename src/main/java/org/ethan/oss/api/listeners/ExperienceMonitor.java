package org.ethan.oss.api.listeners;

import org.ethan.oss.script.ScriptEngine;
import org.ethan.oss.utils.Condition;
import org.parabot.environment.scripts.Script;

import java.util.*;

public class ExperienceMonitor implements Runnable {

    private final List<ExperienceListener> listeners   = new ArrayList<>();
    private final SkillData                sd          = new SkillData();
    private final Map<Integer, Integer>    experiences = new HashMap<>();

    public void addListener(ExperienceListener... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    @Override
    public void run() {
        System.out.println("Started ExperienceMonitor...");
        while (ScriptEngine.getInstance().getScript() != null && ScriptEngine.getInstance().getScript().getState() != Script.STATE_STOPPED) {
            for (int i = 0; i < 24; i++) {
                final int currXp = sd.experience(i);
                final int lastXp = experiences.containsKey(i) ? experiences.get(i) : 0;
                if (currXp > lastXp) {
                    for (ExperienceListener listener : listeners) {
                        listener.onExperience(i, currXp - lastXp);
                    }
                    experiences.put(i, currXp);
                }
            }
            Condition.sleep(600);
        }

    }
}