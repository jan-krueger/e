package de.SweetCode.e.rendering.layers;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Layer {

    private float alpha = 1F;
    private BufferedImage bufferedImage = new BufferedImage(E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight(), BufferedImage.TYPE_INT_ARGB);
    private Graphics2D graphics2D = bufferedImage.createGraphics();

    public Layer() {}

    /**
     * Gets the used BufferedImage.
     *
     * @return
     */
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }


    /**
     * Gets the used BufferedImage.
     *
     * @return
     */
    public BufferedImage b() {
        return this.bufferedImage;
    }

    /**
     * Gets the Graphics2D object.
     *
     * @return
     */
    public Graphics2D getGraphics2D() {
        return this.graphics2D;
    }

    /**
     * Gets the Graphics2D object.
     *
     * @return
     */
    public Graphics2D g() {
        return this.graphics2D;
    }

    /**
     * Gets the used alpha value to setScene the layers.
     *
     * @return
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Sets the alpha value.
     *
     * @param alpha
     */
    public void setAlpha(float alpha) {
        Assert.assertTrue("The alpha value must be => 0 and <= 1.", (alpha >= 0 && alpha <= 1));

        this.alpha = alpha;
    }

    /**
     * Cleans the layer.
     */
    public void clean() {
        //this.graphics2D.setColor(Color.WHITE);
        //this.graphics2D.fillRect(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());

        this.graphics2D.setBackground(new Color(255, 255, 255));
        this.graphics2D.clearRect(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());
        //this.graphics2D.setBackground(new Color(255, 255, 255, 0));
        //this.graphics2D.clearRect(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());
    }
}
