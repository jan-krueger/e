package test;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.math.Vector2D;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.particle.ParticleArea;

import java.awt.*;

public class TestScene extends GameScene {


    public TestScene() {

        E.getE().getGameComponents().add(new ParticleArea(new BoundingBox(new Location(0, 0), new Location(1000, 1000)), new Vector2D(0.000001, 0.000001), 5, 1000000));

    }

    @Override
    public void render(Graphics2D value) {

    }

    @Override
    public void update(InputEntry input, long delta) {
    }

    @Override
    public boolean isActive() {
        return false;
    }

}
