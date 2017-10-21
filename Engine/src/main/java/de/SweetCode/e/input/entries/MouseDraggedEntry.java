package de.SweetCode.e.input.entries;

import java.awt.*;

public class MouseDraggedEntry extends MouseEntry {

    public MouseDraggedEntry(Point locationOnScreen, Point point, int button, int clickCount, boolean isPopupTrigger, boolean isAltDown, boolean isAltGraphDown, boolean isControlDown, boolean isMetaDown, boolean isShiftDown) {
        super(locationOnScreen, point, button, clickCount, isPopupTrigger, isAltDown, isAltGraphDown, isControlDown, isMetaDown, isShiftDown);
    }

}
