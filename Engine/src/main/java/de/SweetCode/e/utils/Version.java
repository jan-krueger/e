package de.SweetCode.e.utils;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class Version implements Comparable<Version> {

    private final String readable;

    private final ReleaseTag releaseTag;

    private final int major;
    private final int minor;
    private final int patch;

    private final int build;

    public Version(int major, int minor, int patch, int build, ReleaseTag releaseTag) {

        if((major < 0) ||(minor < 0) || (patch < 0) || (build < 0)) {
            throw new IllegalArgumentException("No part of the version can be negative.");
        }

        if(releaseTag == null) {
            throw new IllegalArgumentException("The release tag cannot be null.");
        }

        this.major = major;
        this.minor = minor;
        this.patch = patch;

        this.build = build;

        this.releaseTag = releaseTag;

        if(this.build == 0) {
            this.readable = String.format("%d.%d.%d%s", this.major, this.minor, this.patch, this.releaseTag.getTag());
        } else {
            this.readable = String.format("%d.%d.%d-%d%s", this.major, this.minor, this.patch, this.build, this.releaseTag.getTag());
        }
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

    public int getBuild() {
        return this.build;
    }

    /**
     * <p>
     *    Gives a human readable string of the version.
     * </p>
     * @return
     */
    public String getVersion() {
        return this.readable;
    }

    public ReleaseTag getReleaseTag() {
        return this.releaseTag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .build();
    }

    @Override
    public int compareTo(Version v) {

        int releaseTagDiff = this.releaseTag.getIndex() - v.getReleaseTag().getIndex();
        if(!(releaseTagDiff == 0)) {
            return (releaseTagDiff > 0 ? 1 : -1);
        }

        if(!(this.major == v.getMajor())) {
            return (this.major > v.getMajor() ? 1 : -1);
        }

        if(!(this.minor == v.getMinor())) {
            return (this.minor > v.getMinor() ? 1 : -1);
        }

        if(!(this.patch == v.getPatch())) {
            return (this.patch > v.getPatch() ? 1 : -1);
        }

        if(!(this.build == v.getBuild())) {
            return (this.build > v.getBuild() ? 1 : -1);
        }

        return 0;

    }

    public enum ReleaseTag {

        ALPHA("a", 0),
        BETA("b", 1),
        NIGHTLY("n", 2),
        STABLE("", 3);

        private final String tag;
        private final int index;

        ReleaseTag(String tag, int index) {
            this.tag = tag;
            this.index = index;
        }

        public String getTag() {
            return this.tag;
        }

        public int getIndex() {
            return this.index;
        }
    }

}
