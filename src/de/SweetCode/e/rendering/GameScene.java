package de.SweetCode.e.rendering;

import de.SweetCode.e.GameComponent;

public abstract class GameScene<T> implements GameComponent {

    public GameScene() {}

    /**
     * Called every update.
     * @param value
     */
    public abstract void render(T value);

}
