package de.SweetCode.e;

import de.SweetCode.e.log.LogEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.rendering.AspectRatio;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class EScreen extends JFrame {

    private final BufferStrategy bufferStrategy;
    private GameScene current = null;
    private GameScene tmp = null;

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

        /*
        //@TODO Fix flickering
        int i = 4;
        for(; i > 1; i--) {
            try {
                this.createBufferStrategy(i);
                break;
            } catch(Exception e) {}
        }*/
        //E.getE().getLog().log(LogEntry.Builder.create().message("Created BufferStrategy %d-buffering strategy.", i).build());

        this.createBufferStrategy(2);
        this.bufferStrategy = this.getBufferStrategy();

        if(!(this.bufferStrategy == null)) {
            E.getE().getLog().log(LogEntry.Builder.create().message("Failed to create BufferStrategy.").build());
        }

    }

    public GameScene getCurrent() {
        return this.current;
    }

    public void setScene(GameScene gameScene) {
        this.tmp = current;
        this.current = gameScene;
        this.invalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics graphics) {

        if (this.current == null) {
            return;
        }

        //@TODO Work on camera.
        //Camera camera = E.getE().getCamera();

        do {
            Graphics2D g;

            g = (Graphics2D) this.bufferStrategy.getDrawGraphics();

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

            if(E.getE().getSettings().fixAspectRatio()) {
                AspectRatio aspectRatio = new AspectRatio(new Dimension(1280, 720), new Dimension(this.getWidth(), this.getHeight()));
                BoundingBox optimal = aspectRatio.getOptimal();

                x = (int) optimal.getMin().getX();
                y = (int) optimal.getMin().getY();
            }

            g.drawImage(E.getE().getLayers().combine(), x, y, null);

            E.getE().getLayers().getLayers().forEach(Layer::clean);

            g.dispose();

            this.bufferStrategy.show();

        } while(this.bufferStrategy.contentsLost());

    }

}
