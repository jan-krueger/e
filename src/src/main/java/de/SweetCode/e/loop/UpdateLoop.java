package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;

import java.util.concurrent.TimeUnit;

public class UpdateLoop extends Loop {

    private final Input input;

    public UpdateLoop(Input input, long optimalTime) {
        super(optimalTime);
        this.input = input;

        this.setRunning(true);
    }

    @Override
    public void tick(long updateLength) {

        // update length to required delta unit
        long delta = Math.max(E.getE().getSettings().getDeltaUnit().convert(updateLength, TimeUnit.NANOSECONDS), (E.getE().getSettings().roundDelta() ? 1 : 0));

        // get the input
        InputEntry input = this.input.build();
        long now = System.currentTimeMillis();

        E.getE().getGameComponents().forEach(k -> {

            if(k.getGameComponent().isActive()) {
                k.getGameComponent().update(
                        // the input since the last call
                        input,
                        // Delta + iterationTime -> We do this to make the delta more accurate
                        delta + (System.currentTimeMillis() - now)
                );
            }

        });

    }

}
