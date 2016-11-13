package de.SweetCode.e;

import de.SweetCode.e.utils.Version;

import java.awt.*;
import java.util.HashMap;
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

    /** @TODO
    default Camera getCamera() {
        return new Camera(new Location(0, 0), new BoundingBox(new Location(0, 0), new Location(this.getWidth(), this.getHeight())));
    }**/

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
     * The optimal fps target.
     * @return
     */
    default int getTargetFPS() {
        return 100;
    }

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
     * @TODO
     * If the renderer should fix the aspect ratio on its own.
     * @return
     */
    /**default boolean fixAspectRatio() {
        return false;
    }**/

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

}
