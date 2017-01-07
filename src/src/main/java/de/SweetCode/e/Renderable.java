package de.SweetCode.e;

import de.SweetCode.e.rendering.layers.Layers;

public interface Renderable extends GameComponent {

    /**
     * Called every update.
     * @param layers A container object containg all layers that are know to the engine.
     */
    void render(Layers layers);

}
