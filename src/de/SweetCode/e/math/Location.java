package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class Location {

    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void substract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2) + Math.pow(other.getY() - this.getY(), 2));
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this).append("x", this.x).append("y", this.y).build();
    }

}
