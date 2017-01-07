package de.SweetCode.e.input;

import de.SweetCode.e.input.entries.KeyEntry;
import de.SweetCode.e.input.entries.MouseEntry;
import de.SweetCode.e.input.entries.MouseWheelEntry;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.LinkedList;

public class InputEntry {

    private final LinkedList<KeyEntry> keyEntries;
    private final LinkedList<MouseEntry> mouseEntries;
    private final LinkedList<MouseEntry> mouseReleasedQueue;
    private final LinkedList<MouseEntry> mouseDraggedEntries;
    private final LinkedList<MouseEntry> mouseMovedEntries;
    private final LinkedList<MouseWheelEntry> mouseWheelEntries;

    /**
     * An input entry.
     * @param keyEntries All key entries from the keyboard.
     * @param mouseEntries All mouse entries from normal clicks.
     * @param mouseWheelEntries All mouse entries from the mouse wheel.
     * @param mouseDraggedEntries All mouse entries from mouse dragging.
     * @param mouseMovedEntries All mouse entries from mouse movement.
     * @param mouseReleasedQueue All mouse entries from mouse releases.
     */
    public InputEntry(LinkedList<KeyEntry> keyEntries, LinkedList<MouseEntry> mouseEntries, LinkedList<MouseWheelEntry> mouseWheelEntries, LinkedList<MouseEntry> mouseDraggedEntries, LinkedList<MouseEntry> mouseMovedEntries, LinkedList<MouseEntry> mouseReleasedQueue) {
        this.keyEntries = keyEntries;
        this.mouseEntries = mouseEntries;
        this.mouseReleasedQueue = mouseReleasedQueue;
        this.mouseWheelEntries = mouseWheelEntries;
        this.mouseDraggedEntries = mouseDraggedEntries;
        this.mouseMovedEntries = mouseMovedEntries;
    }

    /**
     * @return Gives ll key entries from the keyboard.
     */
    public LinkedList<KeyEntry> getKeyEntries() {
        return this.keyEntries;
    }

    /**
     * @return Gives all mouse entries from normal clicks.
     */
    public LinkedList<MouseEntry> getMouseEntries() {
        return this.mouseEntries;
    }

    /**
     * @return Gives all mouse entries from mouse releases.
     */
    public LinkedList<MouseEntry> getMouseReleasedQueue() {
        return this.mouseReleasedQueue;
    }

    /**
     * @return Gives all mouse entries from the mouse wheel.
     */
    public LinkedList<MouseWheelEntry> getMouseWheelEntries() {
        return this.mouseWheelEntries;
    }

    /**
     * @return Gives all mouse entries from mouse dragging.
     */
    public LinkedList<MouseEntry> getMouseDraggedEntries() {
        return this.mouseDraggedEntries;
    }

    /**
     * @return Gives all mouse entries from mouse movement.
     */
    public LinkedList<MouseEntry> getMouseMovedEntries() {
        return this.mouseMovedEntries;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
            .append("keyEntries", this.keyEntries)
            .append("mouseEntries", this.mouseEntries)
            .append("mouseWheelEntries", this.mouseWheelEntries)
            .append("mouseDraggedEntries", this.mouseDraggedEntries)
            .append("mouseMovedEntries", this.mouseMovedEntries)
            .append("mouseReleasedQueue", this.mouseReleasedQueue)
        .build();
    }

}
