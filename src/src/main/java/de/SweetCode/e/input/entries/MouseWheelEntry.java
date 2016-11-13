package de.SweetCode.e.input.entries;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;


public class MouseWheelEntry {

    private final Point point;

    private final double preciseWheelRotation;
    private final int scrollAmount;
    private final int unitsToScroll;
    private final int wheelRotation;

    private final boolean isShiftDown;
    private final boolean isMetaDown;
    private final boolean isAltGraphDown;
    private final boolean isAltDown;
    private final boolean isControlDown;

    public MouseWheelEntry(Point point, double preciseWheelRotation, int scrollAmount, int unitsToScroll, int wheelRotation, boolean isShiftDown, boolean isMetaDown, boolean isAltGraphDown, boolean isAltDown, boolean isControlDown) {
        this.point = point;
        this.preciseWheelRotation = preciseWheelRotation;
        this.scrollAmount = scrollAmount;
        this.unitsToScroll = unitsToScroll;
        this.wheelRotation = wheelRotation;
        this.isShiftDown = isShiftDown;
        this.isMetaDown = isMetaDown;
        this.isAltGraphDown = isAltGraphDown;
        this.isAltDown = isAltDown;
        this.isControlDown = isControlDown;
    }

    public Point getPoint() {
        return this.point;
    }

    public double getPreciseWheelRotation() {
        return this.preciseWheelRotation;
    }

    public int getScrollAmount() {
        return this.scrollAmount;
    }

    public int getUnitsToScroll() {
        return this.unitsToScroll;
    }

    public int getWheelRotation() {
        return this.wheelRotation;
    }

    public boolean isAltDown() {
        return this.isAltDown;
    }

    public boolean isAltGraphDown() {
        return this.isAltGraphDown;
    }

    public boolean isControlDown() {
        return this.isControlDown;
    }

    public boolean isMetaDown() {
        return this.isMetaDown;
    }

    public boolean isShiftDown() {
        return this.isShiftDown;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this).build();
    }

    public static class Builder {

        private Point point;
        private double preciseWheelRotation;
        private int scrollAmount;
        private int unitsToScroll;
        private int wheelRotation;

        private boolean isShiftDown;
        private boolean isMetaDown;
        private boolean isAltGraphDown;
        private boolean isAltDown;
        private boolean isControlDown;

        public Builder() {}

        public static MouseWheelEntry.Builder create() {
            return new MouseWheelEntry.Builder();
        }

        public MouseWheelEntry.Builder point(Point point) {
            this.point = point;
            return this;
        }

        public MouseWheelEntry.Builder preciseWheelRotation(double preciseWheelRotation) {
            this.preciseWheelRotation = preciseWheelRotation;
            return this;
        }

        public MouseWheelEntry.Builder scrollAmount(int scrollAmount) {
            this.scrollAmount = scrollAmount;
            return this;
        }
        public MouseWheelEntry.Builder unitsToScroll(int unitsToScroll) {
            this.unitsToScroll = unitsToScroll;
            return this;
        }

        public MouseWheelEntry.Builder wheelRotation(int wheelRotation) {
            this.wheelRotation = wheelRotation;
            return this;
        }

        public MouseWheelEntry.Builder isAltDown(boolean isAltDown) {
            this.isAltDown = isAltDown;
            return this;
        }

        public MouseWheelEntry.Builder isAltGraphDown(boolean isAltGraphDown) {
            this.isAltGraphDown = isAltGraphDown;
            return this;
        }

        public MouseWheelEntry.Builder isControlDown(boolean isControlDown) {
            this.isControlDown = isControlDown;
            return this;
        }

        public MouseWheelEntry.Builder isMetaDown(boolean isMetaDown) {
            this.isMetaDown = isMetaDown;
            return this;
        }

        public MouseWheelEntry.Builder isShiftDown(boolean isShiftDown) {
            this.isShiftDown = isShiftDown;
            return this;
        }

        public MouseWheelEntry build() {
            return new MouseWheelEntry(
                    this.point,
                    this.preciseWheelRotation,
                    this.scrollAmount,
                    this.unitsToScroll,
                    this.wheelRotation,
                    this.isShiftDown,
                    this.isMetaDown,
                    this.isAltGraphDown,
                    this.isAltDown,
                    this.isControlDown
            );
        }

    }

}
