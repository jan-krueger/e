package de.SweetCode.e.resources.file;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.log.LogEntry;
import de.SweetCode.e.utils.log.LogPrefixes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

public class HotSwapFile extends File {

    /**
     * <p>
     *     The content of the file.
     * </p>
     */
    private byte[] content;

    public HotSwapFile(String pathname) {
        super(pathname);

        this.register();
    }

    public HotSwapFile(String parent, String child) {
        super(parent, child);

        this.register();
    }

    public HotSwapFile(File parent, String child) {
        super(parent, child);

        this.register();
    }

    public HotSwapFile(URI uri) {
        super(uri);

        this.register();
    }

    /**
     * <p>
     *     The content of the file in bytes.
     * </p>
     *
     * @return byte[] of the content.
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * <p>
     *     Loads the current content of the file into memory and makes it accessible through {@link HotSwapFile#getContent()}.
     * </p>
     */
    public void refresh() {
        if(this.loadContent()) {
            E.getE().getLog().log(
                LogEntry.Builder.create(this.getClass())
                    .prefix(LogPrefixes.HOT_SWAP)
                    .message("The file's %s (%s) content has been refreshed.", this.getName(), this.getAbsolutePath())
                .build()
            );
        } else {
            E.getE().getLog().log(
                LogEntry.Builder.create(this.getClass())
                    .prefix(LogPrefixes.HOT_SWAP)
                    .message("The file's %s (%s) content couldn't be refreshed.", this.getName(), this.getAbsolutePath())
                .build()
            );
        }
    }

    /**
     * <p>
     *    Registers the file in the HotSwapLoop.
     * </p>
     */
    private void register() {
        if(this.loadContent()) {
            E.getE().getHotSwapLoop().addFile(this);
        } else {
            E.getE().getLog().log(
                LogEntry.Builder.create(this.getClass())
                    .prefix(LogPrefixes.HOT_SWAP)
                    .message("Failed to add the file %s (%s) to the HotSwap system.", this.getName(), this.getAbsolutePath())
                .build()
            );
        }
    }

    /**
     * <p>
     *     Loads the current code of the file.
     * </p>
     */
    private boolean loadContent() {
        try {
            this.content = Files.readAllBytes(this.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
