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

/**
 * <p>
 *    The Input is basically just a {@link KeyAdapter} waiting for all user input to put it into {@link LinkedTransferQueue}
 *    to make it easier accessible for the user.
 * </p>
 */
public final class Input extends KeyAdapter {

    private final Queue<KeyEntry> keyQueue = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseQueue = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseReleasedQueue = new LinkedTransferQueue<>();
    private final Queue<MouseWheelEntry> mouseScrollQueue = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseDraggedEntries = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseMovedEntries = new LinkedTransferQueue<>();

    /**
     * <p>
     *    Creates a new instance of Input and calls the {@link Input#register()} method to set up all listeners.
     * </p>
     */
    public Input() {
        this.register();
    }

    /**
     * <p>
     *    Sets up all listeners.
     * </p>
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
     * <pre>
     *    This method returns a {@link LinkedList} of all keyboard entries since its last call. It copies all entries
     *    from the queue which the listener is feeding into the list and clears it.
     *    <b>Event:</b> {@link KeyEvent} and {@link KeyEvent#getID()} equals to <i>{@link KeyEvent#KEY_PRESSED}</i>
     * </pre>
     *
     * @return A list of all registered keyboard entries.
     */
    public LinkedList<KeyEntry> getKeyboardEntries() {
        LinkedList<KeyEntry> stream = new LinkedList<>(this.keyQueue);
        this.keyQueue.clear();
        return stream;
    }

    /**
     * <pre>
     *    This method returns a {@link LinkedList} of all mouse entries since its last call. It copies all entries from
     *    the queue which the listener is feeding into the list and clears it.
     *    <b>Event:</b> {@link MouseAdapter#mousePressed(MouseEvent)}
     * </pre>
     *
     * @return A list of all registered mouse entries.
     */
    public LinkedList<MouseEntry> getMouseEntries() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseQueue);
        this.mouseQueue.clear();
        return stream;
    }

    /**
     * <pre>
     *    This method returns a {@link LinkedList} of all mouse wheel entries since its last call. It copies all entries
     *    from the queue which the listener is feeding into the list and clears it.
     *    <b>Event:</b> {@link MouseWheelEvent}
     * </pre>
     *
     * @return A list of all registered mouse wheel entries.
     */
    public LinkedList<MouseWheelEntry> getMouseWheelEntries() {
        LinkedList<MouseWheelEntry> stream = new LinkedList<>(this.mouseScrollQueue);
        this.mouseScrollQueue.clear();
        return stream;
    }

    /**
     * <pre>
     *    This method returns a {@link LinkedList} of all mouse dragged entries since its last call. It copies all entries
     *    from the queue which the listener is feeding into the list and clears it.
     *    <b>Event:</b> {@link MouseMotionAdapter#mouseDragged(MouseEvent)}
     * </pre>
     *
     * @return A list of all registered mouse dragged entries.
     */
    public LinkedList<MouseEntry> getMouseDraggedEntries() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseDraggedEntries);
        this.mouseDraggedEntries.clear();
        return stream;
    }

    /**
     * <pre>
     *    This method returns a {@link LinkedList} of all mouse moved entries since its last call. It copies all entries
     *    from the queue which the listener is feeding into the list and clears it.
     *    <b>Event:</b> {@link MouseMotionAdapter#mouseMoved(MouseEvent)}
     * </pre>
     *
     * @return A list of all registered mouse moved entries.
     */
    public LinkedList<MouseEntry> getMouseMovedEntries() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseMovedEntries);
        this.mouseMovedEntries.clear();
        return stream;
    }

    /**
     * <pre>
     *    This method returns a {@link LinkedList} of all mouse moved entries since its last call. It copies all entries
     *    from the queue which the listener is feeding into the list and clears it.
     *    <b>Event:</b> {@link MouseAdapter#mouseReleased(MouseEvent)}
     * </pre>
     *
     * @return A list of all registered mouse release entries.
     */
    public LinkedList<MouseEntry> getMouseReleasedQueue() {
        LinkedList<MouseEntry> stream = new LinkedList<>(this.mouseReleasedQueue);
        this.mouseReleasedQueue.clear();
        return stream;
    }

    /**
     * <p>
     *    Builds a new {@link InputEntry} by calling:
     * </p>
     * <ul>
     *     <li>{@link Input#getKeyboardEntries()}</li>
     *     <li>{@link Input#getMouseEntries()}</li>
     *     <li>{@link Input#getMouseWheelEntries()}</li>
     *     <li>{@link Input#getMouseDraggedEntries()}</li>
     *     <li>{@link Input#getMouseMovedEntries()}</li>
     *     <li>{@link Input#getMouseReleasedQueue()}</li>
     * </ul>
     * <p>
     *    and passing them to its constructor.
     * </p>
     *
     * @return Builds a new {@link InputEntry} with the current values and removes all from the queue.
     */
    public InputEntry build() {
        return new InputEntry(
                this.getKeyboardEntries(),
                this.getMouseEntries(),
                this.getMouseWheelEntries(),
                this.getMouseDraggedEntries(),
                this.getMouseMovedEntries(),
                this.getMouseReleasedQueue()
        );
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
