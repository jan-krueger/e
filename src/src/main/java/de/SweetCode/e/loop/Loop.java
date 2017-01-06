package de.SweetCode.e.loop;

import de.SweetCode.e.E;

public abstract class Loop implements Runnable {

    //--- Internals
    private final long optimalIterationTime;

    private boolean isRunning = false;
    private int currentTicks = 0;
    //---

    //--- Update Metrics
    private long lastIteration = System.nanoTime();
    private long lastIterationTime = 0;

    private int TMP_TICKS = 0;
    //---

    public Loop(long optimalIterationTime) {
        this.optimalIterationTime = optimalIterationTime;
    }

    public int getCurrentTicks() {
        return this.currentTicks;
    }

    public long getOptimalIterationTime() {
        return this.optimalIterationTime;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }



    @Override
    public void run() {

        long now = System.nanoTime();
        long updateLength = now - this.lastIteration;
        this.lastIteration = now;

        this.lastIterationTime += updateLength;
        this.TMP_TICKS++;

        if(this.lastIterationTime >= E.NANO_SECOND) {
            this.currentTicks = this.TMP_TICKS;
            this.lastIterationTime = 0;
            this.TMP_TICKS = 0;
        }

        this.tick(updateLength);

    }

    public abstract void tick(long updateLength);

}
