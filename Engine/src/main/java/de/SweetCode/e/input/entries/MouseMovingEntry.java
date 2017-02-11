package de.SweetCode.e.input.entries;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;

public class MouseMovingEntry {

    private final Point locationOnScreen;
    private final Point point;

    public MouseMovingEntry(Point locationOnScreen, Point point) {
        this.locationOnScreen = locationOnScreen;
        this.point = point;
    }

    public Point getLocationOnScreen() {
        return this.locationOnScreen;
    }

    public Point getPoint() {
        return this.point;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("locationOnScreen", this.getLocationOnScreen().toString())
                .append("point", this.getPoint().toString())
            .build();
    }

    public static class Builder {

        private Point locationOnScreen;
        private Point point;

        public Builder() {}

        public static MouseMovingEntry.Builder create() {
            return new MouseMovingEntry.Builder();
        }

        public MouseMovingEntry.Builder locationOnScreen(Point locationOnScreen) {
            this.locationOnScreen = locationOnScreen;
            return this;
        }

        public MouseMovingEntry.Builder point(Point point) {
            this.point = point;
            return this;
        }

        public MouseMovingEntry build() {
            return new MouseMovingEntry(
                this.locationOnScreen,
                this.point
            );
        }

    }

}
