package de.SweetCode.e;

import de.SweetCode.e.input.InputEntry;

public interface GameComponent {

    /**
     * Will be called each game loop iteration.
     * @param input The keyboard input since the last call.
     */
    void update(InputEntry input, long delta);

    /**
     * If the method returns true the loop will call the update method otherwise
     * it won't call the update method in the iteration.
     * @return
     */
    boolean isActive();

}
