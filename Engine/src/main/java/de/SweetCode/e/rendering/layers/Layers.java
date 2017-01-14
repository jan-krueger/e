package de.SweetCode.e.rendering.layers;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <p>
 * Layers is responsible for keeping all {@link Layer Layers} together and combining them to a frame.
 * </p>
 */
public class Layers {

    private final List<Layer> layers = new LinkedList<>();

    /**
     * <p>
     *     Creates a new Layers.
     * </p>
     *
     * @param amount The amount of layers that the constructor generates and holds, initially.
     */
    public Layers(int amount) {
        IntStream.range(0, amount).forEach(i -> this.layers.add(new Layer()));
    }

    /**
     * <p>
     *    Gives a {@link LinkedList} of all {@link Layer Layers} currently stored in the Layers wrapper.
     * </p>
     *
     * @return Returns a {@link LinkedList} list of {@link Layer} references.
     */
    public List<Layer> getLayers() {
        return this.layers;
    }

    /**
     * <p>
     *    Returns the first layer from the {@link LinkedList} of {@link Layer layer} references. The index of the item is
     *    0.
     * </p>
     *
     * @return Returns a {@link Layer}.
     */
    public Layer first() {
        Assert.assertFalse("The layer list is empty.", this.layers.isEmpty());

        return this.layers.get(0);
    }

    /**
     * <p>
     *    Returns the first layer from the {@link LinkedList} of {@link Layer layer} references. The index of the item is
     *    n - 1. n is the total size of the list storing all {@link Layer layers}. If there is only one element in the list
     *    it will return as {@link Layers#first()}.
     * </p>
     *
     * @return Returns a {@link Layer}.
     */
    public Layer last() {
        Assert.assertFalse("The layer list is empty.", this.layers.isEmpty());

        return this.layers.get(this.layers.size() - 1);
    }

    /**
     * <p>
     *     Returns a layer based on the its index.
     * </p>
     *
     * @param index The index of the layer to grab.
     * @return Returns a {@link Layer}.
     */
    public Layer get(int index) {
        Assert.assertTrue("index is not a valid layer index.", (index >= 0 && index < this.layers.size()));

        return this.layers.get(index);
    }

    /**
     * <p>
     *    Adds a new {@link Layer} to the list.
     * </p>
     *
     * @param layer A reference to the layer to store.
     */
    public void add(Layer layer) {
        Assert.assertNotNull("The layer cannot be null.", layer);
        Assert.assertFalse("The layer already exists.", this.layers.contains(layer));

        this.layers.add(layer);
    }

    /**
     * <p>
     *    Combines all layers to a single {@link BufferedImage} used in the final render process. The BufferedImage is
     *    using the ARGB color space. - If the alpha value of {@link Layers#last()} is equals to one, it will render only
     *    the frame of it, because no others would be visible. The method also avoids images where the alpha value is
     *    equals to 0.
     * </p>
     *
     * @return Returns a {@link BufferedImage}.
     */
    public BufferedImage combine() {

        BufferedImage img = new BufferedImage(
                E.getE().getSettings().getFrameDimension().getWidth(),
                E.getE().getSettings().getFrameDimension().getHeight(),
                BufferedImage.TYPE_INT_ARGB_PRE
        );
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
