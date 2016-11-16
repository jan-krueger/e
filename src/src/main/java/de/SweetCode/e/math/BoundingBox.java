package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

/**
 * The BoundingBox is representing a square.
 */
public class BoundingBox {

    private final Location min;
    private final Location max;

    /**
     * @param a One corner of the square.
     * @param b Another corner of the square.
     */
    public BoundingBox(Location a, Location b) {
        this(
                Math.min(a.getX(), b.getX()),
                Math.min(a.getY(), b.getY()),
                Math.max(a.getX(), b.getX()),
                Math.max(a.getY(), b.getY())
        );
    }

    /**
     * @param a One corner of the square.
     * @param b Another corner of the square.
     */
    public BoundingBox(ILocation a, ILocation b) {
        this(new Location(a), new Location(b));
    }

    /**
     * @param minX The minimum x coordinate of the box.
     * @param minY The minimum y coordinate of the box.
     * @param maxX The maximum x coordinate of the box.
     * @param maxY The maximum y coordinate of the box.
     */
    public BoundingBox(double minX, double minY, double maxX, double maxY) {
        this.min = new Location(minX, minY);
        this.max = new Location(maxX, maxY);
    }

    /**
     * Moves the box by x and y.
     * @param x The units to move on the x axis.
     * @param y The units to move on the y axis.
     */
    public void move(double x, double y) {
        this.getMax().add(x, y);
        this.getCenter().add(x, y);
        this.getMin().add(x, y);
    }

    /**
     * Returns the max location.
     * @return
     */
    public Location getMax() {
        return this.max;
    }

    /**
     * Returns the center of the bounding box.
     * @return
     */
    public Location getCenter() {
        return new Location(((this.getMin().getX() + this.getMax().getX()) / 2.0D), ((this.getMin().getY() + this.getMax().getY()) / 2.0D));
    }

    /**
     * Returns the min location.
     * @return
     */
    public Location getMin() {
        return this.min;
    }

    /**
     * Returns the height of the bounding box.
     * @return
     */
    public double getHeight() {
        return Math.abs(this.getMax().getY() - this.getMin().getY());
    }

    /**
     * Returns the with of the bounding box.
     * @return
     */
    public double getWidth() {
        return Math.abs(this.getMax().getX() - this.getMin().getX());
    }

    /**
     * Checks if a square is intersecting this bounding box.
     * @param minX The minimum x coordinate of the box.
     * @param minY The minimum y coordinate of the box.
     * @param maxX The maximum x coordinate of the box.
     * @param maxY The maximum y coordinate of the box.
     * @return
     */
    public boolean intersects(double minX, double minY, double maxX, double maxY) {
        return this.intersects(new BoundingBox(minX, minY, maxX, maxY));
    }

    /**
     * Checks if a square is intersecting this bounding box.
     * @param other The bounding box to check against.
     * @return
     */
    public boolean intersects(BoundingBox other) {
        return (
                this.getMin().getX() < other.getMax().getX() &&
                this.getMax().getX() > other.getMin().getX() &&
                this.getMin().getY() < other.getMax().getY() &&
                this.getMax().getY() > other.getMin().getY()
        );
    }

    /**
     * Checks if the bounding box contains another bounding box.
     * @param other The bounding box to check against.
     * @return
     */
    public boolean contains(BoundingBox other) {
        return (
                this.getMin().getX() <= other.getMin().getX() &&
                this.getMax().getX() <= other.getMax().getX() &&
                this.getMin().getY() <= other.getMin().getY() &&
                this.getMax().getY() <= other.getMax().getY()
        );
    }

    /**
     * Checks if the bounding box contains a location.
     * @param location The location to check against.
     * @return
     */
    public boolean contains(Location location) {
        return (
                 location.getX() >= this.min.getX() &&
                 location.getX() <= this.max.getX() &&
                 location.getY() >= this.min.getY() &&
                 location.getY() <= this.max.getY()
                );
    }

    /**
     * Checks if the bounding box contains a location.
     * @param location The location to check against.
     * @return
     */
    public boolean contains(ILocation location) {
        return this.contains(new Location(location));
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("min", this.min)
                .append("max", this.max)
            .build();
    }

}
