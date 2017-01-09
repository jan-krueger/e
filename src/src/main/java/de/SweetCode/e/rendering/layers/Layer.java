package de.SweetCode.e.rendering.layers;

import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * <p>
 * This class represents one rendering layer. The engine allows you to have multiple layers at once will be combined and
 * then rendered on the screen.
 * </p>
 */
public class Layer {

    private float alpha = 1F;

    private BufferedImage bufferedImage = new BufferedImage(E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight(), BufferedImage.TYPE_INT_ARGB);

    private Graphics2D graphics2D = this.bufferedImage.createGraphics();

    /**
     * <p>
     *    Creates a new Layer and setting the {@link RenderingHints} provided by {@link Settings#getRenderingHints()}.
     * </p>
     */
    public Layer() {
        this.graphics2D.setRenderingHints(E.getE().getSettings().getRenderingHints());
    }

    /**
     * <p>
     *    Gives the instance of the BufferedImage related to the layer.
     * </p>
     *
     * @return Returns a BufferedImage used by the layer to draw on.
     */
    public BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }


    /**
     * <p>
     *    Gives the instance of the BufferedImage related to the layer.
     * </p>
     *
     * @return Returns the same as {@link #getBufferedImage()}.
     */
    public BufferedImage b() {
        return this.bufferedImage;
    }

    /**
     * <p>
     *    Gets the {@link Graphics2D} which is also related to the {@link BufferedImage} of the layer.
     * </p>
     *
     * @return Returns a Graphics2D object used by the layer to draw on.
     */
    public Graphics2D getGraphics2D() {
        return this.graphics2D;
    }

    /**
     * <p>
     *    Gets the {@link Graphics2D} which is also related to the {@link BufferedImage} of the layer.
     * </p>
     *
     * @return Returns the same as {@link #getGraphics2D()}
     */
    public Graphics2D g() {
        return this.graphics2D;
    }

    /**
     * <p>
     *    Returns the alpha value of the layer. The value is in the interval of [0, 1].
     * </p>
     *
     * @return Returns the alpha value of the layer.
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * <p>
     *     Sets the alpha value of the layer. The value must be in the interval of [0, 1].
     * </p>
     *
     * @param alpha This method sets the alpha value of this layer.
     */
    public void setAlpha(float alpha) {
        Assert.assertTrue("The alpha value must be => 0 and <= 1.", (alpha >= 0 && alpha <= 1));
        this.alpha = alpha;
    }

    /**
     * <p>
     *    Clears the layer by:
     * </p>
     * <ul>
     *    <li>resetting he clip by calling {@link Graphics2D#setClip(int, int, int, int)}</li>
     *    <li>setting the transform to {@link AffineTransform}</li>
     *    <li>clearing the frame by calling {@link Graphics2D#clearRect(int, int, int, int)}</li>
     *    <li>setting the rendering hints provided by {@link Settings#getRenderingHints()}</li>
     * </ul>
     */
    public void clean() {
        this.graphics2D.setClip(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());
        this.graphics2D.setTransform(new AffineTransform());
        this.graphics2D.clearRect(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());
        this.graphics2D.setRenderingHints(E.getE().getSettings().getRenderingHints());
    }
}
