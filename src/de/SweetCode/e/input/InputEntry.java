package de.SweetCode.e.input;

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

}
