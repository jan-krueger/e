package de.SweetCode.e.math;

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

    @Override
    public int hashCode() {
        int sum = this.width + this.height;
        return sum * (sum + 1)/2 + this.width;
    }

}
