package de.SweetCode.e;

import de.SweetCode.e.log.LogEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.utils.ToStringBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class EScreen extends JFrame {

    private final BufferStrategy bufferStrategy;
    private GameScene current = null;

    public EScreen() {

        Settings settings = E.getE().getSettings();
        this.setTitle(settings.getName());
        this.setUndecorated(!settings.isDecorated());
        this.setResizable(settings.isResizable());
        this.setPreferredSize(new Dimension(settings.getWidth(), settings.getHeight()));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        int i = 4;
        for(; i > 1; i--) {
            try {
                this.createBufferStrategy(i);
                break;
            } catch(Exception e) {}
        }
        E.getE().getLog().log(LogEntry.Builder.create().message("Created BufferStrategy %d-buffering strategy.", i).build());

        this.bufferStrategy = this.getBufferStrategy();

        if(!(this.bufferStrategy == null)) {
            E.getE().getLog().log(LogEntry.Builder.create().message("Failed to create BufferStrategy.").build());
        }

    }

    public void render(GameScene gameScene) {

        this.current = gameScene;
        this.invalidate();
        this.repaint();

    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = null;

        if (this.current == null) {
            return;
        }

        do {
            try {

                g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
                g.setRenderingHints(E.getE().getSettings().getRenderingHints());
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, 1920, 1080);

                this.current.render(g);

                Graphics2D finalG = g;
                E.getE().getGameComponents().forEach(e -> {

                    if(e instanceof Renderable && e.isActive()) {
                        ((Renderable) e).render(finalG);
                    }

                });
            } finally {
                if (g != null) {
                    g.dispose();
                }
            }
            bufferStrategy.show();
        } while(bufferStrategy.contentsLost());

    }

}
