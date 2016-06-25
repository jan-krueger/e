package de.SweetCode.e;

import de.SweetCode.e.input.KeyEntry;

import java.util.stream.Stream;

public interface GameComponent {

    /**
     * Will be called each game loop iteration.
     * @param input The input since the last call.
     * @param delta The time passed in nano seconds since the last iteration.
     */
    void update(Stream<KeyEntry> input, long delta);

    /**
     * If the method returns true the loop will call the update method otherwise
     * it won't call the update method in the iteration.
     * @return
     */
    boolean isActive();

}
