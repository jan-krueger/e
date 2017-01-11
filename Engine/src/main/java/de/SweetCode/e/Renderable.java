package de.SweetCode.e;

import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.Priority;
import de.SweetCode.e.rendering.layers.Layers;

/**
 * <p>
 * The Renderable is a extended {@link GameComponent} and gets expanded by the {@link Renderable#render(Layers)} method.
 * This interface is designed to give objects the ability to be added to the {@link de.SweetCode.e.loop.RenderLoop} by
 * calling {@link E#addScene(GameScene)} or {@link E#addComponent(GameComponent, Priority)}.
 * </p>
 */
public interface Renderable extends GameComponent {

    /**
     * <p>
     *    This method gets called by the {@link de.SweetCode.e.loop.RenderLoop} if active. The frequency of the render
     *    calls depends on {@link Settings#getTargetFPS()}.
     * </p>
     * @param layers A container object containg all layers that are know to the engine.
     */
    void render(Layers layers);

}
