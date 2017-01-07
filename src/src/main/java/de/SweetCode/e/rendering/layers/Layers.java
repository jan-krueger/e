package de.SweetCode.e.rendering.layers;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Managing and combining all layers.
 */
public class Layers {

    private final List<Layer> layers = new LinkedList<>();

    /**
     * Constructor.
     * @param amount The amount of layers that the constructor sould generate.
     */
    public Layers(int amount) {
        IntStream.range(0, amount).forEach(i -> this.layers.add(new Layer()));
    }

    /**
     * Gives a reference to the list of all layers stored by the object.
     * @return Returns a {@link LinkedList} list of {@link Layer} references.
     */
    public List<Layer> getLayers() {
        return this.layers;
    }

    /**
     * Returns the first layer.
     *
     * @return Returns a {@link Layer}.
     */
    public Layer first() {
        return this.layers.get(0);
    }

    /**
     * Returns the last layer.
     * @return Returns a {@link Layer}.
     */
    public Layer last() {
        return this.layers.get(this.layers.size() - 1);
    }

    /**
     * Returns the layer by a specified index.
     *
     * @param index The index of the layer to grab.
     * @return Returns a {@link Layer}.
     */
    public Layer get(int index) {
        Assert.assertTrue("index is not a valid layer index.", (index >= 0 && index < this.layers.size()));

        return this.layers.get(index);
    }

    /**
     * Adds a new layer.
     *
     * @param layer A reference to the layer to store.
     */
    public void add(Layer layer) {
        Assert.assertNotNull("The layer cannot be null.", layer);

        this.layers.add(layer);
    }

    /**
     * Combines all layers to a single {@link BufferedImage} used in the final render process. The BufferdImage is
     * using the ARGB color space.
     *
     * @return Returns a {@link BufferedImage}.
     */
    public BufferedImage combine() {

        BufferedImage img = new BufferedImage(E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        g2.setRenderingHints(E.getE().getSettings().getRenderingHints());

        if(this.last().getAlpha() == 1) {
            g2.drawImage(this.last().getBufferedImage(), 0, 0, null);
        } else {
            this.layers.forEach(l -> {

                // If the Alpha-Value is equals to 0 we don't have to
                // setScene the layer at all.
                if (l.getAlpha() > 0) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, l.getAlpha()));
                    g2.drawImage(l.getBufferedImage(), 0, 0, null);
                }

            });
        }

        g2.dispose();

        return img;

    }
}
