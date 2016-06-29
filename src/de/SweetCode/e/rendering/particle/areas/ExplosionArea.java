package de.SweetCode.e.rendering.particle.areas;

import de.SweetCode.e.E;
import de.SweetCode.e.Renderable;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.CircleBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.math.Vector2D;
import de.SweetCode.e.rendering.particle.Particle;
import de.SweetCode.e.rendering.particle.ParticleTypes;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExplosionArea implements Renderable {

    private final List<Particle> particles = new ArrayList<>();

    private CircleBox circleBox;
    private final boolean evenDistribution;
    private final int stepDelay;
    private final int lifetime;

    private int amount;
    private int step;
    private int currentStep = 0;

    private long timePassed;

    /**
     * The constructor to create a new explosion area.
     * @param circleBox The area of the explosion.
     * @param amount The amount of particles used in the explosion.
     * @param steps The amount of steps to spawn all particles.
     * @param stepDelay The delay between each step.
     */
    public ExplosionArea(CircleBox circleBox, boolean evenDistribution, int amount, int steps, int stepDelay) {

        Assert.assertNotNull("The circle box cannot be null.", circleBox);
        Assert.assertTrue("The amount of particles cannot be lass than 2.", amount > 1);
        Assert.assertTrue("The amount of steps cannot be less than 1.", amount > 0);
        Assert.assertTrue("The step delay cannot be less than 1.", stepDelay > 0);

        this.circleBox = circleBox;
        this.evenDistribution = evenDistribution;
        this.amount = amount;
        this.step = amount / steps;
        this.stepDelay = stepDelay;
        this.lifetime = steps * stepDelay;

        this.setup();
    }

    private void setup() {

        for(int i = 0; i < this.amount; i++) {

            this.particles.add(new Particle(ExplosionArea.getRandomLocation(this.circleBox, this.evenDistribution), new Vector2D(0, 0), ParticleTypes.RANDOM, Color.ORANGE, true, true, false, this.lifetime, 10));

        }

        Collections.sort(this.particles, (o1, o2) -> {

            double o1D = o1.getLocation().distanceTo(this.circleBox.getCenter());
            double o2D = o2.getLocation().distanceTo(this.circleBox.getCenter());

            if(o1D == o2D) {
                return 0;
            }

            return (o1D > o2D ? 1 : -1);

        });

    }

    @Override
    public void render(Graphics2D value) {}

    @Override
    public void update(InputEntry input, long delta) {

        this.timePassed += delta;

        if(this.stepDelay <= this.timePassed && this.currentStep < this.particles.size()) {

            long deltaStep = (currentStep + (this.timePassed / this.stepDelay * this.step));
            for(int i = this.currentStep; i < deltaStep && this.currentStep < this.particles.size() - 1; i++) {
                E.getE().addComponent(this.particles.get(i));
            }

            this.currentStep += step;
            this.timePassed = 0;

        }

    }

    @Override
    public boolean isActive() {
        return true;
    }

    private static Location getRandomLocation(CircleBox circleBox, boolean evenDistribution) {
        double angle = Math.random() * Math.PI * 2;
        double radius = (evenDistribution ? Math.sqrt(Math.random()) : Math.random()) * circleBox.getRadius();

        return new Location(
                circleBox.getCenter().getX() + radius * Math.cos(angle),
                circleBox.getCenter().getY() + radius * Math.sin(angle)
        );
    }

}
