package de.SweetCode.e;

import de.SweetCode.e.log.LogEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.concurrent.TimeUnit;

public class EScreen extends JFrame {

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

    private BufferStrategy bufferStrategy;
    private GameScene current = null;

    private VolatileImage volatileImage = null;
    private static GraphicsConfiguration graphicConfiguration;

    public EScreen() {

        Settings settings = E.getE().getSettings();
        this.setTitle(settings.getName());
        this.setUndecorated(!settings.isDecorated());
        this.setResizable(settings.isResizable());
        this.setPreferredSize(new Dimension(settings.getWidth(), settings.getHeight()));
        this.setMinimumSize(new Dimension(settings.getWidth(), settings.getHeight()));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        EScreen.graphicConfiguration = this.getGraphicsConfiguration();

        if(!(EScreen.USE_VRAM)) {

            this.createBufferStrategy(2);
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
            this.current.render(E.getE().getLayers());

            E.getE().getGameComponents().forEach(k -> {
                GameComponent e = k.getGameComponent();
                if(e instanceof Renderable && e.isActive()) {
                    ((Renderable) e).render(E.getE().getLayers());
                }

            });

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

            BufferedImage frame = E.getE().getLayers().combine();
            g.drawImage(frame, x, y, null);

            if(E.getE().getSettings().isDebugging()) {
                g.setColor(Color.MAGENTA);
                g.drawString(String.format("FPS: %s (in %d %s)",  E.getE().getCurrentFPS(), s.getDeltaUnit().convert(E.getE().getCurrentDelta(), TimeUnit.NANOSECONDS), s.getDeltaUnit().name()), frame.getWidth() - 200, 10);
            }

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

}
