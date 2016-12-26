package de.SweetCode.e.utils;

public enum Platform {

    WINDOWS,
    MAC_OS_X,
    LINUX,
    UNKNOWN;

    private final static String OS_NAME = System.getProperty("os.name");
    private static Platform OS_CACHE = null;

    public static Platform getPlatform() {

        if(Platform.OS_NAME == null) {
            return UNKNOWN;
        }

        if(!(Platform.OS_CACHE == null)) {
            return Platform.OS_CACHE;
        }

        if(Platform.OS_NAME.startsWith("Windows")) {
            return (Platform.OS_CACHE = WINDOWS);
        } else if(
            Platform.OS_NAME.startsWith("Linux")   ||
            Platform.OS_NAME.startsWith("LINUX")   ||
            Platform.OS_NAME.startsWith("FreeBSD") ||
            Platform.OS_NAME.startsWith("Unix")    ||
            Platform.OS_NAME.startsWith("SunOS")
        ) {
            return (Platform.OS_CACHE = LINUX);
        } else if(
            Platform.OS_NAME.startsWith("Mac OS X") ||
            Platform.OS_NAME.startsWith("Darwin")
        ) {
            return (Platform.OS_CACHE = MAC_OS_X);
        }

        return (Platform.OS_CACHE = UNKNOWN);

    }

}
