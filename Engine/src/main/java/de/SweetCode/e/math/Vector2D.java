package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class Vector2D {

    private double x;
    private double y;
    private double magnitude;

    /**
     * @param x The x direction of the vector.
     * @param y The y direction of the vector.
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.magnitude = Math.sqrt(
            Math.pow(this.x, 2) +
            Math.pow(this.y, 2)
        );
    }

    /**
     * @return Gives the x direction of the vector.
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return Gvies the y direction of the vector.
     */
    public double getY() {
        return this.y;
    }

    /**
     * @return Gives the magnitude of the vector.
     */
    public double getMagnitude() {
        return this.magnitude;
    }

    /**
     * Normalizes this vector.
     */
    public void normalize() {

        this.x /= this.magnitude;
        this.y /= this.magnitude;
        this.magnitude = 1D;

    }

    /**
     * Subtract a vector from this vector and returns the result in a new vector.
     * @param vector The vector to substract.
     * @return A new {@link Vector2D} with the result.
     */
    public Vector2D sub(Vector2D vector) {
        return new Vector2D(this.getX() - vector.getX(), this.getY() - this.getY());
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
