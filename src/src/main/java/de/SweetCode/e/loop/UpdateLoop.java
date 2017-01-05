package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;

import java.util.concurrent.TimeUnit;

public class UpdateLoop implements Runnable {

    private final Input input;
    private boolean isRunning = true;
    private int currentTicks = 0;
    private long optimalTime;

    public UpdateLoop(Input input, long optimalTime) {
        this.input = input;
        this.optimalTime = optimalTime / 2;
    }

    public void stop() {
        this.isRunning = false;
    }

    public int getCurrentTicks() {
        return this.currentTicks;
    }

    @Override
    public void run() {

        long lastIteration = System.nanoTime();
        long lastTickTime = 0;
        int tmpTicks = 0;

        while (this.isRunning) {

            long now = System.nanoTime();
            long updateLength = now - lastIteration;
            lastIteration = now;

            // update the frame counter
            lastTickTime += updateLength;
            tmpTicks++;

            if (lastTickTime >= E.NANO_SECOND) {
                this.currentTicks = tmpTicks;
                lastTickTime = 0;
                tmpTicks = 0;
            }

            long delta = Math.max(E.getE().getSettings().getDeltaUnit().convert(updateLength, TimeUnit.NANOSECONDS), (E.getE().getSettings().roundDelta() ? 1 : 0));

            // get the input
            InputEntry input = new InputEntry(this.input.getKeyboardEntries(), this.input.getMouseEntries(), this.input.getMouseWheelEntries(), this.input.getMouseDraggedEntries(), this.input.getMouseMovedEntries(), this.input.getMouseReleasedQueue());

            E.getE().getGameComponents().forEach(k -> {

                if(k.getGameComponent().isActive()) {
                    k.getGameComponent().update(input, delta);
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
