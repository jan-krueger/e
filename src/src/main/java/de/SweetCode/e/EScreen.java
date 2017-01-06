package de.SweetCode.e;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;
import de.SweetCode.e.utils.log.LogEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;
import java.lang.management.ManagementFactory;
import java.nio.IntBuffer;
import com.sun.management.OperatingSystemMXBean;

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
    public static final boolean USE_JOGL = false;

    private BufferStrategy bufferStrategy;
    private GameScene current = null;

    private VolatileImage volatileImage = null;
    private static GraphicsConfiguration graphicConfiguration;

    // OpenGL
    private GLProfile glProfile = null;

    // Debug Stuff
    private OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

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

        Settings settings = E.getE().getSettings();

        //--- Debugging
        if(settings.isDebugging()) {
            Layer layer = E.getE().getLayers().first();
            layer.g().setColor(Color.MAGENTA);
            layer.g().drawString(
                    String.format(
                        "FPS: %d (%d) | Ticks: %d (%d)",
                            E.getE().getCurrentFPS(),
                            settings.getTargetFPS(),
                            E.getE().getCurrentTicks(),
                            settings.getTargetTicks()
                    ),
                    settings.getWidth() - 200,
                    10
            );
            layer.g().drawString(
                    String.format(
                        "CPU: %.2f%% | Memory: %.2fMB",
                            this.bean.getProcessCpuLoad() * 100,
                            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * Math.pow(10, -6)
                    ),
                    settings.getWidth() - 200,
                    20
            );

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

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}

}
