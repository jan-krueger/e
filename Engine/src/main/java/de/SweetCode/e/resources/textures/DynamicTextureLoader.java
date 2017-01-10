package de.SweetCode.e.resources.textures;

import de.SweetCode.e.E;
import de.SweetCode.e.EScreen;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.log.LogEntry;
import me.lemire.integercompression.differential.IntegratedIntCompressor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The DynamicTextureLoader keeps only actively used textures uncompressed (!) in memory as BufferedImage, all other
 * parts of the sprite sheet will be stored as compressed (!) image, if the program requests a image that is not in-cache
 * the TextureLoader will uncompress the requested part and create a new BufferedImage.
 *
 * The DynamicTextureLoader is useful if you are dealing with large sprite sheets, but you are only using parts of it
 * while rendering, if you are using huge parts of the sprite sheet at the same time, than you should use the
 * StaticTextureLoader.
 */
public class DynamicTextureLoader implements TextureLoader {

    private final File file;
    private IntegratedIntCompressor compressor = new IntegratedIntCompressor();

    private Map<Integer, ImageCacheEntry> cache = new WeakHashMap<>();

    private IBoundingBox boundingBox;
    private final int tileWidth;
    private final int tileHeight;

    private int rows;
    private int columns;

    private long lastRun = System.currentTimeMillis();
    private final long cacheTime;

    private int[] compressedImage = null;

    /**
     * Constructor.
     * @param file The sprite sheet file.
     * @param tileWidth The width of one tile.
     * @param tileHeight The height of one tile.
     * @param cacheTime The time in milliseconds to stay in cache.
     */
    public DynamicTextureLoader(File file, int tileWidth, int tileHeight, int cacheTime) {

        Assert.assertNotNull("The file cannot be null.", file);
        Assert.assertTrue("The file does not exist.", file.exists());
        Assert.assertTrue("tileWidth cannot be less than 1.", tileWidth > 0);
        Assert.assertTrue("tileHeight cannot be less than 1.", tileHeight > 0);
        Assert.assertTrue("cacheTime cannot be less than 1.", cacheTime > 0);

        this.file = file;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.cacheTime = cacheTime;

    }

    @Override
    public void load() {

        try {
            BufferedImage bufferedImage = ImageIO.read(this.file);

            // compress image
            // @TODO :) - The results are currently ok-ish, I get around 15% compression.

            int[] data = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
            this.compressedImage = compressor.compress(data);

            E.getE().getLog().log(
                LogEntry.Builder.create()
                    .message(
                        String.format(
                            "DynamicTextureLoader compressed the image (%s) and could save %.2f%% in-memory space.",
                            this.file.getAbsolutePath(),
                            ((1 - this.compressedImage.length / (double) data.length) * 100)
                        )
                    )
                .build()
            );

            // load all images into the cache
            this.boundingBox = new IBoundingBox(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

            this.rows = bufferedImage.getHeight() / this.tileHeight;
            this.columns = bufferedImage.getWidth() / this.tileWidth;

            int index = 0;
            for(int y = 0; y < this.rows; y++) {
                for(int x = 0; x < this.columns; x++) {
                    this.cache.put(
                        index,
                        new ImageCacheEntry(
                            System.currentTimeMillis() + this.cacheTime,
                            bufferedImage.getSubimage(x * this.tileWidth, y * this.tileHeight, this.tileWidth, this.tileHeight)
                        )
                    );
                    index++;
                }
            }


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

        ImageCacheEntry entry = null;

        // is the index currently in cache?
        if(this.cache.containsKey(index)) {

            entry = this.cache.get(index);

        }
        // Image currently not in cache
        else {

            int[] uncompressed = compressor.uncompress(this.compressedImage);

            BufferedImage bufferedImage = new BufferedImage(this.tileWidth, this.tileHeight, BufferedImage.TYPE_INT_ARGB);
            /**
             * The array offset. Explained.
             * i = offset + (y-startY)*scansize + (x-startX)
             *
             * offset   = 0, because we had no offset in our original RGB array.
             * y        = y, the y coordinate we calculated from the index.
             * startY   = 0, because the new BufferdImage has the same size as the array, no startY-offset required.
             * scansize = with of the original spritesheet, because this is the scansize used in the original RGB array.
             * x        = x, the x coordinate we calculated from the index.
             * startX   = 0, because the new BufferdImage has the same size as the array, no startX-offset required.
             */
            bufferedImage.setRGB(0, 0, this.tileWidth, this.tileHeight, uncompressed, (y * this.boundingBox.getWidth() + x), this.boundingBox.getWidth());

            entry = new ImageCacheEntry(
                    System.currentTimeMillis() + this.cacheTime,
                    bufferedImage
            );

            this.cache.put(index, entry);

        }

        entry.accessed(this.cacheTime);
        return entry.getImage();
    }

    @Override
    public void update(InputEntry input, long delta) {

        if(System.currentTimeMillis() >= this.lastRun + this.cacheTime) {

            Iterator<ImageCacheEntry> iterator = this.cache.values().iterator();
            iterator.forEachRemaining(e -> {

                if(e.hasExpired()) {
                    iterator.remove();
                }

            });

            this.lastRun = System.currentTimeMillis();

        }

    }

    @Override
    public boolean isActive() {
        return true;
    }

    private static class ImageCacheEntry {

        private long expired;
        private Image image;

        public ImageCacheEntry(long expired, BufferedImage image) {
            this.expired = expired;

            // Storing images in VRAM if we are supposed to... :) otherwise just plain BufferdImage in normal RAM
            this.image = (EScreen.USE_VRAM ? toVolatile(image) : image);
        }

        public Image getImage() {
            return this.image;
        }

        public boolean hasExpired() {
            return this.expired <= System.currentTimeMillis();
        }

        public void accessed(long expireTime) {
            this.expired = System.currentTimeMillis() + expireTime;
        }

        private static Image toVolatile(BufferedImage bufferedImage) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            VolatileImage volatileImage = EScreen.getGraphicConfiguration().createCompatibleVolatileImage(width, height);
            volatileImage.setAccelerationPriority(1);

            //@TODO: Remove background
            Graphics2D g = volatileImage.createGraphics();

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.setBackground(new Color(255, 255, 255, 0));
            g.fillRect(0, 0, width, height);

            g.setRenderingHints(E.getE().getSettings().getRenderingHints());
            g.drawImage(bufferedImage, 0, 0, null);
            g.dispose();

            return volatileImage;
        }

    }

}