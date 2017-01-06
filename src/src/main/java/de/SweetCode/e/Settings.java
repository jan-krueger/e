package de.SweetCode.e;

import de.SweetCode.e.utils.Version;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Settings {

    /**
     * The name of the game.
     * @return
     */
    default String getName() {
        return "e - 1.6021";
    }

    /**
     * The version of the game.
     * @return
     */
    default Version getVersion() {
        return new Version(1, 0, 0, 1, Version.ReleaseTag.NIGHTLY);
    }

    /**
     * The timeunit that the delta time should use.
     * @return
     */
    default TimeUnit getDeltaUnit() {
        return TimeUnit.MILLISECONDS;
    }

    /**
     * If this is set to true, the delta time will always be 1 or higher.
     * @return
     */
    default boolean roundDelta() {
        return true;
    }

    /**
     * The width of the screen.
     * @return
     */
    default int getWidth() {
        return 1280;
    }

    /**
     * The height of the screen.
     * @return
     */
    default int getHeight() {
        return 720;
    }

    /**
     * The optimal FPS (Frames-Per-Second) target.
     * @return
     */
    default int getTargetFPS() {
        return 25;
    }

    /**
     * How often should run the update-loop/second.
      * @return
     */
    default int getTargetTicks() { return 64; }

    /**
     * The max. amount of log entries.
     * @return
     */
    default int getLogCapacity() {
        return 1024;
    }

    /**
     * The amount of layers.
     * @return
     */
    default int getAmountOfLayers() {
        return 1;
    }

    /**
     * If the screen is decorated.
     * @return
     */
    default boolean isDecorated() {
        return false;
    }

    /**
     * If the screen is resizable.
     * @return
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
     * @return
     */
    default boolean isParallelizingUpdate() { return false; }

    /**
     * Hints to be used while rendering.
     * @return
     */
    default Map<RenderingHints.Key, Object> getRenderingHints() {
        return new HashMap<>();
    }

    /**
     * Rendering debugging information.
     * @return
     */
    default boolean isDebugging() {
        return true;
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

            return invalids;

        }

    }

}
