import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;

public class TestScene extends GameScene {


    public TestScene() {}

    @Override
    public void render(Layers layers) {

        layers.first().g().setColor(Color.GREEN);
        layers.first().g().fillRect(0, 0, 800, 700);

    }

    @Override
    public void update(InputEntry input, long delta) {}

    @Override
    public boolean isActive() {
        return true;
    }
}
