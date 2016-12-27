package de.SweetCode.e;

import de.SweetCode.e.input.InputEntry;


public interface GameComponent {

    /**
     * Call to update the component.
     */
    void update(InputEntry inputEntry, long delta);

    /**
     * If the method is marked as active (true) then the component expects updates.
     * @return
     */
    boolean isActive();

}
