package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;

public class IDimension {

    private int width;
    private int height;

    public IDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Dimension toDimension() {
        return new Dimension(width, height);
    }

    /**
     * Checks if the bounding box contains a location.
     *
     * @param origin the origin of the IDimension.
     * @param location The location to check against.
     * @return true, if this object contains the other location, otherwise false.
     */
    public boolean contains(Location origin, Location location) {
        return (
                location.getX() >= origin.getX() &&
                location.getX() <= (origin.getX() + this.getWidth()) &&
                location.getY() >= origin.getY() &&
                location.getY() <= (origin.getY() + this.getHeight())
        );
    }

    /**
     * Checks if the bounding box contains a location.
     *
     * @param origin the origin of the IDimension.
     * @param location The location to check against.
     * @return true, if this object contains the other location, otherwise false.
     */
    public boolean contains(Location origin, ILocation location) {
        return this.contains(origin, new Location(location));
    }

    /**
     * Checks if the bounding box contains a location.
     *
     * @param origin the origin of the IDimension.
     * @param location The location to check against.
     * @return true, if this object contains the other location, otherwise false.
     */
    public boolean contains(ILocation origin, ILocation location) {
        return this.contains(new Location(origin), new Location(location));
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("width", this.width)
                .append("width", this.height)
            .build();
    }

    @Override
    public int hashCode() {
        int sum = this.width + this.height;
        return sum * (sum + 1)/2 + this.width;
    }

}
