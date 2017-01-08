package de.SweetCode.e;

import de.SweetCode.e.utils.Version;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Settings {

    /**
     * @return The name of the game.
     */
    default String getName() {
        return "e - 1.6021";
    }

    /**
     * @return The {@link Version} of the game.
     */
    default Version getVersion() {
        return new Version(1, 0, 0, 1, Version.ReleaseTag.NIGHTLY);
    }

    /**
     * @return The {@link TimeUnit} that the delta time should use.
     */
    default TimeUnit getDeltaUnit() {
        return TimeUnit.MILLISECONDS;
    }

    /**
     * @return If this is set to true, the delta time will always be 1 or higher.
     */
    default boolean roundDelta() {
        return true;
    }

    /**
     * @return The width of the screen.
     */
    default int getWidth() {
        return 1280;
    }

    /**
     * @return The height of the screen.
     */
    default int getHeight() {
        return 720;
    }

    /**
     * @return If true, than we will fix the aspect ratio on-the-fly if the size of the window is changing by
     *         repositioning the image correctly.
     */
    default boolean fixAspectRatio() {
        return false;
    }

    /**
     * @return The optimal FPS (Frames-Per-Second) target.
     */
    default int getTargetFPS() {
        return 25;
    }

    /**
     * @return How often should run the update-loop/second.
     */
    default int getTargetTicks() { return 64; }

    /**
     * @return The max. amount of log entries.
     */
    default int getLogCapacity() {
        return 1024;
    }

    /**
     * @return The amount of layers.
     */
    default int getAmountOfLayers() {
        return 1;
    }

    /**
     * @return If the screen is decorated.
     */
    default boolean isDecorated() {
        return false;
    }

    /**
     * @return If the screen is resizable.
     */
    default boolean isResizable() {
        return false;
    }

    /**
     * If the update-loop should run updates parallel to each other, it is using
     * internally a parallelized stream by default it is sequential. You should only
     * consider using this if:
     *  - you have a massive amount of components to update
     *  - the time it takes to update the component takes to long to satisfy your tick rate requirements
     *
     * A parallel stream has a higher overhead than the sequential version of it!
     * A parallel stream IGNORES all set priorities.
     * @return True, run it parallelized, otherwise not.
     */
    default boolean isParallelizingUpdate() { return false; }

    /**
     * @return Hints to be used while rendering.
     */
    default Map<RenderingHints.Key, Object> getRenderingHints() {
        return new HashMap<>();
    }

    /**
     * @return Rendering debugging information.
     */
    default boolean isDebugging() {
        return true;
    }

    /**
     * @return Define what you see of the debug information.
     */
    default List<DebugDisplay> getDebugInformation() {
        return Arrays.asList(DebugDisplay.values());
    }

    /**
     * Validator to validate settings.
     */
    final class Validator {

        public static List<String> validate(Settings settings) {

            List<String> invalids = new ArrayList<>();

            if(settings.getName() == null) {
                invalids.add("name cannot be null");
            }

            if(settings.getDeltaUnit() == null) {
                invalids.add("deltaUnit cannot be null");
            }

            if(settings.getVersion() == null) {
                invalids.add("version cannot be null");
            }

            if(settings.getTargetFPS() < 1) {
                invalids.add("targetFPS cannot be negative 0");
            }

            if(settings.getTargetTicks() < 1) {
                invalids.add("targetTicks cannot be negative or 0");
            }

            if(settings.getWidth() < 1) {
                invalids.add("width cannot be negative or 0");
            }

            if(settings.getHeight() < 1) {
                invalids.add("height cannot be negative or 0");
            }

            if(settings.getLogCapacity() < 1) {
                invalids.add("logCapacity cannot be negative or 0");
            }

            if(settings.getAmountOfLayers() < 1) {
                invalids.add("The amount of layers must be at least 1");
            }

            if(settings.getDebugInformation() == null) {
                invalids.add("The getDebugInformation cannot be null.");
            }

            return invalids;

        }

    }

    /**
     * The developer can chose what he wanna see as debug informatuon.
     */
    enum DebugDisplay {

        CPU_PROFILE,
        MEMORY_PROFILE,
        LOOP_PROFILE,
        GC_PROFILE,
        THREAD_PROFILE,

    }

}
