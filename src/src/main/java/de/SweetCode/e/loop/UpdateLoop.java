package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.GameComponentEntry;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class UpdateLoop extends Loop {

    private final Settings settings;
    private final Input input;

    public UpdateLoop(Settings settings, Input input, long optimalTime) {
        super(optimalTime);

        this.settings = settings;
        this.input = input;

        this.setRunning(true);
    }

    @Override
    public void tick(long updateLength) {

        // update length to required delta unit
        long delta = Math.max(
                this.settings.getDeltaUnit().convert(updateLength, TimeUnit.NANOSECONDS),
                (this.settings.roundDelta() ? 1 : 0)
        );

        // get the input
        InputEntry input = this.input.build();
        long now = System.currentTimeMillis();

        //--- Depending on what the developer chose, we gonna use a sequential or parallelized stream.
        Stream<GameComponentEntry> stream = (
                this.settings.isParallelizingUpdate() ?
                        E.getE().getGameComponents().parallelStream() : E.getE().getGameComponents().stream()
        );
        stream.forEach(k -> {

            System.out.println(k.getPriority().name());
            if(k.getGameComponent().isActive()) {
                k.getGameComponent().update(
                        // the input since the last call
                        input,
                        // Delta + iterationTime -> We do this to make the delta more accurate
                        delta + (System.currentTimeMillis() - now)
                );
            }

        });

        System.out.println("Delta Update: " + (System.currentTimeMillis() - now));

    }

}
