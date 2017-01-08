package de.SweetCode.e.loop;

import de.SweetCode.e.E;

/**
 * <p>
 * A Loop is a concept of frequently called methods. They are used heavily internal by the engine to easily manage its loops.
 * </p>
 */
public abstract class Loop implements Runnable {

    //--- Internals
    private final String name;
    private final long optimalIterationTime;

    private boolean isRunning = false;
    private int currentTicks = 0;
    //---

    //--- Update Metrics
    private long lastIteration = System.nanoTime();
    private long lastIterationTime = 0;

    private int TMP_TICKS = 0;
    //---

    /**
     * <p>
     *    Creates a new loop.
     * </p>
     *
     * @param name The internal name of the loop used e.g. in debug messages to make it easier to identify them for a human.
     * @param optimalIterationTime The time in {@link java.util.concurrent.TimeUnit#NANOSECONDS} between each update call.
     */
    Loop(String name, long optimalIterationTime) {
        this.name = name;
        this.optimalIterationTime = Math.max(optimalIterationTime, 1);
    }

    /**
     * <p>
     *    Gives the name of the loop which can be used in debug information. Easily readable for humans.
     * </p>
     *
     * @return The name of the loop.
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>
     *    The amount of updates in the last full second.
     * </p>
     *
     * @return The current tick rate.
     */
    public int getCurrentTicks() {
        return this.currentTicks;
    }

    /**
     * <p>
     *    The optimal time between calls of the {@link Loop#tick(long)} method in {@link java.util.concurrent.TimeUnit#NANOSECONDS}.
     * </p>
     *
     * @return The time between to calls.
     */
    public long getOptimalIterationTime() {
        return this.optimalIterationTime;
    }

    @Override
    public void run() {

        long now = System.nanoTime();
        long updateLength = now - this.lastIteration;
        this.lastIteration = now;

        this.lastIterationTime += updateLength;
        this.TMP_TICKS++;

        if(this.lastIterationTime >= E.C.SECOND_AS_NANO) {
            this.currentTicks = this.TMP_TICKS;
            this.lastIterationTime = 0;
            this.TMP_TICKS = 0;
        }

        this.tick(updateLength);

    }

    /**
     * <p>
     *    This method gets called automatically by the loop. The frequency of calls depends on the optimal iteration time.
     * </p>
     *
     * @param updateLength Time in {@link java.util.concurrent.TimeUnit#NANOSECONDS} since the last call.
     */
    public abstract void tick(long updateLength);

}
