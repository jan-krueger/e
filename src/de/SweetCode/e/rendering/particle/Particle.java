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
    private final boolean mixType;
    private final boolean destroyItself;

    // Fade in and out
    private final boolean fadeInAndOut;
    private double fadeTurnPoint;
    private int fadeTime;
    private double fadeAlphaStep;
    private float fadeAlpha = 0;

    private ParticleTypes particleType;
    private boolean endless;
    private long lifeSpan;
    private boolean destroy = false;

    /**
     * The constructor to create a new particle.
     * @param location The location where the particle should appear.
     * @param vector2D The direction of the particle.
     * @param particleType The type of the particle.
     * @param color The color of the particle.
     * @param mixType If it is set to true than it iterates through all different particle types.
     * @param destroyItself If this is set to true than the particle will destroy itself as soon as Particle#isActive() returns false.
     * @param fadeInAndOut If it is set to true the particle fades in and out.
     * @param lifeSpan The amount of time the particle should exist.
     * @param width The width of the particle.
     */
    public Particle(Location location, Vector2D vector2D, ParticleTypes particleType, Color color, boolean fadeInAndOut, boolean destroyItself, boolean mixType, long lifeSpan, int width) {

        Assert.assertNotNull(location);
        Assert.assertNotNull(vector2D);
        Assert.assertNotNull(particleType);
        Assert.assertNotNull(color);
        Assert.assertTrue("The lifeSpan of an particle cannot be less than 1 if the fadeInAndOut mode is in use.", (fadeInAndOut ? lifeSpan > 0 : true));
        Assert.assertTrue("The width of a particle cannot be less than 1.", width > 0);

        this.location = location;
        this.vector2D = vector2D;
        this.particleType = particleType;
        this.color = color;
        this.width = width;
        this.mixType = mixType;
        this.fadeInAndOut = fadeInAndOut;
        this.fadeTurnPoint = Math.abs(lifeSpan / 2F);
        this.fadeAlphaStep = (1F / this.fadeTurnPoint);
        this.destroyItself = destroyItself;

        this.fadeAlpha = (this.fadeInAndOut ? 0 : 1);

        this.lifeSpan = lifeSpan;
        this.endless = (lifeSpan == -1);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
        this.lifeSpan = -1;
        this.endless = false;
    }

    @Override
    public void update(InputEntry input, long delta) {

        this.location.add(this.vector2D.getX() * delta, this.vector2D.getY() * delta);
        if(!(this.lifeSpan == -1) && !(this.endless)) {
            this.lifeSpan -= delta;
        }

        if(this.fadeTime >= this.fadeTurnPoint && this.fadeInAndOut) {
            this.fadeAlpha -= this.fadeAlphaStep;
            this.fadeAlpha = Math.max(this.fadeAlpha, 0);
        } else if(this.fadeInAndOut) {
            this.fadeAlpha += this.fadeAlphaStep;
            this.fadeAlpha = Math.min(1, this.fadeAlpha);
        }

        if(this.fadeInAndOut) {
            this.fadeTime += delta;
        }

        if(!(this.isActive()) && this.destroyItself) {
            E.getE().getGameComponents().remove(this);
        }

    }

    @Override
    public boolean isActive() {

        if((this.lifeSpan > 0 || this.endless) && !this.destroy) {
            return true;
        }

        if(this.fadeInAndOut && this.fadeTime <= this.fadeTurnPoint * 2) {
            return true;
        }

        return false;
    }


    @Override
    public void render(Graphics2D value) {

        if(this.fadeInAndOut) {
            value.setComposite(AlphaComposite.SrcOver.derive(this.fadeAlpha));
        }

        value.setColor(this.color);
        switch (this.particleType) {

            case CIRCLE:
                value.fillOval((int) this.location.getX(), (int) this.location.getY(), this.width, this.width);

                if(this.mixType) {
                    this.particleType = ParticleTypes.RANDOM;
                }
                break;

            case SQUARE:
                value.fillRect((int) this.location.getX(), (int) this.location.getY(), this.width, this.width);

                if(this.mixType) {
                    this.particleType = ParticleTypes.RANDOM;
                }
                break;

            case RANDOM:
                this.particleType = (E.getE().getRandom(false).nextBoolean() ? ParticleTypes.CIRCLE : ParticleTypes.SQUARE);
                this.render(value);
                break;

        }

        if(this.fadeInAndOut) {
            value.setComposite(AlphaComposite.SrcOver.derive(1));
        }

    }

}
