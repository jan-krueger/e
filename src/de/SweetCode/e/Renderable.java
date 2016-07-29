package de.SweetCode.e;

import de.SweetCode.e.rendering.layers.Layers;

public interface Renderable extends GameComponent {

    /**
     * Called every update.
     * @param layers
     */
    void render(Layers layers);

}
