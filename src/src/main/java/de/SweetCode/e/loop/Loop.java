package de.SweetCode.e.loop;

import de.SweetCode.e.E;

import java.util.concurrent.TimeUnit;

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

    @Override
    public void run() {

        long lastIteration = System.nanoTime();
        long lastIterationTime = 0;

        int TMP_TICKS = 0;

        while (this.isRunning()) {

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

            try {
                long delay = TimeUnit.NANOSECONDS.toMillis(this.getOptimalIterationTime() - (System.nanoTime() - now));
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public abstract void tick(long updateLength);

}
