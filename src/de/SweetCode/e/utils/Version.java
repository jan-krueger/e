package de.SweetCode.e.utils;

public class Version {

    private final String readable;

    private final ReleaseTag releaseTag;

    private final int major;
    private final int minor;
    private final int patch;

    public Version(int major, int minor, int patch, ReleaseTag releaseTag) {

        if((major < 0) ||(minor < 0) || (patch < 0)) {
            throw new IllegalArgumentException("No part of the version can be negative.");
        }

        if(releaseTag == null) {
            throw new IllegalArgumentException("The release tag cannot be null.");
        }

        this.major = major;
        this.minor = minor;
        this.patch = patch;

        this.releaseTag = releaseTag;

        this.readable = String.format("%d.%d.%d%s", this.major, this.minor, this.patch, this.releaseTag.getTag());
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getPatch() {
        return this.patch;
    }

    public String getVersion() {
        return this.readable;
    }

    public ReleaseTag getReleaseTag() {
        return this.releaseTag;
    }

    @Override
    public String toString() {
        return this.getVersion();
    }

    public enum ReleaseTag {

        ALPHA("a"),
        BETA("b"),
        NIGHTLY("n"),
        STABLE("");

        private final String tag;

        ReleaseTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }
    }

}
