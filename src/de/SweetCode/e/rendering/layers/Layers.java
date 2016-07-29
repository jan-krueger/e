package de.SweetCode.e.rendering.layers;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Layers {

    private final List<Layer> layers;

    public Layers() {
        this.layers =  new LinkedList<>();
    }

    public Layer first() {
        return this.layers.get(0);
    }

    public Layer last() {
        return this.layers.get(this.layers.size() - 1);
    }

    public Layer get(int index) {
        return this.layers.get(index);
    }

    public void add(Layer layer) {
        Assert.assertNotNull(layer);

        this.layers.add(layer);
    }

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
