package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class Vector2D {

    private double x;
    private double y;
    private double magnitude;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.magnitude = Math.sqrt(
            Math.pow(this.x, 2) +
            Math.pow(this.y, 2)
        );
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public void normalize() {

        this.x /= this.magnitude;
        this.y /= this.magnitude;
        this.magnitude = 1D;

    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("x", this.getX())
                .append("y", this.getY())
                .append("magnitude", this.getMagnitude())
            .build();
    }

}
