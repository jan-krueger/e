package de.SweetCode.e.utils;

/**
 * <p>
 *     Platform allows you to check on which OS your application is running.
 * </p>
 */
public enum Platform {

    WINDOWS,
    MAC_OS_X,
    LINUX,
    UNKNOWN;

    private final static String OS_NAME = System.getProperty("os.name");
    private static Platform OS_CACHE = null;

    /**
     * <p>
     *    Calls {@link Platform#getPlatform(boolean)} without giving the command to clear
     *    the cache.
     * </p>
     *
     * @return The operating system the engine is running on, never null.
     */
    public static Platform getPlatform() {
        return getPlatform(false);
    }

    /**
     * <p>
     *     Determines on which OS the application is running. This method doesn't only functions as getter, but also
     *     performs the necessary operations to figure out on which operating system the engine is running.
     * </p>
     *
     * @param clearCache True, to force the OS cache to be cleared and to trigger a new operation to determine the OS.
     *
     * @return The operating system the engine is running on, never null.
     */
    public static Platform getPlatform(boolean clearCache) {

        if(OS_NAME == null) {
            return UNKNOWN;
        }

        if(clearCache) {
            Platform.clearCache();
        }

        if(!(OS_CACHE == null)) {
            return OS_CACHE;
        }

        if(OS_NAME.startsWith("Windows")) {
            return (OS_CACHE = WINDOWS);
        } else if(
            OS_NAME.startsWith("Linux")   ||
            OS_NAME.startsWith("LINUX")   ||
            OS_NAME.startsWith("FreeBSD") ||
            OS_NAME.startsWith("Unix")    ||
            OS_NAME.startsWith("SunOS")
        ) {
            return (OS_CACHE = LINUX);
        } else if(
            OS_NAME.startsWith("Mac OS X") ||
            OS_NAME.startsWith("Darwin")
        ) {
            return (OS_CACHE = MAC_OS_X);
        }

        return (OS_CACHE = UNKNOWN);

    }

    /**
     * <p>
     *    Clears the OSCACHE.
     * </p>
     */
    public static void clearCache() {
        OS_CACHE = null;
    }

}
