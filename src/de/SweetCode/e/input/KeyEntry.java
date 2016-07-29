package de.SweetCode.e.input;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class KeyEntry {

    private final char character;

    private final int keyCode;
    private final int extendedKeyCode;
    private final int keyLocation;

    private final boolean isActionKey;
    private final boolean isAltDown;
    private final boolean isAltGraphDown;
    private final boolean isControlDown;
    private final boolean isMetaDown;
    private final boolean isShiftDown;

    public KeyEntry(int keyCode, int extendedKeyCode, char character, int keyLocation, boolean isActionKey, boolean isAltDown, boolean isAltGraphDown, boolean isControlDown, boolean isMetaDown, boolean isShiftDown) {
        this.keyCode = keyCode;
        this.extendedKeyCode = extendedKeyCode;
        this.character = character;
        this.keyLocation = keyLocation;
        this.isActionKey = isActionKey;
        this.isAltDown = isAltDown;
        this.isAltGraphDown = isAltGraphDown;
        this.isControlDown = isControlDown;
        this.isMetaDown = isMetaDown;
        this.isShiftDown = isShiftDown;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public int getExtendedKeyCode() {
        return this.extendedKeyCode;
    }

    public char getCharacter() {
        return this.character;
    }

    public int getKeyLocation() {
        return this.keyLocation;
    }

    public boolean isActionKey() {
        return this.isActionKey;
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
                .append("keyCode", this.getKeyCode())
                .append("extendedKeyCode", this.getExtendedKeyCode())
                .append("character", this.getCharacter())
                .append("keyLocation", this.getKeyLocation())
                .append("isActionKey", this.isActionKey())
                .append("isAltDown", this.isAltDown())
                .append("isAltGraphDown", this.isAltGraphDown())
                .append("isControlDown", this.isControlDown())
                .append("isMetaDown", this.isMetaDown())
                .append("isShiftDown", this.isShiftDown())
            .build();
    }

    public static class Builder {

        private char character;

        private int keyCode;
        private int extendedKeyCode;
        private int keyLocation;

        private boolean isActionKey;
        private boolean isAltDown;
        private boolean isAltGraphDown;
        private boolean isControlDown;
        private boolean isMetaDown;
        private boolean isShiftDown;

        public Builder() {}

        public static KeyEntry.Builder create() {
            return new Builder();
        }

        public Builder character(char character) {
            this.character = character;
            return this;
        }

        public Builder keyCode(int keyCode) {
            this.keyCode = keyCode;
            return this;
        }
        public Builder extendedKeyCode(int extendedKeyCode) {
            this.extendedKeyCode = extendedKeyCode;
            return this;
        }

        public Builder keyLocation(int keyLocation) {
            this.keyLocation = keyLocation;
            return this;
        }

        public Builder isActionKey(boolean isActionKey) {
            this.isActionKey = isActionKey;
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

        public KeyEntry build() {
            return new KeyEntry(
                    this.keyCode,
                    this.extendedKeyCode,
                    this.character,
                    this.keyLocation,
                    this.isActionKey,
                    this.isAltDown,
                    this.isAltGraphDown,
                    this.isControlDown,
                    this.isMetaDown,
                    this.isShiftDown
            );
        }

    }

}
