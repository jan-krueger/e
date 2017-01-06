package de.SweetCode.e.loop;

import de.SweetCode.e.E;

public abstract class Loop implements Runnable {

    private final long optimalIterationTime;

    private boolean isRunning = false;
    private int currrentTicks = 0;

    public Loop(long optimalIterationTime) {
        this.optimalIterationTime = optimalIterationTime;
    }

    public int getCurrrentTicks() {
        return this.currrentTicks;
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

    long lastIteration = System.nanoTime();
    long lastIterationTime = 0;

    int TMP_TICKS = 0;

    @Override
    public void run() {

        long now = System.nanoTime();
        long updateLength = now - lastIteration;
        lastIteration = now;

        lastIterationTime += updateLength;
        TMP_TICKS++;

        if(lastIterationTime >= E.NANO_SECOND) {
            this.currrentTicks = TMP_TICKS;
            lastIterationTime = 0;
            TMP_TICKS = 0;
        }

        this.tick(updateLength);

    }

    public abstract void tick(long updateLength);

}
