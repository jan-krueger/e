package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.resources.file.HotSwapFile;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.log.LogEntry;
import de.SweetCode.e.utils.log.LogPrefixes;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class HotSwapLoop extends Loop {

    private Map<HotSwapFile, FileEntry> fileList = new HashMap<>();

    public HotSwapLoop(long optimalIterationTime) {
        super("HotSwapLoop", optimalIterationTime);
    }

    @Override
    public void tick(long updateLength) {

        this.fileList.forEach((file, fileEntry) -> {

            if(
                (file.lastModified() > fileEntry.getLastModified()) &&
                !(file.getCRC32() == fileEntry.getLastHash()) &&
                ((System.nanoTime() - fileEntry.getLastCheck()) <= System.nanoTime())
            ) {

                //--- Refresh the file's content
                file.refresh();

                //--- Update values
                FileEntry tmp = this.fileList.get(file);
                tmp.setLastModified(file.lastModified());
                tmp.setLastCheck((long) (fileEntry.getLastCheck() + (E.C.SECOND_AS_NANO / E.getE().getSettings().getHotSwapInterval())));
                tmp.setLastHash(file.getCRC32());
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

        //--- Add the file
        this.fileList.put(file, new FileEntry(
                file.lastModified(),
                (long) (System.nanoTime() + (E.C.SECOND_AS_NANO / E.getE().getSettings().getHotSwapInterval())),
                file.getCRC32()
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
        private long lastHash;

        private FileEntry(long lastModified, long lastCheck, long lastHash) {
            this.lastModified = lastModified;
            this.lastCheck = lastCheck;
            this.lastHash = lastHash;
        }

        private long getLastModified() {
            return this.lastModified;
        }

        private long getLastCheck() {
            return this.lastCheck;
        }

        private long getLastHash() {
            return this.lastHash;
        }

        private void setLastCheck(long lastCheck) {
            this.lastCheck = lastCheck;
        }

        private void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        private void setLastHash(long lastHash) {
            this.lastHash = lastHash;
        }
    }

}
