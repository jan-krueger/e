package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

/**
 * A circle box - :) - is just a 2D circle.
 */
public class CircleBox {

    private final Location center;
    private final double radius;

    /**
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     */
    public CircleBox(Location center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * @return Gives the center of the circle.
     */
    public Location getCenter() {
        return this.center;
    }

    /**
     * @return Gives the radius of the circle.
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Checks if another circle is intersecting.
     * @param x The center x coordinate of the other circle.
     * @param y The center y coordinate of the other circle.
     * @param radius The radius of the other circle.
     * @return true, if they intersect, otherwise false.
     */
    public boolean intersects(double x, double y, double radius) {
        return (Math.pow(radius - this.getRadius(), 2) >= (Math.pow(x - this.getCenter().getX(), 2) + Math.pow(y - this.getCenter().getY(), 2)));
    }

    /**
     * Checks if another circle is intersecting.
     * @param center The center location of the other circle.
     * @param radius The radius of the other circle.
     * @return true, if they intersect, otherwise false.
     */
    public boolean intersects(Location center, double radius) {
        return this.intersects(new CircleBox(center, radius));
    }

    /**
     * Checks if another circle box is intersecting this circle.
     * @param circleBox The other circle box.
     * @return true, if they intersect, otherwise false.
     */
    public boolean intersects(CircleBox circleBox) {
        return this.intersects(circleBox.getCenter().getX(), circleBox.getCenter().getY(), circleBox.getRadius());
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("radius", this.radius)
                .append("center", this.center.toString())
            .build();
    }

}
