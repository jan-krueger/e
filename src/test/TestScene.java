package test;

import de.SweetCode.e.input.KeyEntry;
import de.SweetCode.e.rendering.context.Graphics2DScene;

import java.awt.*;
import java.util.stream.Stream;

public class TestScene extends Graphics2DScene {

    public TestScene() {
    }

    @Override
    public void render(Graphics2D value) {
        value.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));

        value.fillRect(
                0, 0, 1280, 720
        );
    }

    @Override
    public void update(Stream<KeyEntry> input, long delta) {

    }

    @Override
    public boolean isActive() {
        return true;
    }


}
