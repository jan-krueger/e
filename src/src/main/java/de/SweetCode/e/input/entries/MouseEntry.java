package de.SweetCode.e.input.entries;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;

public class MouseEntry {

    private final Point locationOnScreen;
    private final Point point;

    private final int button;
    private final int clickCount;

    private final boolean isPopupTrigger;
    private final boolean isAltDown;
    private final boolean isAltGraphDown;
    private final boolean isControlDown;
    private final boolean isMetaDown;
    private final boolean isShiftDown;

    public MouseEntry(Point locationOnScreen, Point point, int button, int clickCount, boolean isPopupTrigger, boolean isAltDown, boolean isAltGraphDown, boolean isControlDown, boolean isMetaDown, boolean isShiftDown) {
        this.locationOnScreen = locationOnScreen;
        this.point = point;
        this.button = button;
        this.clickCount = clickCount;
        this.isPopupTrigger = isPopupTrigger;
        this.isAltDown = isAltDown;
        this.isAltGraphDown = isAltGraphDown;
        this.isControlDown = isControlDown;
        this.isMetaDown = isMetaDown;
        this.isShiftDown = isShiftDown;
    }

    public Point getLocationOnScreen() {
        return this.locationOnScreen;
    }

    public Point getPoint() {
        return this.point;
    }

    public int getButton() {
        return this.button;
    }

    public int getClickCount() {
        return this.clickCount;
    }

    public boolean isPopupTrigger() {
        return this.isPopupTrigger;
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
        return ToStringBuilder.create(this)
                .append("locationOnScreen", this.getLocationOnScreen().toString())
                .append("point", this.getPoint().toString())
                .append("button", this.getButton())
                .append("clickCount", this.getClickCount())
                .append("isPopupTrigger", this.isPopupTrigger())
                .append("isAltDown", this.isAltDown())
                .append("isAltGraphDown", this.isAltGraphDown())
                .append("isPopupTrigger", this.isPopupTrigger())
                .append("isControlDown", this.isControlDown())
                .append("isMetaDown", this.isMetaDown())
                .append("isShiftDown", this.isShiftDown())
            .build();
    }

    public static class Builder {

        private Point locationOnScreen;
        private Point point;

        private int button;
        private int clickCount;

        private boolean isPopupTrigger;
        private boolean isAltDown;
        private boolean isAltGraphDown;
        private boolean isControlDown;
        private boolean isMetaDown;
        private boolean isShiftDown;

        public Builder() {}

        public static Builder create() {
            return new Builder();
        }

        public Builder locationOnScreen(Point locationOnScreen) {
            this.locationOnScreen = locationOnScreen;
            return this;
        }

        public Builder point(Point point) {
            this.point = point;
            return this;
        }

        public Builder button(int button) {
            this.button = button;
            return this;
        }

        public Builder clickCount(int clickCount) {
            this.clickCount = clickCount;
            return this;
        }

        public Builder isPopupTrigger(boolean isPopupTrigger) {
            this.isPopupTrigger = isPopupTrigger;
            return this;
        }

        public Builder isAltDown(boolean isAltDown) {
            this.isAltDown = isAltDown;
            return this;
        }

        public Builder isAltGraphDown(boolean isAltGraphDown) {
            this.isAltGraphDown = isAltGraphDown;
            return this;
        }

        public Builder isControlDown(boolean isControlDown) {
            this.isControlDown = isControlDown;
            return this;
        }

        public Builder isMetaDown(boolean isMetaDown) {
            this.isMetaDown = isMetaDown;
            return this;
        }

        public Builder isShiftDown(boolean isShiftDown) {
            this.isShiftDown = isShiftDown;
            return this;
        }

        public MouseEntry build() {
            return new MouseEntry(
                    this.locationOnScreen,
                    this.point,
                    this.button,
                    this.clickCount,
                    this.isPopupTrigger,
                    this.isAltDown,
                    this.isAltGraphDown,
                    this.isControlDown,
                    this.isMetaDown,
                    this.isShiftDown
            );
        }

    }

}
