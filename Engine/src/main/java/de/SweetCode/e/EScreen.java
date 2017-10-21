package de.SweetCode.e;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
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
import de.SweetCode.e.utils.log.LogPrefixes;

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

    //---DEBUG
    private final static int DEBUG_FONT_SIZE = 12;
    private static boolean debugFirstRun = true;
    private static int debugXOffset = -1;

    private boolean displayDebuggingInformation = true;


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
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        this.pack();
        this.setLocationRelativeTo(null);

        EScreen.graphicConfiguration = this.getGraphicsConfiguration();

        if(!(settings.useOpenGL())) {
            this.setVisible(true);

            this.createBufferStrategy(3);
            this.bufferStrategy = this.getBufferStrategy();

            if (this.bufferStrategy == null) {
                E.getE().getLog().log(
                        LogEntry.Builder.create(EScreen.class)
                            .prefix(LogPrefixes.ENGINE)
                            .message("Failed to create BufferStrategy.")
                        .build()
                );
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

    public boolean isDisplayDebuggingInformation() {
        return this.displayDebuggingInformation;
    }

    public void setDisplayDebuggingInformation(boolean enabled) {
        this.displayDebuggingInformation = enabled;
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

        Settings s = E.getE().getSettings();

        if(s.useOpenGL()) {
            super.paint(graphics);
            return;
        }

        if (this.current == null) {
            return;
        }

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
                    frame = frame.getScaledInstance(dimension.getWidth(), dimension.getHeight(), Image.SCALE_AREA_AVERAGING);
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

    /**     * <p>
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
        if(E.getE().getSettings().isDebugging() && !(E.getE().getSettings().getDebugInformation().isEmpty()) && this.displayDebuggingInformation) {
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
            int[] data = ((DataBufferInt)frame.getRaster().getDataBuffer()).getData();

            IntBuffer buffer = IntBuffer.allocate(data.length);

            // Note: Well, in a normal case we just could have called IntBuffer#put and passed the as parameter the data
            // int-array, however the RGB values in it have a wrong order, so we have to fix them. This is what we are doing
            // below. The values stored in the image-object are in the following order: ARGB, however the OpenGL pipeline
            // expects ABGR for some reason.
            // Since bit-shifting is not a expensive operation it usually takes about 1ms (of course depending on the system,
            // but still acceptable). I tried to implement a cache but even if the look-up time is O(1) it is too slow
            // to make any sense.
            Arrays.stream(data).forEachOrdered(e -> {

                int alpha = (e >> 24) & 0xFF;
                int red = (e >> 16) & 0xFF;
                int green = (e >> 8) & 0xFF;
                int blue = (e & 0xFF) & 0xFF;


                buffer.put(
                        ((alpha & 0xFF) << 24)  |
                        ((blue & 0xFF)  << 16)  |
                        ((green & 0xFF) << 8)   |
                        ((red & 0xFF)   << 0)
                );

            });

            buffer.flip();

            GL2 gl = drawable.getGL().getGL2();

            // disables sync-to-vertical-refresh
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
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {}

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

        float defaultHeight = layer.g().getFont().getSize2D();
        float fontHeight = Math.max(7, DEBUG_FONT_SIZE * (settings.getFrameDimension().getHeight() / 720F));
        layer.g().setFont(layer.g().getFont().deriveFont(fontHeight));

        //--- Offsets
        if(EScreen.debugFirstRun) {
            EScreen.debugXOffset = (int) (layer.g().getFontMetrics().stringWidth(
                    String.format(
                            "FPS: %d (%d) | Ticks: %d (%d) | Objects: %d (%.4f%%)",
                            E.getE().getCurrentFPS(),
                            settings.getTargetFPS(),
                            E.getE().getCurrentTicks(),
                            settings.getTargetTicks(),
                            E.getE().getGameComponents().size(),
                            ((double) profilerLoop.getActiveObjects() / E.getE().getGameComponents().size()) * 100D
                    )
            ) * 1.28D);
            EScreen.debugFirstRun = false;
        }

        int xOffset = EScreen.debugXOffset;
        int yOffset = (int) (0.02D * settings.getFrameDimension().getHeight());

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
                            (settings.useOpenGL() ? "on" : "off"),
                            (E.getE().getSettings().isParallelizingUpdate() ? String.format("parallelized (%d)", E.POOL_SIZE) : "sequential")
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

        if(displays.contains(Settings.DebugDisplay.EVENT_PROFILE)) {
            layer.g().drawString(String.format("Event Queue: %d", E.getE().getEventHandler().getQueuedEvents().size()), width - xOffset, yOffset * xStep);
            xStep += 1;
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

            //--- Main Thread
            final double[] waiting = {0D};
            threads
                .entrySet().stream()
                .filter(e -> e.getKey().getName().equals("main"))
                .findFirst().get()
                .getValue().forEach(thread -> {
                    if(thread.getState() == Thread.State.WAITING) {
                        waiting[0]++;
                    }
                });

            if(waiting[0] / E.POOL_SIZE > 0.5D) {
                //layer.g().drawString("WARNING Decrease the pool size.");
            }

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
                    Color tmp = layer.g().getColor();
                    if(t.getName().startsWith("e-loop-thread-")) {
                        double usage = waiting[0] / E.POOL_SIZE;
                        if(usage < 0.31) {
                            layer.g().setColor(Color.GREEN);
                        } else if(usage > 0.3 && usage < 0.51) {
                            layer.g().setColor(Color.ORANGE);
                        } else {
                            layer.g().setColor(Color.RED);
                        }
                    }

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

                    layer.g().setColor(tmp);
                    i[0]++;
                });

            });

            xStep += i[0];
            xStep++;
        }

        layer.g().setFont(layer.g().getFont().deriveFont(defaultHeight));

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
