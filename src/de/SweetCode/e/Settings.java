package de.SweetCode.e;

import de.SweetCode.e.utils.Version;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Settings {

    /**
     * The name of the game.
     * @return
     */
    String getName();

    /**
     * The version of the game.
     * @return
     */
    Version getVersion();

    /**
     * The timeunit that the delta time should use.
     * @return
     */
    TimeUnit getDeltaUnit();

    /**
     * If this is set to true, the delta time will always be 1 or higher.
     * @return
     */
    boolean roundDelta();

    /**
     * The width of the screen.
     * @return
     */
    int getWidth();

    /**
     * The height of the screen.
     * @return
     */
    int getHeight();

    /**
     * The optimal fps target.
     * @return
     */
    int getTargetFPS();

    /**
     * The max. amount of log entries.
     * @return
     */
    int getLogCapacity();

    /**
     * The amount of layers.
     * @return
     */
    int getAmountOfLayers();

    /**
     * If the screen is decorated.
     * @return
     */
    boolean isDecorated();

    /**
     * If the screen is resizable.
     * @return
     */
    boolean isResizable();

    /**
     * Hints to be used while rendering.
     * @return
     */
    Map<RenderingHints.Key, Object> getRenderingHints();

}
