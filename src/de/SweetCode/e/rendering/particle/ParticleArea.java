package de.SweetCode.e.rendering.particle;

import de.SweetCode.e.E;
import de.SweetCode.e.Renderable;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.math.Vector2D;
import de.SweetCode.e.utils.Assert;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

public class ParticleArea implements Renderable {

    private final List<Particle> particles = new ArrayList<>();


    public ParticleArea(BoundingBox boundingBox, Vector2D vector2D, ParticleTypes particleType, Color color, long lifeSpan, int width, int amount) {

        Assert.assertNotNull(boundingBox);
        Assert.assertNotNull(particleType);
        Assert.assertTrue("The width of a particle cannot be less than 1.", width > 0);
        Assert.assertTrue("The width of a particle cannot be less than 2 if you want to spawn a particle just use the Particle object.", amount > 0);

        for(int i = 0; i < amount; i++) {

            Particle particle = new Particle(ParticleArea.getRandomLocation(boundingBox), vector2D, particleType, (color == null ? new Color(E.getE().getRandom(false).nextInt(255), E.getE().getRandom(false).nextInt(255), E.getE().getRandom(false).nextInt(255)) : color), lifeSpan, width);

            this.particles.add(particle);
            E.getE().getGameComponents().add(particle);

        }

    }

    public ParticleArea(BoundingBox boundingBox, Vector2D vector2D, ParticleTypes particleType, Color color, int width, int amount) {
        this(boundingBox, vector2D, particleType, color, -1, width, amount);
    }

    public ParticleArea(BoundingBox boundingBox, Vector2D vector2D, ParticleTypes particleType, int width, int amount) {
        this(boundingBox, vector2D, particleType, null, -1, width, amount);
    }

    public ParticleArea(BoundingBox boundingBox, Vector2D vector2D, Color color, int width, int amount) {
        this(boundingBox, vector2D, ParticleTypes.RANDOM, color, -1, width, amount);
    }

    public ParticleArea(BoundingBox boundingBox, Vector2D vector2D, int width, int amount) {
        this(boundingBox, vector2D, ParticleTypes.RANDOM, null, -1, width, amount);
    }


    @Override
    public void render(Graphics2D value) {}

    @Override
    public void update(InputEntry input, long delta) {

        if(!(this.particles.get(0).isActive())) {
            this.particles.clear();
        }

    }

    @Override
    public boolean isActive() {
        return (this.particles.size() > 0);
    }

    private static Location getRandomLocation(BoundingBox boundingBox) {

        Location min = boundingBox.getMin();
        Location max = boundingBox.getMax();

        Location relative = new Location(max.getX() - min.getX(), max.getY() - min.getY());

        double x = E.getE().getRandom(false).nextDouble() * relative.getX() + min.getX();
        double y = E.getE().getRandom(false).nextDouble() * relative.getY() + min.getY();

        return new Location(x, y);

    }

}
