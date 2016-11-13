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

    public InputEntry(LinkedList<KeyEntry> keyEntries, LinkedList<MouseEntry> mouseEntries, LinkedList<MouseWheelEntry> mouseWheelEntries, LinkedList<MouseEntry> mouseDraggedEntries, LinkedList<MouseEntry> mouseMovedEntries, LinkedList<MouseEntry> mouseReleasedQueue) {
        this.keyEntries = keyEntries;
        this.mouseEntries = mouseEntries;
        this.mouseReleasedQueue = mouseReleasedQueue;
        this.mouseWheelEntries = mouseWheelEntries;
        this.mouseDraggedEntries = mouseDraggedEntries;
        this.mouseMovedEntries = mouseMovedEntries;
    }

    public LinkedList<KeyEntry> getKeyEntries() {
        return this.keyEntries;
    }

    public LinkedList<MouseEntry> getMouseEntries() {
        return this.mouseEntries;
    }

    public LinkedList<MouseEntry> getMouseReleasedQueue() {
        return this.mouseReleasedQueue;
    }

    public LinkedList<MouseWheelEntry> getMouseWheelEntries() {
        return this.mouseWheelEntries;
    }

    public LinkedList<MouseEntry> getMouseDraggedEntries() {
        return this.mouseDraggedEntries;
    }

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
