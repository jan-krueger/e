package de.SweetCode.e.resources.textures;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.log.LogEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.utils.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticTextureLoader implements TextureLoader {

    private final File file;
    private BufferedImage bufferedImage;

    private IBoundingBox boundingBox;
    private final int tileWidth;
    private final int tileHeight;

    private int columns;

    /**
     * Constructor.
     * @param file The sprite sheet file.
     * @param tileWidth The width of one tile.
     * @param tileHeight The height of one tile.
     */
    public StaticTextureLoader(File file, int tileWidth, int tileHeight) {

        Assert.assertNotNull("The file cannot be null.", file);
        Assert.assertTrue("tileWidth cannot be less than 1.", tileWidth > 0);
        Assert.assertTrue("tileHeight cannot be less than 1.", tileHeight > 0);

        this.file = file;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

    }

    @Override
    public void load() {

        try {

            this.bufferedImage = ImageIO.read(this.file);

            this.boundingBox = new IBoundingBox(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            this.columns = bufferedImage.getWidth() / this.tileWidth;

        } catch (IOException e) {
            E.getE().getLog().log(
                LogEntry.Builder.create()
                    .message(String.format("DynamicTextureLoader failed to load the image (%s).", this.file.getAbsolutePath()))
                .build()
            );
            e.printStackTrace();
        }

    }

    @Override
    public Image get(int index) {

        // bounding check :)
        int x = (index % this.columns) * this.tileWidth;
        int y = (index / this.columns) * this.tileHeight;

        if(!(this.boundingBox.contains(new ILocation(x + this.tileWidth, y + this.tileHeight)))) {
            throw new IndexOutOfBoundsException(String.format("%d out of texture.", index));
        }

        return this.bufferedImage.getSubimage(x, y, this.tileWidth, this.tileHeight);

    }

    @Override
    public void update(InputEntry input, long delta) {}

    @Override
    public boolean isActive() {
        return true;
    }
}
