package de.SweetCode.e.loop;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import de.SweetCode.e.E;
import de.SweetCode.e.EScreen;
import de.SweetCode.e.Settings;

/**
 * <p>
 * The profiler loop is responsible for rendering the frames frequently.
 * </p>
 */
public class RenderLoop extends Loop {

    private final EScreen screen;

    //--- OpenGL
    private GLProfile glProfile = null;
    private final FPSAnimator animator;

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

        if(EScreen.USE_JOGL) {
            this.glProfile = GLProfile.get(GLProfile.GL2);

            GLCapabilities glCapabilities = new GLCapabilities(this.glProfile);
            glCapabilities.setDoubleBuffered(true);

            Settings s = E.getE().getSettings();

            GLCanvas canvas = new GLCanvas(glCapabilities);
            canvas.addGLEventListener(E.getE().getScreen());
            canvas.setSize(s.getFrameDimension().getWidth(), s.getFrameDimension().getHeight());

            this.animator = new FPSAnimator(canvas, s.getTargetFPS());
            this.animator.setUpdateFPSFrames(s.getTargetFPS() / 5, null);

            this.animator.start();

            E.getE().getScreen().add(canvas);
            E.getE().getScreen().setVisible(true);


        }
    }

    /**
     * <p>
     *    Gives the GLProfile if the engine is using OpenGL to render the scene.
     * </p>
     *
     * @return Gives GLProfile, is null if OpenGL not active.
     */
    public GLProfile getGlProfile() {
        return this.glProfile;
    }

    /**
     * <p>
     *    Gives the FPSAnimator if the engine is using OpenGL to render the scene.
     * </p>
     *
     * @return Gives FPSAnimator, is null if OpenGL not active.
     */
    public FPSAnimator getAnimator() {
        return animator;
    }

    @Override
    public void tick(long updateLength) {

        if(!(EScreen.USE_JOGL)) {
            this.screen.invalidate();
            this.screen.repaint();
        }

    }


}
