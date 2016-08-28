package de.SweetCode.e.input;

import de.SweetCode.e.input.entries.KeyEntry;
import de.SweetCode.e.input.entries.MouseEntry;
import de.SweetCode.e.input.entries.MouseWheelEntry;
import de.SweetCode.e.utils.StringUtils;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.LinkedList;

public class InputEntry {

    private final LinkedList<KeyEntry> keyEntries;
    private final LinkedList<MouseEntry> mouseEntries;
    private final LinkedList<MouseEntry> mouseDraggedEntries;
    private final LinkedList<MouseWheelEntry> mouseWheelEntries;

    public InputEntry(LinkedList<KeyEntry> keyEntries, LinkedList<MouseEntry> mouseEntries, LinkedList<MouseWheelEntry> mouseWheelEntries, LinkedList<MouseEntry> mouseDraggedEntries) {
        this.keyEntries = keyEntries;
        this.mouseEntries = mouseEntries;
        this.mouseWheelEntries = mouseWheelEntries;
        this.mouseDraggedEntries = mouseDraggedEntries;
    }

    public LinkedList<KeyEntry> getKeyEntries() {
        return this.keyEntries;
    }

    public LinkedList<MouseEntry> getMouseEntries() {
        return this.mouseEntries;
    }

    public LinkedList<MouseWheelEntry> getMouseWheelEntries() {
        return this.mouseWheelEntries;
    }

    public LinkedList<MouseEntry> getMouseDraggedEntries() {
        return this.mouseDraggedEntries;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("keyEntries", () -> {
                    return StringUtils.join(this.keyEntries, ", ");
                })
                .append("mouseEntries", () -> {
                    return StringUtils.join(this.mouseEntries, ", ");
                })
                .append("mouseWheelEntries", () -> {
                    return StringUtils.join(this.mouseWheelEntries, ", ");
                })
                .append("mouseDraggedEntries", () -> {
                    return StringUtils.join(this.mouseDraggedEntries, ", ");
                })
            .build();
    }

}
