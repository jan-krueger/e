package test;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;

public class TestScene extends GameScene {


    public TestScene() {
        //E.getE().addComponent(new ExplosionArea(E.getE().getLayers().last(), new CircleBox(new Location(540, 360), 200), false, 20000, 60, 50));
        //E.getE().addComponent(new HelixArea(E.getE().getLayers().first(), new Location(540, 0), 720, 50, 3, 2));
        //E.getE().addComponent(new SpiralArea(E.getE().getLayers().last(), new CircleBox(new Location(540, 360), 100), ParticleTypes.SQUARE, Color.YELLOW, 10, 5));
    }

    @Override
    public void render(Layers layers) {

        layers.first().g().setColor(Color.MAGENTA);
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
