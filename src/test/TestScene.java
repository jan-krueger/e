package test;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;

import java.awt.*;

public class TestScene extends GameScene {

    public TestScene() {

        //E.getE().getGameComponents().add(new ParticleArea(new BoundingBox(new Location(0, 0), new Location(1920, 1080)), new Vector2D(0, 0), true, 50, 100000));
        //for(int i = 0; i < 50; i++) {
          //  E.getE().getGameComponents().add(new ExplosionArea(new CircleBox(new Location(random.nextInt(1920), random.nextInt(1080)), 500), false, 1000, 10, 1000));
        //}
        //for(int i = 0; i < 20; i++) {
        //    E.getE().getGameComponents().add(new SpiralArea(new CircleBox(new Location(500, 500), 100), ParticleTypes.CIRCLE, Color.BLACK, 50, 50));
        //}
        //E.getE().getGameComponents().add(new HelixArea(new Location(500, 100), 900, 500, 5, 2));
    }

    @Override
    public void render(Graphics2D value) {

        /*value.setColor(Color.YELLOW);
        value.fillOval((int) tmp1.getCenter().getX(), (int) tmp1.getCenter().getY(), (int) (tmp1.getRadius() / 2), (int) (tmp1.getRadius() / 2));
        value.setColor(Color.BLACK);
        value.fillOval((int) tmp2.getCenter().getX(), (int) tmp2.getCenter().getY(), (int) (tmp2.getRadius() / 2), (int) (tmp2.getRadius() / 2));*/

    }

    @Override
    public void update(InputEntry input, long delta) {
        //System.out.println(E.getE().getCurrentFPS());
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
