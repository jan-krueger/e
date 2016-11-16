package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;

public class ILocation {

    private int x;
    private int y;

    /**
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public ILocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Casts the point coordinate to a int.
     * @param point The point to transfer.
     */
    public ILocation(Point point) {
        this((int) point.getX(), (int) point.getY());
    }

    /**
     * Copies the coordinates of the provided location.
     * @param location The location to copy.
     */
    public ILocation(ILocation location) {
        this(location.getX(), location.getY());
    }

    /**
     * The x coordinate of the location.
     * @return
     */
    public int getX() {
        return this.x;
    }

    /**
     * The y coordinate of the location.
     * @return
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets the coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds the values to the existing coordinates.
     * @param x The x coordinate to add.
     * @param y The y coordinate to add.
     */
    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Substract the values from the existing coordinates.
     * @param x The x coordinate to substract.
     * @param y The y coordinate to substract.
     */
    public void substract(int x, int y) {
        this.x -= x;
        this.y -= y;
    }

    /**
     * Returns the distance to another location.
     * @param other The other location.
     * @return
     */
    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2) + Math.pow(other.getY() - this.getY(), 2));
    }

    /**
     * Returns the distance to another location.
     * @param other The other location.
     * @return
     */
    public double distanceTo(ILocation other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2) + Math.pow(other.getY() - this.getY(), 2));
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("x", this.x)
                .append("y", this.y)
            .build();
    }

}
