package test;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;

public class TestScene extends GameScene {

    @Override
    public void render(Layers layers) {
        layers.first().g().setColor(Color.RED);
        layers.first().g().fillRect(0, 0, 1280, 720);
    }

    @Override
    public void update(InputEntry input, long delta) {

    }

    @Override
    public boolean isActive() {
        return true;
    }

}
