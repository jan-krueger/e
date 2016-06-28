package de.SweetCode.e.rendering.particle;

import de.SweetCode.e.E;
import de.SweetCode.e.Renderable;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.math.Vector2D;
import de.SweetCode.e.utils.Assert;

import java.awt.*;

public class Particle implements Renderable {

    private final Location location;
    private final Vector2D vector2D;
    private final Color color;
    private final int width;
    private final boolean endless;

    private ParticleTypes particleType;
    private long lifeSpan;

    public Particle(Location location, Vector2D vector2D, ParticleTypes particleType, Color color, long lifeSpan, int width) {

        Assert.assertNotNull(location);
        Assert.assertNotNull(particleType);
        Assert.assertNotNull(color);
        Assert.assertTrue("The width of a particle cannot be less than 1.", width > 0);

        this.location = location;
        this.vector2D = vector2D;
        this.particleType = particleType;
        this.color = color;
        this.width = width;

        this.lifeSpan = lifeSpan;
        this.endless = (lifeSpan == -1);
    }

    public Particle(Location location, Vector2D vector2D, ParticleTypes particleType, Color color, int width) {
        this(location, vector2D, particleType, color, -1, width);
    }

    public Particle(Location location, Vector2D vector2D, ParticleTypes particleType, int width) {
        this(location, vector2D, particleType, new Color(E.getE().getRandom(false).nextInt(255), E.getE().getRandom(false).nextInt(255), E.getE().getRandom(false).nextInt(255)), -1, width);
    }

    public Particle(Location location, Vector2D vector2D, Color color, int width) {
        this(location, vector2D, ParticleTypes.RANDOM, color, -1, width);
    }

    public Particle(Location location, Vector2D vector2D, int width) {
        this(location, vector2D, ParticleTypes.RANDOM, new Color(E.getE().getRandom(false).nextInt(255), E.getE().getRandom(false).nextInt(255), E.getE().getRandom(false).nextInt(255)), -1, width);
    }

    @Override
    public void update(InputEntry input, long delta) {

        this.location.add(this.vector2D.getX() * delta, this.vector2D.getY() * delta);
        if(!(this.lifeSpan == -1)) {
            this.lifeSpan -= delta;
        }

    }

    @Override
    public boolean isActive() {
        return (this.lifeSpan > 0 || this.endless);
    }



    @Override
    public void render(Graphics2D value) {

        value.setColor(this.color);
        switch (this.particleType) {

            case CIRCLE:
                value.fillOval((int) this.location.getX(), (int) this.location.getY(), this.width, this.width);
                break;

            case SQUARE:
                value.fillRect((int) this.location.getX(), (int) this.location.getY(), this.width, this.width);
                break;

            case RANDOM:
                this.particleType = (E.getE().getRandom(false).nextBoolean() ? ParticleTypes.CIRCLE : ParticleTypes.SQUARE);
                this.render(value);
                break;

        }

    }
}
