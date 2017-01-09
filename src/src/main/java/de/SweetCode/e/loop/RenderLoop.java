package de.SweetCode.e.loop;

import de.SweetCode.e.EScreen;

/**
 * <p>
 * The profiler loop is responsible for rendering the frames frequently.
 * </p>
 */
public class RenderLoop extends Loop {

    private final EScreen screen;

    /**
     * <p>
     *     Creates a new RenderLoop.
     * </p>
     *
     * @param screen The screen the loop is supposed to render.
     * @param optimalTime The amount of time between each frame in {@link java.util.concurrent.TimeUnit#NANOSECONDS}.
     */
    public RenderLoop(EScreen screen, long optimalTime) {
        super("Render Loop", optimalTime);

        this.screen  = screen;
    }

    @Override
    public void tick(long updateLength) {
        this.screen.invalidate();
        this.screen.repaint();
    }


}
