package de.SweetCode.e;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.utils.Version;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     An instance of the Settings can be provided to the {@link E#E(Settings)}
 *     to modify basic behaviour of the engine and its tasks, however the
 *     also just works fine with the default settings. There is no need
 *     to provide them, but it is recommend to take a look at them to avoid
 *     developing basic functionality that is already included.
 * </pre>
 */
public interface Settings {

    /**
     * <pre>
     *     Defines the name of the application. This is e.g. used to set
     *     the name of the window shown by the OS.
     * </pre>
     *
     * @return The name of the game.
     */
    default String getName() {
        return "e - 1.6021";
    }

    /**
     * <pre>
     *     Defines the version of the application. The application expects you to
     *     use SemVer 2.0. You can find more documentation on that on the internet
     *     or on <a href="http://semver.org">http://semver.org</a>.
     * </pre>
     *
     * @return The {@link Version} of the game.
     */
    default Version getVersion() {
        return new Version(1, 0, 0, 1, Version.ReleaseTag.NIGHTLY);
    }

    /**
     * <pre>
     *     Defines the time-unite that is used to provide the delta time (the
     *     amount of time that passed by since the last update call) to the
     *     methods, e.g.: {@link GameComponent#update(InputEntry, long)}. The
     *     {@link de.SweetCode.e.loop.UpdateLoop} behind the scenes is using
     *     nano-seconds for all calculations and is just scaling the value up-and-down
     *     to fit the requirements.
     * </pre>
     *
     * @return The {@link TimeUnit} that the delta time should use.
     */
    default TimeUnit getDeltaUnit() {
        return TimeUnit.MILLISECONDS;
    }

    /**
     * <pre>
     *      Defines if the engine should provide rounded delta times, but
     *      it <b>does not</b> mean rounding up in what you might think since
     *      we are using floats their are no floating-point values that might
     *      mess something up, however it can happen that the time passed by
     *      so fast that there is no difference in time that the engine could
     *      measure.
     *
     *      Example: {@link Settings#getDeltaUnit()} requires the program to use
     *      {@link TimeUnit#MILLISECONDS}. 1ms = 1000000ns. - If the last call is
     *      0.4ms old the transformed delta-time to {@link TimeUnit#MILLISECONDS}
     *      would return 0ms. If this method returns true you make sure that the
     *      delta-value is at least 1.
     * </pre>
     *
     * @return If this is set to true, the delta time will always be 1 or higher.
     */
    default boolean roundDelta() {
        return true;
    }

    /**
     * <pre>
     *     Defines the width of the canvas in pixel you can draw to
     * </pre>
     *
     * @return The width of the screen.
     */
    default int getWidth() {
        return 1280;
    }

    /**
     * <pre>
     *     Defines the height of the canvas in pixel you can draw to.
     * </pre>
     *
     * @return The height of the screen.
     */
    default int getHeight() {
        return 720;
    }

    /**
     * <pre>
     *     Defines whether the engine should fix the aspect ratio or not
     *     if the window sizes changes. This can happen if you allow the
     *     user to resize the size of the window. If this method returns
     *     true then the engine scales the frame correctly while keeping
     *     the aspect ratio of it.
     * </pre>
     *
     * @return If true, than we will fix the aspect ratio on-the-fly if the size of the window is changing by
     *         repositioning the image correctly.
     */
    default boolean fixAspectRatio() {
        return false;
    }

    /**
     * <pre>
     *     This sets the max. amount of frames/second that can be
     *     drawn to the screen. The real value can vary by several
     *     frames. This also defines how often the {@link de.SweetCode.e.loop.RenderLoop} will
     *     call the {@link de.SweetCode.e.rendering.GameScene#render(Layers)} method.
     * </pre>
     *
     * @return The optimal FPS (Frames-Per-Second) target.
     */
    default int getTargetFPS() {
        return 25;
    }

    /**
     * <pre>
     *     Defines the amount of ticks aka. updates the {@link de.SweetCode.e.loop.UpdateLoop}
     *     should perform. The real value can vary by several ticks
     *     per second, however you should not see any huge variations.
     *     This also defines how often the {@link de.SweetCode.e.loop.UpdateLoop}
     *     will call the {@link GameComponent#update(InputEntry, long)}
     *     and {@link de.SweetCode.e.rendering.GameScene#update(InputEntry, long)}
     *     methods.
     * </pre>
     *
     * @return How often should run the update-loop/second.
     */
    default int getTargetTicks() { return 64; }

    /**
     * <pre>
     *     The maximum of elements the log can store. The log
     *     is used by the engine, but you can also send your
     *     own entries to it. If the log reaches the maximum it
     *     will delete the oldest entry in it.
     * </pre>
     *
     * @return The max. amount of log entries.
     */
    default int getLogCapacity() {
        return 1024;
    }

    /**
     * <pre>
     *     Defines the amount of {@link de.SweetCode.e.rendering.layers.Layer layers} the engine
     *     provides. The engine allows you to draw on several layers
     *     at once and giving them different alpha-values to blend
     *     them in our out.
     * </pre>
     *
     * @return The amount of layers.
     */
    default int getAmountOfLayers() {
        return 1;
    }

    /**
     * <pre>
     *     Defines if the engine should show the user
     *     a option bar to close, minimize or full-screen
     *     the window.
     * </pre>
     *
     * @return If the screen is decorated.
     */
    default boolean isDecorated() {
        return false;
    }

    /**
     * <pre>
     *     Defines if the screen is resizable by the user.
     * </pre>
     *
     * @return If the screen is resizable.
     */
    default boolean isResizable() {
        return false;
    }

    /**
     * <pre>
     *     If the update-loop should run updates parallel to each other, it is using
     *     internally a parallelized stream by default it is sequential. You should only
     *     consider using this if:
     * </pre>
     * <ul>
     *     <li>you have a massive amount of components to update</li>
     *     <li>the time it takes to update the component takes to long to satisfy your tick rate requirements</li>
     * </ul>
     * <pre>
     *     <b>A parallel stream has a higher overhead than the sequential version of it!
     *     A parallel stream IGNORES all set priorities.</b>
     * </pre>
     *
     * @return True, run it parallelized, otherwise not.
     */
    default boolean isParallelizingUpdate() { return false; }

    /**
     * <pre>
     *     Defines {@link RenderingHints} used by the renderer
     *     to draw the screen.
     * </pre>
     *
     * @return Hints to be used while rendering.
     */
    default Map<RenderingHints.Key, Object> getRenderingHints() {
        return new HashMap<>();
    }

    /**
     * <pre>
     *     Defines if the engine is in the debug mode or
     *     not. - If it is in the debug mode it will start
     *     to collect data about the process, memory, CPU
     *     and so on and display them on top of the frame.
     *
     *     It also forwards everything send to the {@link de.SweetCode.e.utils.log.Log}
     *     directly to {@link System#out}.
     * </pre>
     *
     * @return Rendering debugging information.
     */
    default boolean isDebugging() {
        return true;
    }

    /**
     * <pre>
     *     Defines the information the engine should should
     *     log and show the developer, if the debug mode is
     *     enabled.
     * </pre>
     *
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
