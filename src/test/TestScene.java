package test;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.CircleBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.rendering.particle.areas.ExplosionArea;

import java.awt.*;

public class TestScene extends GameScene {


    public TestScene() {
        E.getE().addComponent(new ExplosionArea(E.getE().getLayers().last(), new CircleBox(new Location(540, 360), 200), false, 20000, 60, 50));
    }

    @Override
    public void render(Layers layers) {

        Layer first = layers.first();
        Graphics2D g = first.getGraphics2D();
        g.setColor(Color.MAGENTA);
        g.drawString("Hello, World!", 540, 360);

        layers.last().setAlpha(0.5F);
    }

    @Override
    public void update(InputEntry input, long delta) {
        System.out.println("FPS: " + E.getE().getCurrentFPS() + " @ Game Objects: " + E.getE().getGameComponents().size());
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
