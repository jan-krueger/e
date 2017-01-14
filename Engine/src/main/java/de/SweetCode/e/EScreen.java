package de.SweetCode.e;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import de.SweetCode.e.loop.ProfilerLoop;
import de.SweetCode.e.math.IDimension;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.AspectRatio;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.utils.log.LogEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;
import java.lang.management.GarbageCollectorMXBean;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * EScreen is the component of the engine that is responsible for rendering.
 * </p>
 */
public class EScreen extends JFrame implements GLEventListener {

    /**
     * @TODO:
     * Experimental Feature - it will allocate VRAM instead of RAM
     * to store and draw frames. This will also be used for the DynamicTextureLoader
     * to reduce the used RAM.
     *
     * I am currently working on some bugs and on the implementation itself to ensure its
     * performance.
     */
    public static final boolean USE_VRAM = false;

    /**
     * @TODO:
     * Experimental Feature: using OpenGL to render the frame.
     */
    public static final boolean USE_JOGL = true;

    private BufferStrategy bufferStrategy;
    private GameScene current = null;

    private static GraphicsConfiguration graphicConfiguration;

    EScreen() {

        Settings settings = E.getE().getSettings();
        this.setTitle(settings.getName());
        this.setUndecorated(!settings.isDecorated());
        this.setResizable(settings.isResizable());
        this.setPreferredSize(settings.getWindowDimension().toDimension());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        EScreen.graphicConfiguration = this.getGraphicsConfiguration();

        if(!(USE_JOGL)) {
            this.setVisible(true);

            this.createBufferStrategy(3);
            this.bufferStrategy = this.getBufferStrategy();

            if (this.bufferStrategy == null) {
                E.getE().getLog().log(LogEntry.Builder.create().message("Failed to create BufferStrategy.").build());
            }

        }

    }

    /**
     * <p>
     *    Returns the game scene that is currently rendered.
     * </p>
     *
     * @return The {@link GameScene} which is currently rendered.
     */
    public GameScene getCurrent() {
        return this.current;
    }

    /**
     * <p>
     *    Sets the scene that should be rendered.
     * </p>
     *
     * @param gameScene The new active {@link GameScene}.
     */
    void setScene(GameScene gameScene) {
        this.current = gameScene;
        this.invalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics graphics) {

        if(USE_JOGL) {
            super.paint(graphics);
            return;
        }

        if (this.current == null) {
            return;
        }

        Settings s = E.getE().getSettings();
        do {

            Graphics2D g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
            g.setRenderingHints(E.getE().getSettings().getRenderingHints());

            Image frame = this.frame();

            ILocation drawPosition = new ILocation(0, 0);

            if(E.getE().getSettings().fixAspectRatio()) {
                AspectRatio.Result aspectRatio = AspectRatio.calculateOptimal(s.getFrameDimension(), s.getWindowDimension());

                drawPosition = aspectRatio.getPosition();
                IDimension dimension = aspectRatio.getDimension();

                //Note: only rescale if necessary
                if(!(frame.getWidth(null) == dimension.getWidth()) || !(frame.getHeight(null) == dimension.getHeight())) {
                    frame = frame.getScaledInstance(dimension.getWidth(), dimension.getHeight(), Image.SCALE_SMOOTH);
                }
            }

            g.drawImage(frame, drawPosition.getX(), drawPosition.getY(), null);

            this.bufferStrategy.show();
            g.dispose();

            E.getE().getLayers().getLayers().forEach(Layer::clean);

        } while(this.bufferStrategy.contentsLost());

    }

    /**
     * <p>
     *    The configuration used to generate and use {@link VolatileImage VolatileImages}.
     * </p>
     *
     * @return The globally used {@link GraphicsConfiguration}.
     */
    public static GraphicsConfiguration getGraphicConfiguration() {
        return EScreen.graphicConfiguration;
    }

    /**
     * <p>
     *     This method generates the next frame that is going to be rendered to the screen. It does this by calling
     *     all {@link GameScene#render(Layers)} methods of all active scenes. Then it checks if the frame should also
     *     contain debug information and calls {@link EScreen#drawDebugInformation()} to draw them if required, and then
     *     it finally calls {@link Layers#combine()} to combine all images and to get the final frame.
     * </p>
     *
     * @return The frame to render.
     */
    private BufferedImage frame() {

        E.getE().getGameComponents().forEach(k -> {
            GameComponent e = k.getGameComponent();

            if (e instanceof Renderable && e.isActive()) {
                ((Renderable) e).render(E.getE().getLayers());
            }

        });

        //--- Debugging
        if(E.getE().getSettings().isDebugging() && !(E.getE().getSettings().getDebugInformation().isEmpty())) {
            EScreen.drawDebugInformation();
        }
        //---

        return E.getE().getLayers().combine();
    }

    @Override
    public void init(GLAutoDrawable drawable) {}

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {

        if(!(this.current == null)) {

            drawable.swapBuffers();

            // getting the new frame
            BufferedImage frame = this.frame();

            // Frame to Buffer
            // Note and @TODO: We have to convert RGBA to BGRA for some reason. This works currently but the performance
            // might not be the best. We should think about caching data from old frames, maybe or something compareable.
            int[] data = ((DataBufferInt)frame.getRaster().getDataBuffer()).getData();
            IntBuffer buffer = IntBuffer.allocate(data.length);
            Arrays.stream(data).forEachOrdered(e -> {

                int red = (e >> 16) & 0xFF;
                int green = (e >> 8) & 0xFF;
                int blue = (e & 0xFF) & 0xFF;
                int alpha = (e >> 24) & 0xFF;

                buffer.put(
                        ((alpha & 0xFF) << 24)  |
                        ((red & 0xFF)   << 0)   |
                        ((green & 0xFF) << 8)   |
                        ((blue & 0xFF)  << 16)
                );
            });
            buffer.flip();

            GL2 gl = drawable.getGL().getGL2();

            // bypass sync
            gl.setSwapInterval(0);

            // clear
            gl.glClearColor(0F, 0F, 0F, 0F);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();

            // generating & binding texture
            TextureData textureData = new TextureData(
                    E.getE().getRenderLoop().getGlProfile(),         //glp                   GLProfile
                    GL.GL_RGBA,             //internalFormat        int
                    frame.getWidth(),       //width                 int
                    frame.getHeight(),      //height                int
                    0,                      //border                int
                    GL.GL_RGBA,             //pixelFormat           int
                    GL2.GL_UNSIGNED_BYTE,   //pixelType             int
                    false,                  //mipmap                boolean
                    false,                  //dataIsCompressed      boolean
                    false,                  //mustFlopVertically    boolean
                    buffer,                 //buffer                Buffer
                    null                    //flusher               TextureData.Flusher
            );
            Texture texture = new Texture(gl, textureData);
            texture.enable(gl);
            texture.bind(gl);

            // viewport
            gl.glViewport(0, 0, frame.getWidth(), frame.getHeight());

            gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(0, frame.getWidth(), frame.getHeight(), 0, 0, 1);

            gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
            gl.glLoadIdentity();

            gl.glBegin(GL2.GL_QUADS);

            gl.glTexCoord2f(0, 0);
            gl.glVertex2f(0, 0);

            gl.glTexCoord2f(1, 0);
            gl.glVertex2f(frame.getWidth(), 0);

            gl.glTexCoord2f(1, 1);
            gl.glVertex2f(frame.getWidth(), frame.getHeight());

            gl.glTexCoord2f(0, 1);
            gl.glVertex2f(0, frame.getHeight());

            gl.glEnd();
            gl.glFlush();

            textureData.destroy();
            textureData.flush();
            texture.disable(gl);
            texture.destroy(gl);

            buffer.clear();

            drawable.swapBuffers();

            E.getE().getLayers().getLayers().forEach(Layer::clean);

        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}

    /**
     * <p>
     *     Draws all debug information to the {@link Layers#first()} layer, but only those which are in {@link Settings#getDebugInformation()}.
     * </p>
     */
    private static void drawDebugInformation() {

        Settings settings = E.getE().getSettings();
        Layer layer = E.getE().getLayers().first();

        ProfilerLoop profilerLoop = E.getE().getProfilerLoop();
        List<Settings.DebugDisplay> displays = settings.getDebugInformation();

        //--- Offsets
        int xOffset = 360;
        int yOffset = 12;

        int xStep = 1;

        int width = settings.getFrameDimension().getWidth();

        layer.g().setColor(
                EScreen.highContrast(
                        new Color(layer.b().getRGB(width - xOffset / 2, (int) (yOffset * 1.5D)))
                )
        );

        //--- CPU_PROFILE
        if(displays.contains(Settings.DebugDisplay.CPU_PROFILE)) {
            layer.g().drawString(
                    String.format(
                        "CPU: %.2f%% | Cores: %d",
                            profilerLoop.getAverageCPU() * 100,
                            profilerLoop.getAvailableProcessors()
                    ),
                    width - xOffset,
                    yOffset * xStep
            );

            xStep++;
        }

        if(displays.contains(Settings.DebugDisplay.LOOP_PROFILE)) {
            layer.g().drawString(
                    String.format(
                        "FPS: %d (%d) | Ticks: %d (%d) | Objects: %d (%.4f%%)",
                            E.getE().getCurrentFPS(),
                            settings.getTargetFPS(),
                            E.getE().getCurrentTicks(),
                            settings.getTargetTicks(),
                            E.getE().getGameComponents().size(),
                            ((double) profilerLoop.getActiveObjects() / E.getE().getGameComponents().size()) * 100D
                    ),
                    width - xOffset,
                    yOffset * xStep
            );

            layer.g().drawString(
                    String.format(
                            "VRAM: %s | OpenGL: %s | Updates: %s",
                            (EScreen.USE_VRAM ? "on" : "off"),
                            (EScreen.USE_JOGL ? "on" : "off"),
                            (E.getE().getSettings().isParallelizingUpdate() ? "parallelized" : "sequential")
                    ),
                    width - xOffset,
                    yOffset * (xStep + 1)
            );


            xStep += 2;
        }

        //--- MEMORY_PROFILE
        if(displays.contains(Settings.DebugDisplay.MEMORY_PROFILE)) {
            layer.g().drawString("Memory Usage JVM & Heap", width - xOffset, yOffset * xStep);
            layer.g().drawString(
                    String.format(
                        "JVM - Used: %.2fMB",
                            profilerLoop.getAverageJvmMemoryUsed() * E.C.BYTES_TO_MEGABYTES
                    ),
                    (int) (width - xOffset * 0.95),
                    yOffset * (xStep + 1)
            );
            layer.g().drawString(
                    String.format(
                        "Heap - Max: %.2fMB | Used: %.2fMB",
                            profilerLoop.getMaxHeapSize() * E.C.BYTES_TO_MEGABYTES,
                            profilerLoop.getAverageHeapMemoryUsed() * E.C.BYTES_TO_MEGABYTES
                    ),
                    (int) (width - xOffset * 0.95),
                    yOffset * (xStep + 2)
            );

            xStep += 3;
        }

        //--- GC_PROFILE
        if(displays.contains(Settings.DebugDisplay.GC_PROFILE)) {
            List<GarbageCollectorMXBean> gcBeans = profilerLoop.getGCBeans();
            layer.g().drawString(
                    String.format(
                        "GCs: %d",
                            gcBeans.size()
                    ),
                    width - xOffset,
                    yOffset * xStep
            );

            int gcTotalSize = gcBeans.size();
            int beanIndex = 0;
            for (int i = 0; i < gcTotalSize; i++) {

                GarbageCollectorMXBean gc = gcBeans.get(beanIndex);
                layer.g().drawString(
                        String.format(
                            "%s, %d (%dms)",
                                gc.getName(),
                                gc.getCollectionCount(),
                                gc.getCollectionTime()
                        ),
                        (int) (width - xOffset * 0.95),
                        yOffset * ((xStep + 1) + i)
                );

                //--- Pools
                String[] pools = gc.getMemoryPoolNames();
                for(int x = 0; x < pools.length; x++) {
                    layer.g().drawString(
                            String.format("%s", pools[x]),
                            (int) (width - xOffset * 0.9),
                            yOffset * ((xStep + 2) + i + x)
                    );
                }

                gcTotalSize += pools.length;
                i += pools.length;
                beanIndex++;

            }

            xStep += gcTotalSize;
            xStep++;

        }

        //--- THREAD_PROFILE
        if(displays.contains(Settings.DebugDisplay.GC_PROFILE)) {
            Map<ThreadGroup, List<Thread>> threads = profilerLoop.getThreads();
            layer.g().drawString(
                    String.format(
                        "Threads: %d",
                            threads.size()
                    ),
                    width - xOffset,
                    yOffset * xStep
            );

            final int[] i = {0};
            int finalXStep = xStep;
            threads.forEach((threadGroup, threadList) -> {

                //--- Group Name
                layer.g().drawString(
                        String.format(
                            "%s",
                                threadGroup.getName()
                        ),
                        (int) (width - xOffset * 0.95),
                        yOffset * ((finalXStep + 1) + i[0]))
                ;
                i[0]++;

                //--- Threads belonging to the group
                threadList.forEach(t -> {
                    layer.g().drawString(
                            String.format(
                                "%d - P: %d - %s (%s)",
                                    t.getId(),
                                    t.getPriority(),
                                    t.getName(),
                                    t.getState().name()
                            ),
                            (int) (width - xOffset * 0.9),
                            yOffset * ((finalXStep + 1) + i[0])
                    );
                    i[0]++;
                });

            });

            xStep += i[0];
            xStep++;
        }
    }

    /**
     * <p>
     *     Returns a color with the highest possible contrast compared to the input color.
     * </p>
     *
     * @param input The input color.
     * @return The complementary color.
     */
    private static Color highContrast(Color input) {

        //--- RGB to HSB (Hue, Saturation, Brightness)
        float[] hsb = new float[3];
        Color.RGBtoHSB(input.getRed(), input.getGreen(), input.getBlue(), hsb);

        float hue = hsb[0];
        float saturation = hsb[1];
        float brightness = hsb[2];

        //--- If we have a color with low saturation -> not colorful
        if(saturation < 0.45) {
            return (brightness < 0.5 ? Color.WHITE : Color.BLACK);
        }

        //--- If we have a color with high saturation -> colorful
        // then we get the complementary
        return new Color(Color.HSBtoRGB((hue * 360 + 180) % 360 / 360, saturation, brightness));
    }


}
