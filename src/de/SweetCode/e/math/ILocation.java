package de.SweetCode.e.math;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;

/**
 * Created by 204g02 on 29.08.2016.
 */
public class ILocation {

    private int x;
    private int y;

    public ILocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ILocation(Point point) {
        this((int) point.getX(), (int) point.getY());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void substract(int x, int y) {
        this.x -= x;
        this.y -= y;
    }

    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2) + Math.pow(other.getY() - this.getY(), 2));
    }

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
