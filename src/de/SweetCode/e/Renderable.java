package de.SweetCode.e;

import java.awt.*;

public interface Renderable extends GameComponent {

    /**
     * Called every update.
     * @param value
     */
    public abstract void render(Graphics2D value);

}
