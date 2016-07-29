package de.SweetCode.e.input;

import de.SweetCode.e.utils.StringUtils;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.stream.Stream;

public class InputEntry {

    private final Stream<KeyEntry> keyEntries;
    private final Stream<MouseEntry> mouseEntries;

    public InputEntry(Stream<KeyEntry> keyEntries, Stream<MouseEntry> mouseEntries) {
        this.keyEntries = keyEntries;
        this.mouseEntries = mouseEntries;
    }

    public Stream<KeyEntry> getKeyEntries() {
        return this.keyEntries;
    }

    public Stream<MouseEntry> getMouseEntries() {
        return this.mouseEntries;
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
            .build();
    }

}
