package de.SweetCode.e;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.Priority;

/**
 * <p>
 * A game component is a component that can be added to the {@link de.SweetCode.e.loop.UpdateLoop} by calling {@link E#addComponent(GameComponent)}
 * or {@link E#addComponent(GameComponent, Priority)}. If it has been added it is part of the loop and will be called if active.
 * </p>
 */
public interface GameComponent {

    /**
     *  <p>
     *     The method will be called by the {@link de.SweetCode.e.loop.UpdateLoop}. The frequency of the method calls
     *     depends on {@link Settings#getTargetTicks()}.
     *  </p>
     *
     * @param inputEntry InputEntry is a wrapper object containg all kinds of input since the last update call.
     * @param delta The time between now and the last call in the pre-defined {@link Settings}.
     */
    void update(InputEntry inputEntry, long delta);

    /**
     * <p>
     *     The method determines if the {@link GameComponent#update(InputEntry, long)} gets called by the {@link de.SweetCode.e.loop.UpdateLoop}.
     *     You can determine on the fly if the component should receive an update or not.
     * </p>
     *
     * @return If the retured value is true then the game loop will update the game component.
     */
    boolean isActive();

}
