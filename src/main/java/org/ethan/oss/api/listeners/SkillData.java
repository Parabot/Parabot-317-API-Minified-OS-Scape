package org.ethan.oss.api.listeners;

import org.ethan.oss.api.enums.Skills;
import org.ethan.oss.utils.BasicTimer;

public final class SkillData {

    public static final int NUM_SKILL = 24;

    public final int[] initialExp = new int[NUM_SKILL];

    public final int[] initialLevels = new int[NUM_SKILL];

    private BasicTimer timer;

    public SkillData() {
        this(new BasicTimer(0l));
    }

    public SkillData(BasicTimer timer) {
        for (int index = 0; index < NUM_SKILL; index++) {
            initialExp[index] = Skills.getExperience(index);
            initialLevels[index] = Skills.getRealLevel(index);
        }
        this.timer = timer == null ? new BasicTimer(0l) : timer;
    }

    /**
     * Calculates the experience gained for the given skill index.
     *
     * @param index the skill index
     *
     * @return the experience gained
     */
    public int experience(int index) {
        if (index < 0 || index > NUM_SKILL) {
            throw new IllegalArgumentException("0 > index < " + NUM_SKILL);
        }
        return Skills.getExperience(index) - initialExp[index];
    }

    /**
     * Calculates the experience gained for the given skill index at the given rate.
     *
     * @param rate  the rate in which to calculate the experience gained
     * @param index the skill index
     *
     * @return the experience gained at the given rate
     */
    public int experience(Rate rate, int index) {
        return (int) (experience(index) * rate.time / timer.getElapsed());
    }

    /**
     * Calculates the number of levels gained for the given skill index.
     *
     * @param index the skill index
     *
     * @return the number of levels gained
     */
    public int level(int index) {
        if (index < 0 || index > NUM_SKILL) {
            throw new IllegalArgumentException("0 > index < " + NUM_SKILL);
        }
        return Skills.getRealLevel(index) - initialLevels[index];
    }

    /**
     * Calculates the number of levels gained for the given skill index at the given rate.
     *
     * @param rate  the rate in which to calculate the number of levels gained
     * @param index the skill index
     *
     * @return the number of levels gained at the given rate
     */
    public int level(Rate rate, int index) {
        return (int) (level(index) * rate.time / timer.getElapsed());
    }

    /**
     * Calculates the time to level up at the given rate for the given skill index.
     *
     * @param rate  the rate in which to calculate the time to level up
     * @param index the skill index
     *
     * @return the estimated time to level up at the given rate
     */
    public long timeToLevel(Rate rate, int index) {
        double exp = experience(rate, index);
        if (exp == 0d) {
            return 0l;
        }
        return (long) (Skills.expTilNextLevel(index) / exp * rate.time);
    }

    public static enum Rate {
        MINUTE(60000d),
        HOUR(3600000d),
        DAY(86400000d),
        WEEK(604800000d);

        public final double time;

        Rate(double time) {
            this.time = time;
        }

        /**
         * Gets the time for this rate.
         *
         * @return this rate's time
         */
        public double getTime() {
            return time;
        }

    }

}