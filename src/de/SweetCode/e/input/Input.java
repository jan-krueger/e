package de.SweetCode.e.input;

import de.SweetCode.e.E;
import de.SweetCode.e.input.entries.KeyEntry;
import de.SweetCode.e.input.entries.MouseEntry;
import de.SweetCode.e.input.entries.MouseWheelEntry;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class Input extends KeyAdapter {

    private final Queue<KeyEntry> keyQueue = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseQueue = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseReleasedQueue = new LinkedTransferQueue<>();
    private final Queue<MouseWheelEntry> mouseScrollQueue = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseDraggedEntries = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseMovedEntries = new LinkedTransferQueue<>();

    public Input() {
        this.register();
    }

    /**
     * Adds the listener.
     */
    private void register() {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {

            if(e.getID() == KeyEvent.KEY_PRESSED) {
                this.keyQueue.add(
                    KeyEntry.Builder.create()
                        .keyCode(e.getKeyCode())
                        .extendedKeyCode(e.getExtendedKeyCode())
                        .character(e.getKeyChar())
                        .keyLocation(e.getKeyLocation())
                        .isActionKey(e.isActionKey())
                        .isAltDown(e.isAltDown())
                        .isAltGraphDown(e.isAltGraphDown())
                        .isMetaDown(e.isMetaDown())
                        .isShiftDown(e.isShiftDown())
                    .build()
                );
            }

            return false;

        });

        E.getE().getScreen().addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Input.this.mouseQueue.add(
                    MouseEntry.Builder.create()
                        .button(e.getButton())
                        .clickCount(e.getClickCount())
                        .isPopupTrigger(e.isPopupTrigger())
                        .locationOnScreen(e.getLocationOnScreen())
                        .point(e.getPoint())
                        .isAltDown(e.isAltDown())
                        .isAltGraphDown(e.isAltGraphDown())
                        .isControlDown(e.isControlDown())
                        .isMetaDown(e.isMetaDown())
                        .isShiftDown(e.isShiftDown())
                    .build()
                );

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Input.this.mouseReleasedQueue.add(
                    MouseEntry.Builder.create()
                        .button(e.getButton())
                        .clickCount(e.getClickCount())
                        .isPopupTrigger(e.isPopupTrigger())
                        .locationOnScreen(e.getLocationOnScreen())
                        .point(e.getPoint())
                        .isAltDown(e.isAltDown())
                        .isAltGraphDown(e.isAltGraphDown())
                        .isControlDown(e.isControlDown())
                        .isMetaDown(e.isMetaDown())
                        .isShiftDown(e.isShiftDown())
                    .build()
                );
            }
        });

        E.getE().getScreen().addMouseWheelListener(e -> Input.this.mouseScrollQueue.add(
            MouseWheelEntry.Builder.create()
                .point(e.getPoint())
                .preciseWheelRotation(e.getPreciseWheelRotation())
                .scrollAmount(e.getScrollAmount())
                .unitsToScroll(e.getUnitsToScroll())
                .wheelRotation(e.getWheelRotation())
                .isAltDown(e.isAltDown())
                .isAltGraphDown(e.isAltGraphDown())
                .isControlDown(e.isControlDown())
                .isMetaDown(e.isMetaDown())
                .isShiftDown(e.isShiftDown())
            .build()
        ));

        E.getE().getScreen().addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                Input.this.mouseDraggedEntries.add(
                    MouseEntry.Builder.create()
                        .button(e.getButton())
                        .clickCount(e.getClickCount())
                        .isPopupTrigger(e.isPopupTrigger())
                        .locationOnScreen(e.getLocationOnScreen())
                        .point(e.getPoint())
                        .isAltDown(e.isAltDown())
                        .isAltGraphDown(e.isAltGraphDown())
                        .isControlDown(e.isControlDown())
                        .isMetaDown(e.isMetaDown())
                        .isShiftDown(e.isShiftDown())
                    .build()
                );

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Input.this.mouseMovedEntries.add(
                    MouseEntry.Builder.create()
                        .button(e.getButton())
                        .clickCount(e.getClickCount())
                        .isPopupTrigger(e.isPopupTrigger())
                        .locationOnScreen(e.getLocationOnScreen())
                        .point(e.getPoint())
                        .isAltDown(e.isAltDown())
                        .isAltGraphDown(e.isAltGraphDown())
                        .isControlDown(e.isControlDown())
                        .isMetaDown(e.isMetaDown())
                        .isShiftDown(e.isShiftDown())
                    .build()
                );
            }
        });

    }

    /**
     * Returns all pressed keys since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest key.
     * @return
     */
    public LinkedList<KeyEntry> getKeyboardEntries() {
        LinkedList<KeyEntry> stream = new LinkedList<>(this.keyQueue);
        this.keyQueue.clear();
        return stream;
    }

    /**
     * Returns all pressed mouse buttons since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest mouse button.
     * @return
     */
    public LinkedList<MouseEntry> getMouseEntries() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseQueue);
        this.mouseQueue.clear();
        return stream;
    }

    /**
     * Returns all mouse scrolls since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest mouse button.
     * @return
     */
    public LinkedList<MouseWheelEntry> getMouseWheelEntries() {
        LinkedList<MouseWheelEntry> stream = new LinkedList<>(this.mouseScrollQueue);
        this.mouseScrollQueue.clear();
        return stream;
    }

    /**
     * Returns all mouse scrolls since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest mouse button.
     * @return
     */
    public LinkedList<MouseEntry> getMouseDraggedEntries() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseDraggedEntries);
        this.mouseDraggedEntries.clear();
        return stream;
    }

    /**
     * Returns all mouse scrolls since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest mouse button.
     * @return
     */
    public LinkedList<MouseEntry> getMouseMovedEntries() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseMovedEntries);
        this.mouseMovedEntries.clear();
        return stream;
    }

    /**
     * Returns all mouse scrolls since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest mouse button.
     * @return
     */
    public LinkedList<MouseEntry> getMouseReleasedQueue() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseReleasedQueue);
        this.mouseReleasedQueue.clear();
        return stream;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
            .append("keyQueue", this.keyQueue)
            .append("mouseQueue", this.mouseQueue)
            .append("mouseScrollQueue", this.mouseScrollQueue)
            .append("mouseDraggedEntries", this.mouseDraggedEntries)
            .append("mouseMovedEntries", this.mouseMovedEntries)
            .append("mouseReleasedQueue", this.mouseReleasedQueue)
        .build();
    }

}
