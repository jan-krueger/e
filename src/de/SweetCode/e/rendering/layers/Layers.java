package de.SweetCode.e.rendering.layers;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Layers {

    private final List<Layer> layers = new LinkedList<>();

    public Layers() {}

    public List<Layer> getLayers() {
        return this.layers;
    }

    /**
     * Returns the first layer.
     *
     * @return
     */
    public Layer first() {
        return this.layers.get(0);
    }

    /**
     * Returns the last layer.
     * @return
     */
    public Layer last() {
        return this.layers.get(this.layers.size() - 1);
    }

    /**
     * Returns the layer by a specified index.
     *
     * @param index
     * @return
     */
    public Layer get(int index) {
        return this.layers.get(index);
    }

    /**
     * Adds a new layer.
     *
     * @param layer
     */
    public void add(Layer layer) {
        Assert.assertNotNull(layer);

        this.layers.add(layer);
    }

    /**
     * Combines all layers to render them.
     *
     * @return
     */
    public BufferedImage combine() {

        BufferedImage img = new BufferedImage(E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHints(E.getE().getSettings().getRenderingHints());

        this.layers.forEach(l -> {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, l.getAlpha()));
            g2.drawImage(l.getBufferedImage(), 0, 0, null);


        });

        g2.dispose();
        return img;

    }
}
