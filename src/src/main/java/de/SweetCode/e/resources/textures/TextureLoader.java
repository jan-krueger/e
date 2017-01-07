package de.SweetCode.e.resources.textures;

import de.SweetCode.e.GameComponent;

import java.awt.*;

public interface TextureLoader extends GameComponent {

    /**
     * Loads everything and prepares the image loader for its mission.
     */
    void load();

    /**
     * Gets image by its index.
     * @param index The index of the image.
     * @return A reference to the {@link Image}.
     */
    Image get(int index);

}
