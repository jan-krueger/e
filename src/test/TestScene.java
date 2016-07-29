package test;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;

public class TestScene extends GameScene{

    public TestScene() {


    }

    @Override
    public void render(Layers layers) {
        Graphics2D last = layers.last().getGraphics2D();

        last.setColor(Color.MAGENTA);
        last.drawString("HALLLO", 500, 500);

        Graphics2D first = layers.first().getGraphics2D();
        layers.first().setAlpha(0.3F);
        first.setColor(Color.BLUE);
        first.drawString("Hello, World!", 500, 500);
    }

    @Override
    public void update(InputEntry input, long delta) {
        //System.out.println("FPS: " + E.getE().getCurrentFPS() + " @ Game Objects: " + E.getE().getGameComponents().size());
        System.out.println(delta + " => " + E.getE().getSettings().getDeltaUnit());
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
