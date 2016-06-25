package de.SweetCode.e.rendering.context;

import de.SweetCode.e.input.KeyEntry;
import de.SweetCode.e.rendering.GameScene;

import java.awt.*;
import java.util.stream.Stream;

public abstract class Graphics2DScene extends GameScene<Graphics2D> {

    public Graphics2DScene() {}

    @Override
    public abstract void render(Graphics2D value);

    @Override
    public abstract void update(Stream<KeyEntry> input, long delta);

    @Override
    public abstract boolean isActive();

}
