package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class IBoundingBox {

    private final ILocation min;
    private final ILocation max;

    /**
     * Casting double locations to int.
     * @param a One corner of the bounding box.
     * @param b Another corner of the bounding box.
     */
    public IBoundingBox(Location a, Location b) {
        this(
                Math.min((int) a.getX(), (int) b.getX()),
                Math.min((int) a.getY(), (int) b.getY()),
                Math.max((int) a.getX(), (int) b.getX()),
                Math.max((int) a.getY(), (int) b.getY())
        );
    }

    /**
     * @param a One corner of the square.
     * @param b Another corner of the square.
     */
    public IBoundingBox(ILocation a, ILocation b) {
        this(new Location(a), new Location(b));
    }

    /**
     * @param minX The minimum x coordinate of the box.
     * @param minY The minimum y coordinate of the box.
     * @param maxX The maximum x coordinate of the box.
     * @param maxY The maximum y coordinate of the box.
     */
    public IBoundingBox(int minX, int minY, int maxX, int maxY) {
        this.min = new ILocation(minX, minY);
        this.max = new ILocation(maxX, maxY);
    }

    /**
     * Moves the box by x and y.
     * @param x The units to move on the x axis.
     * @param y The units to move on the y axis.
     */
    public void move(int x, int y) {
        this.getMax().add(x, y);
        this.getCenter().add(x, y);
        this.getMin().add(x, y);
    }

    /**
     * @return Gives the max {@link Location}.
     */
    public ILocation getMax() {
        return this.max;
    }

    /**
     * @return Gives the center of the bounding box.
     */
    public Location getCenter() {
        return new Location(((this.getMin().getX() + this.getMax().getX()) / 2.0D), ((this.getMin().getY() + this.getMax().getY()) / 2.0D));
    }

    /**
     * @return Returns the min {@link Location}.
     */
    public ILocation getMin() {
        return this.min;
    }

    /**
     * @return Returns the height of the bounding box.
     */
    public int getHeight() {
        return Math.abs(this.getMax().getY() - this.getMin().getY());
    }

    /**
     * @return Returns the with of the bounding box.
     */
    public int getWidth() {
        return Math.abs(this.getMax().getX() - this.getMin().getX());
    }

    /**
     * Checks if the bounding box contains another bounding box.
     * @param other The bounding box to check against.
     * @return true, if they intersect, otherwise false.
     */
    public boolean contains(BoundingBox other) {
        return (
                this.getMin().getX() <= other.getMin().getX() &&
                this.getMax().getX() >= other.getMax().getX() &&
                this.getMin().getY() <= other.getMin().getY() &&
                this.getMax().getY() >= other.getMax().getY()
        );
    }

    /**
     * Checks if the bounding box contains a location.
     * @param location The location to check against.
     * @return true, if this object contains the other location, otherwise false.
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
     * @return true, if this object contains the other location, otherwise false.
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
