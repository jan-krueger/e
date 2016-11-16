package de.SweetCode.e.rendering.layers;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * This class represents one rendering layer. The engine
 * allows you to have multiple layers at once will be combined
 * and then rendered on the screen.
 */
public class Layer {

    private float alpha = 1F;

    private BufferedImage bufferedImage = new BufferedImage(E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight(), BufferedImage.TYPE_INT_ARGB);

    private Graphics2D graphics2D = this.bufferedImage.createGraphics();

    public Layer() {
        this.graphics2D.setRenderingHints(E.getE().getSettings().getRenderingHints());
    }

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
        this.graphics2D.setClip(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());
        this.graphics2D.setTransform(new AffineTransform());
        this.graphics2D.clearRect(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());
        this.graphics2D.setRenderingHints(E.getE().getSettings().getRenderingHints());
    }
}
