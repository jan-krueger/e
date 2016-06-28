package de.SweetCode.e.math;

import de.SweetCode.e.rendering.QuadTreeObject;
import de.SweetCode.e.utils.ToStringBuilder;

public class BoundingBox implements QuadTreeObject {

    private final Location min;
    private final Location max;

    public BoundingBox(Location a, Location b) {
        this(
                Math.min(a.getX(), b.getX()),
                Math.min(a.getY(), b.getY()),
                Math.max(a.getX(), b.getX()),
                Math.max(a.getY(), b.getY())
        );
    }

    public BoundingBox(double minX, double minY, double maxX, double maxY) {
        this.min = new Location(minX, minY);
        this.max = new Location(maxX, maxY);
    }

    public void move(double x, double y) {
        this.getMax().add(x, y);
        this.getCenter().add(x, y);
        this.getMin().add(x, y);
    }

    public Location getMax() {
        return this.max;
    }

    public Location getCenter() {
        return new Location(((this.getMin().getX() + this.getMax().getX()) / 2.0D), ((this.getMin().getY() + this.getMax().getY()) / 2.0D));
    }

    public Location getMin() {
        return this.min;
    }

    public double getHeight() {
        return Math.abs(this.getMax().getY() - this.getMin().getY());
    }

    public double getWidth() {
        return Math.abs(this.getMax().getX() - this.getMin().getX());
    }

    public boolean intersects(double minX, double minY, double maxX, double maxY) {
        return this.intersects(new BoundingBox(minX, minY, maxX, maxY));
    }

    public boolean intersects(BoundingBox other) {
        return (
                this.getMin().getX() < other.getMax().getX() &&
                this.getMax().getX() > other.getMin().getX() &&
                this.getMin().getY() < other.getMax().getY() &&
                this.getMax().getY() > other.getMin().getY()
        );
    }

    public boolean contains(BoundingBox other) {
        return (
                this.getMin().getX() <= other.getMin().getX() &&
                this.getMax().getX() <= other.getMax().getX() &&
                this.getMin().getY() <= other.getMin().getY() &&
                this.getMax().getY() <= other.getMax().getY()
        );
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this).append(this).build();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this;
    }
}
