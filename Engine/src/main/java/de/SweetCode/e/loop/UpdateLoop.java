package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.GameComponentEntry;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * <p>
 * The update loop is responsible for updating all active {@link de.SweetCode.e.GameComponent GameComponents}.
 * </p>
 */
public class UpdateLoop extends Loop {

    private final Input input;

    /**
     * <p>
     *    Creates a new UpdateLoop.
     * </p>
     *
     * @param input The input instance which provides all input.
     * @param optimalTime The time between each call in {@link TimeUnit#NANOSECONDS}.
     */
    public UpdateLoop(Input input, long optimalTime) {
        super("Update Loop", optimalTime);

        this.input = input;

    }

    @Override
    public void tick(long updateLength) {

        Settings settings = E.getE().getSettings();

        // update length to required delta unit
        long delta = Math.max(
                settings.getDeltaUnit().convert(updateLength, TimeUnit.NANOSECONDS),
                (settings.roundDelta() ? 1 : 0)
        );

        // get the input
        InputEntry input = this.input.build();
        long now = System.currentTimeMillis();

        //--- Depending on what the developer chose, we gonna use a sequential or parallelized stream.
        Stream<GameComponentEntry> stream = (
            settings.isParallelizingUpdate() ?
                    E.getE().getGameComponents().parallelStream() : E.getE().getGameComponents().stream()
        );
        stream.forEach(k -> {

            if(k.getGameComponent().isActive()) {
                k.getGameComponent().update(
                    // the input since the last call
                    input,
                    // Delta + iterationTime -> We do this to make the delta more accurate
                    delta + (System.currentTimeMillis() - now)
                );
            }

        });

        //--- After finishing all updates, we can clear the queues.
        this.input.clear();

    }

}
