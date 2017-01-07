package de.SweetCode.e;

import de.SweetCode.e.input.InputEntry;


public interface GameComponent {

    /**
     * Call to update the component.
     *
     * @param inputEntry InputEntry is a wrapper object containg all kinds of input since the last update call.
     * @param delta The time between now and the last call in the pre-defined {@link Settings}.
     */
    void update(InputEntry inputEntry, long delta);

    /**
     * If the method is marked as active (true) then the component expects updates.
     * @return If the retured value is true then the game loop will update this method by calling {@link #update(InputEntry, long)}.
     */
    boolean isActive();

}
