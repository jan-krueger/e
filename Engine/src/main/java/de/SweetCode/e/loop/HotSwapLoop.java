package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.resources.file.HotSwapFile;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.log.LogEntry;
import de.SweetCode.e.utils.log.LogPrefixes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class HotSwapLoop extends Loop {

    private Map<HotSwapFile, FileEntry> fileList = new HashMap<>();

    /**
     * <p>
     * Creates a new loop.
     * </p>
     *
     * @param optimalIterationTime The time in {@link TimeUnit#NANOSECONDS} between each update call.
     */
    public HotSwapLoop(long optimalIterationTime) {
        super("HotSwapLoop", optimalIterationTime);
    }

    @Override
    public void tick(long updateLength) {

        this.fileList.forEach((file, fileEntry) -> {

            if(
                (file.lastModified() > fileEntry.getLastModified()) &&
                ((System.nanoTime() - fileEntry.getLastCheck()) <= System.nanoTime())
            ) {

                //--- Refresh the file's content
                file.refresh();

                FileEntry tmp = this.fileList.get(file);
                tmp.setLastModified(file.lastModified());
                tmp.setLastCheck((long) (fileEntry.getLastCheck() + (E.C.SECOND_AS_NANO / E.getE().getSettings().getHotSwapInterval())));
            }

        });

    }


    public void addFile(HotSwapFile file) {

        Assert.assertNotNull("The HotSwapFile cannot be null.", file);

        if(this.fileList.containsKey(file)) {
            throw new IllegalArgumentException(String.format("The file %s (%s) has already been added to the HotSwap file system.",
                    file.getName(),
                    file.getAbsolutePath()
            ));
        }

        this.fileList.put(file, new FileEntry(
                file.lastModified(),
                (long) (System.nanoTime() + (E.C.SECOND_AS_NANO / E.getE().getSettings().getHotSwapInterval()))
        ));

        if(E.getE().getSettings().isHotSwapEnabled()) {
            E.getE().getLog().log(
                LogEntry.Builder.create(this.getClass())
                    .prefix(LogPrefixes.HOT_SWAP)
                    .message("The file %s (%s) was successfully added to the HotSwap file system.", file.getName(), file.getAbsolutePath())
                .build()
            );
        } else {
            E.getE().getLog().log(
                LogEntry.Builder.create(this.getClass())
                    .prefix(LogPrefixes.HOT_SWAP)
                    .message("The file %s (%s) was successfully added to the HotSwap file system, but it is" +
                            " currently not automatically refreshing because the feature is not enabled. Please activate" +
                            " it in Settings#isHotSwapEnabled().")
                .build()
            );
        }

    }

    /**
     * A simple wrapper class to keep track of two timestamps.
     */
    private class FileEntry {

        private long lastModified;
        private long lastCheck;

        public FileEntry(long lastModified, long lastCheck) {
            this.lastModified = lastModified;
            this.lastCheck = lastCheck;
        }

        public long getLastModified() {
            return this.lastModified;
        }

        public long getLastCheck() {
            return this.lastCheck;
        }

        public void setLastCheck(long lastCheck) {
            this.lastCheck = lastCheck;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }
    }

}
