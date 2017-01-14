package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;

public class Location implements Cloneable {

    private double x;
    private double y;

    /**
     * @param point The point to transfer.
     */
    public Location(Point point) {
        this(point.getX(), point.getY());
    }

    /**
     * Copies the coordinates of the provided location.
     * @param location The location to copy.
     */
    public Location(ILocation location) {
        this(location.getX(), location.getY());
    }

    /**
    * @param x The x coordinate.
    * @param y The y coordinate.
    */
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return The x coordinate of the location.
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return The y coordinate of the location.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Sets the coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds the values to the existing coordinates.
     * @param x The x coordinate to add.
     * @param y The y coordinate to add.
     */
    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Substract the values from the existing coordinates.
     * @param x The x coordinate to substract.
     * @param y The y coordinate to substract.
     */
    public void substract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    /**
     * @param other The other location.
     * @return Returns the distance to another location.
     */
    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2) + Math.pow(other.getY() - this.getY(), 2));
    }

    /**
     * @param other The other location.
     * @return Returns the distance to another location.
     */
    public double distanceTo(ILocation other) {
        return this.distanceTo(new Location(other));
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this).append("x", this.x).append("y", this.y).build();
    }

}
