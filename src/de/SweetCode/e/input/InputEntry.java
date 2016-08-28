package de.SweetCode.e.input;

import de.SweetCode.e.input.entries.KeyEntry;
import de.SweetCode.e.input.entries.MouseEntry;
import de.SweetCode.e.input.entries.MouseWheelEntry;
import de.SweetCode.e.utils.StringUtils;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.stream.Stream;

public class InputEntry {

    private final Stream<KeyEntry> keyEntries;
    private final Stream<MouseEntry> mouseEntries;
    private final Stream<MouseEntry> mouseDraggedEntries;
    private final Stream<MouseWheelEntry> mouseWheelEntries;

    public InputEntry(Stream<KeyEntry> keyEntries, Stream<MouseEntry> mouseEntries, Stream<MouseWheelEntry> mouseWheelEntries, Stream<MouseEntry> mouseDraggedEntries) {
        this.keyEntries = keyEntries;
        this.mouseEntries = mouseEntries;
        this.mouseWheelEntries = mouseWheelEntries;
        this.mouseDraggedEntries = mouseDraggedEntries;
    }

    public Stream<KeyEntry> getKeyEntries() {
        return this.keyEntries;
    }

    public Stream<MouseEntry> getMouseEntries() {
        return this.mouseEntries;
    }

    public Stream<MouseWheelEntry> getMouseWheelEntries() {
        return this.mouseWheelEntries;
    }

    public Stream<MouseEntry> getMouseDraggedEntries() {
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
