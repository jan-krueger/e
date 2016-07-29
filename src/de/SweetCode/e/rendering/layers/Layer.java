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

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public Graphics2D getGraphics2D() {
        return this.graphics2D;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        Assert.assertTrue("The alpha value must be => 0 and <= 1.", (alpha >= 0 && alpha <= 1));

        this.alpha = alpha;
    }
}
