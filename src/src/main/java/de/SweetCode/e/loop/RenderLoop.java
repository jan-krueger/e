package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.EScreen;

import java.util.concurrent.TimeUnit;

public class RenderLoop implements Runnable {

    private final EScreen screen;

    private final long optimalTime;
    private boolean isRunning = true;
    private int currentFPS = 0;

    public RenderLoop(EScreen screen, long optimalTime) {
        this.screen  = screen;
        this.optimalTime = optimalTime;
    }

    public void stop() {
        this.isRunning = false;
    }

    public int getCurrentFPS() {
        return this.currentFPS;
    }

    @Override
    public void run() {

        long lastIteration = System.nanoTime();
        long lastFrameTime = 0;

        int tmpFPS = 0;

        while (this.isRunning) {

            long now = System.nanoTime();
            long updateLength = now - lastIteration;
            lastIteration = now;

            // update the frame counter
            lastFrameTime += updateLength;
            tmpFPS++;

            if (lastFrameTime >= E.NANO_SECOND) {
                this.currentFPS = tmpFPS;
                lastFrameTime = 0;
                tmpFPS = 0;
            }

            E.getE().getScenes().forEach((k, v) -> {

                if (v.getGameScene().isActive()) {
                    this.screen.invalidate();
                    this.screen.repaint();
                }

            });

            try {
                long delay = TimeUnit.NANOSECONDS.toMillis(this.optimalTime - (System.nanoTime() - now));
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


}
