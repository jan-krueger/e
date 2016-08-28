package de.SweetCode.e.rendering.particle.areas;

import de.SweetCode.e.E;
import de.SweetCode.e.Renderable;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.math.Vector2D;
import de.SweetCode.e.rendering.layers.Layer;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.rendering.particle.Particle;
import de.SweetCode.e.rendering.particle.ParticleTypes;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class HelixArea implements Renderable {

    private final Layer layer;
    private final Location location;
    private final List<Particle> particles = new LinkedList<>();

    private final double height;
    private final double width;
    private final double yStep;
    private final double degreeStep;

    /**
     * The constructor to create a new helix area.
     *
     * @param layer The layer to setScene the helix on.
     * @param location The start point of the helix.
     * @param height The height of the helix.
     * @param width The width of the helix.
     * @param yStep The step in the y direction each particle.
     * @param degreeStep The angle step in radiant each step made.
     */
    public HelixArea(Layer layer, Location location, int height, double width, double yStep, double degreeStep) {

        Assert.assertNotNull("The location cannot be null.", location);
        Assert.assertTrue("The height cannot be less than 1.", height > 0);
        Assert.assertTrue("The width cannot be less than or be equals to 0.", width > 0);
        Assert.assertTrue("The yStep cannot be less than or be equals to 0.", yStep > 0);
        Assert.assertTrue("The degreeStep must be greater than 0 and less than or equals to 2.", (degreeStep >= 0 && degreeStep <= 2));

        this.layer = layer;
        this.location = location;
        this.height = height;
        this.width = width;
        this.yStep = yStep;
        this.degreeStep = degreeStep;

        this.setup();
    }

    private void setup() {
        double degree = 0;

        for(double rY = this.location.getY(); rY < this.height + this.location.getY(); rY += this.yStep) {

            double x = this.location.getX() + Math.cos(degree) * this.width;

            if(degree > this.height * Math.PI) {
                degree = 0;
            } else {
                degree += this.degreeStep;
            }

            Particle particle = Particle.Builder.create()
                    .layer(this.layer)
                    .location(new Location(x, rY))
                    .vector2D(new Vector2D(0, 0))
                    .mixType(false)
                    .width(10)
                    .lifeSpan(-1)
                    .color(Color.RED)
                    .destroyItself(false)
                    .fadeInAndOut(false)
                    .particleType(ParticleTypes.RANDOM)
                    .build();

            this.particles.add(particle);
            E.getE().addComponent(particle);
        }
    }

    @Override
    public void render(Layers value) {

    }

    @Override
    public void update(InputEntry input, long delta) {


    }

    @Override
    public boolean isActive() {
        return true;
    }



}
