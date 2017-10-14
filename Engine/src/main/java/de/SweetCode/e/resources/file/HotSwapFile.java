package de.SweetCode.e.resources.file;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.log.LogEntry;
import de.SweetCode.e.utils.log.LogPrefixes;
import org.apache.commons.io.FileUtils;

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

    /**
     * <p>
     *     The callback to notify.
     * </p>
     */
    private HotSwapCallback callback = new HotSwapCallback() {};

    public HotSwapFile(String pathname) {
        super(pathname);

        this.register();
    }

    public HotSwapFile(String pathname, HotSwapCallback callback) {
        this(pathname);

        Assert.assertNotNull(callback);
        this.callback = callback;
    }

    public HotSwapFile(String parent, String child) {
        super(parent, child);

        this.register();
    }

    public HotSwapFile(String parent, String child, HotSwapCallback callback) {
        this(parent, child);

        Assert.assertNotNull(callback);
        this.callback = callback;
    }

    public HotSwapFile(File parent, String child) {
        super(parent, child);

        this.register();
    }

    public HotSwapFile(File parent, String child, HotSwapCallback callback) {
        this(parent, child);

        Assert.assertNotNull(callback);
        this.callback = callback;
    }

    public HotSwapFile(URI uri) {
        super(uri);

        this.register();
    }

    public HotSwapFile(URI uri, HotSwapCallback callback) {
        this(uri);

        Assert.assertNotNull(callback);
        this.callback = callback;
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
     *     Computes the checksum of the file using the CRC32 algorithm.
     * </p>
     *
     * @return the hashsum, negative if it failed to compute.
     */
    public final long getCRC32() {
        try {
            return FileUtils.checksumCRC32(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
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

    @Override
    public boolean createNewFile() {
        throw new IllegalStateException("The method is not supported");
    }

    @Override
    public boolean mkdir() {
        throw new IllegalStateException("The method is not supported");
    }

    @Override
    public boolean mkdirs() {
        throw new IllegalStateException("The method is not supported");
    }

    @Override
    public boolean delete() {
        E.getE().getHotSwapLoop().removeFile(this);

        return super.delete();
    }

    /**
     * <p>
     *    Registers the file in the HotSwapLoop.
     * </p>
     */
    private void register() {

        Assert.assertTrue("The HotSwap system only supports existing files.", this.exists());
        Assert.assertTrue("The HotSwap system only supports files.", this.isFile());
        Assert.assertTrue("The HotSwap file has to be readable.", this.canRead());

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
            this.callback.refreshed(
                this.content,                                       //--- Old Content
                (this.content = Files.readAllBytes(this.toPath()))  //--- Updated Content
            );
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * <p>
     *     The HotSwapCallback class is provided the by the developer, so they can get notified when the content changes.
     * </p>
     */
    public interface HotSwapCallback {

        /**
         * <p>
         *     Gets called when a filed was updated and its content changed.
         * </p>
         *
         * @param old The old content of the file.
         * @param updated The updated content of the file.
         */
        default void refreshed(byte[] old, byte[] updated) {}

    }

}
