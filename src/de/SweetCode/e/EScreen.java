package de.SweetCode.e;

import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.context.Graphics2DScene;

import javax.swing.*;
import java.awt.*;

public class EScreen extends JFrame {

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

    }

    public void render(GameScene gameScene) {

        this.current = gameScene;

        if(this.current instanceof Graphics2DScene) {
            this.invalidate();
            this.repaint();
        }

    }

    @Override
    public void paint(Graphics g)
    {
        if(this.current == null) {
            return;
        }

        if(this.current instanceof Graphics2DScene) {
            ((Graphics2DScene) this.current).render((Graphics2D) g);
        }

        /*g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        g.fillRect(0, 0, 720, 1080);*/
    }

}
