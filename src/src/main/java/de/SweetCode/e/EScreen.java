package de.SweetCode.e;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import de.SweetCode.e.loop.ProfilerLoop;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;
import de.SweetCode.e.utils.log.LogEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;
import java.lang.management.GarbageCollectorMXBean;
import java.nio.IntBuffer;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public static final boolean USE_VRAM = true;

    /**
     * @TODO:
     * Experimental Feature: using OpenGL to render the frame.
     */
    public static final boolean USE_JOGL = false;

    private BufferStrategy bufferStrategy;
    private GameScene current = null;

    private VolatileImage volatileImage = null;
    private static GraphicsConfiguration graphicConfiguration;

    // OpenGL
    private GLProfile glProfile = null;

    public EScreen() {

        Settings settings = E.getE().getSettings();
        this.setTitle(settings.getName());
        this.setUndecorated(!settings.isDecorated());
        this.setResizable(settings.isResizable());
        this.setPreferredSize(new Dimension(settings.getWidth(), settings.getHeight()));
        this.setMinimumSize(new Dimension(settings.getWidth(), settings.getHeight()));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EScreen.graphicConfiguration = this.getGraphicsConfiguration();

        if(USE_JOGL) {

            this.glProfile = GLProfile.get(GLProfile.GL2);
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            glCapabilities.setDoubleBuffered(false);

            GLCanvas canvas = new GLCanvas(glCapabilities);
            canvas.addGLEventListener(this);
            canvas.setSize(400, 400);

            FPSAnimator animator = new FPSAnimator(canvas, 200);
            animator.start();

            this.add(canvas);

        } else {
            if (!(EScreen.USE_VRAM)) {

                this.createBufferStrategy(1);
                this.bufferStrategy = this.getBufferStrategy();

                if (this.bufferStrategy == null) {
                    E.getE().getLog().log(LogEntry.Builder.create().message("Failed to create BufferStrategy.").build());
                }

            } else {
                E.getE().getLog().log(
                        LogEntry.Builder.create()
                                .message("Using images stored in VRAM to render the frames.")
                                .build()
                );
            }
        }

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public GameScene getCurrent() {
        return this.current;
    }

    public void setScene(GameScene gameScene) {
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

        //@TODO Work on camera.
        //Camera camera = E.getE().getCamera();

        do {

            Graphics2D g;

            if(USE_VRAM) {
                if(
                    this.volatileImage == null ||
                    (
                        this.volatileImage != null &&
                        this.volatileImage.validate(super.getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE
                    )
                ) {
                    this.volatileImage = super.createVolatileImage(s.getWidth(), s.getHeight());
                }

                g = this.volatileImage.createGraphics();
            } else {
                g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
            }


            g.setRenderingHints(E.getE().getSettings().getRenderingHints());

            int x = 0;
            int y = 0;
            /**
             @TODO
             if(E.getE().getSettings().fixAspectRatio()) {
             AspectRatio aspectRatio = new AspectRatio(new Dimension(1280, 720), new Dimension(this.getWidth(), this.getHeight()));
             BoundingBox optimal = aspectRatio.getOptimal();

             x = (int) optimal.getMin().getX();
             y = (int) optimal.getMin().getY();
             }**/

            BufferedImage frame = this.frame();

            g.drawImage(frame, x, y, null);

            if(EScreen.USE_VRAM) {
                graphics.drawImage(this.volatileImage, 0, 0, null);
            } else {
                this.bufferStrategy.show();
            }

            g.dispose();

            E.getE().getLayers().getLayers().forEach(Layer::clean);

        } while(USE_VRAM ? this.volatileImage.contentsLost() : this.bufferStrategy.contentsLost());

    }

    public static GraphicsConfiguration getGraphicConfiguration() {
        return graphicConfiguration;
    }

    private BufferedImage frame() {

        this.current.render(E.getE().getLayers());

        E.getE().getGameComponents().forEach(k -> {
            GameComponent e = k.getGameComponent();
            if(e instanceof Renderable && e.isActive()) {
                ((Renderable) e).render(E.getE().getLayers());
            }

        });

        //--- Debugging
        if(E.getE().getSettings().isDebugging()) {
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

            // getting the new frame
            BufferedImage frame = this.frame();

            // Frame to Buffer
            IntBuffer buffer = IntBuffer.allocate(frame.getWidth() * frame.getHeight() * 4);
            buffer.put(((DataBufferInt) frame.getRaster().getDataBuffer()).getData());
            buffer.flip();

            GL2 gl = drawable.getGL().getGL2();

            // clear
            gl.glClearColor(0F, 0F, 0F, 0F);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();

            // generating & binding texture
            TextureData textureData = new TextureData(this.glProfile, GL.GL_RGBA, frame.getWidth(), frame.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, false, false, false, buffer, null);
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
        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}

    /**
     * This method draws to the first layer all requested debug information.
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

        layer.g().setColor(
                EScreen.highContrast(
                        new Color(layer.b().getRGB(settings.getWidth() - xOffset / 2, (int) (yOffset * 1.5D)))
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
                    settings.getWidth() - xOffset,
                    yOffset * xStep
            );

            xStep++;
        }

        if(displays.contains(Settings.DebugDisplay.LOOP_PROFILE)) {
            layer.g().drawString(
                    String.format(
                        "FPS: %d (%d) | Ticks: %d (%d)",
                            E.getE().getCurrentFPS(),
                            settings.getTargetFPS(),
                            E.getE().getCurrentTicks(),
                            settings.getTargetTicks()
                    ),
                    settings.getWidth() - xOffset,
                    yOffset * xStep
            );

            xStep++;
        }

        //--- MEMORY_PROFILE
        if(displays.contains(Settings.DebugDisplay.MEMORY_PROFILE)) {
            layer.g().drawString("Memory Usage JVM & Heap", settings.getWidth() - xOffset, yOffset * xStep);
            layer.g().drawString(
                    String.format(
                        "JVM - Used: %.2fMB",
                            profilerLoop.getAverageJvmMemoryUsed() * E.C.BYTES_TO_MEGABYTES
                    ),
                    (int) (settings.getWidth() - xOffset * 0.95),
                    yOffset * (xStep + 1)
            );
            layer.g().drawString(
                    String.format(
                        "Heap - Max: %.2fMB | Used: %.2fMB",
                            profilerLoop.getMaxHeapSize() * E.C.BYTES_TO_MEGABYTES,
                            profilerLoop.getAverageHeapMemoryUsed() * E.C.BYTES_TO_MEGABYTES
                    ),
                    (int) (settings.getWidth() - xOffset * 0.95),
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
                    settings.getWidth() - xOffset,
                    yOffset * xStep
            );

            int gcTotalSize = gcBeans.size();
            for (int i = 0; i < gcTotalSize; i++) {

                GarbageCollectorMXBean gc = gcBeans.get(0);
                layer.g().drawString(
                        String.format(
                            "%s, %d (%dms)",
                                gc.getName(),
                                gc.getCollectionCount(),
                                gc.getCollectionTime()
                        ),
                        (int) (settings.getWidth() - xOffset * 0.95),
                        yOffset * ((xStep + 1) + i)
                );

                //--- Pools
                String[] pools = gc.getMemoryPoolNames();
                for(int x = 0; x < pools.length; x++) {
                    layer.g().drawString(
                            String.format("%s", pools[x]),
                            (int) (settings.getWidth() - xOffset * 0.9),
                            yOffset * ((xStep + 2) + i + x)
                    );
                }

                gcTotalSize += pools.length;
                i += pools.length;

            }

            xStep += gcTotalSize;
            xStep++;

        }

        //--- THREAD_PROFILE
        if(displays.contains(Settings.DebugDisplay.GC_PROFILE)) {
            Map<String, List<Thread>> threads = profilerLoop.getThreads();
            layer.g().drawString(
                    String.format(
                        "Threads: %d",
                            threads.size()
                    ),
                    settings.getWidth() - xOffset,
                    yOffset * xStep
            );

            final int[] i = {0};
            int finalXStep = xStep;
            threads.forEach((groupName, threadList) -> {

                //--- Group Name
                layer.g().drawString(
                        groupName,
                        (int) (settings.getWidth() - xOffset * 0.95),
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
                            (int) (settings.getWidth() - xOffset * 0.9),
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
     * Returns a color with the highest possible contrast compared to the input color.
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
        if(saturation < 0.3) {
            return (brightness < 0.5 ? Color.WHITE : Color.BLACK);
        }

        //--- If we have a color with high saturation -> colorful
        // then we get the complementary
        return new Color(Color.HSBtoRGB((hue * 360 + 180) % 360 / 360, saturation, brightness));
    }


}
