package de.SweetCode.e.rendering;

import de.SweetCode.e.math.ILocation;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The AspectRatio is a simple calculation class that is work in progress.
 * It is supposed to take in to Dimension. The Dimension of the screen and the Dimension
 * of the game window. Based on these two values the class can calculate the best AspectRatio
 * for the game window.
 */
public class AspectRatio {

    private final static int CACHE_SIZE = 10;

    private final static Map<Integer, Result> cache = new LinkedHashMap<Integer, Result>(CACHE_SIZE) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Result> eldest) {
            return this.size() > CACHE_SIZE;
        }

    };

    /**
     * The method generates the optimal with and height as well as the new x and y coordinates in the window
     * while keeping the aspect ratio of the original frame.
     *
     * @param image The dimensions of the image aka. frame.
     * @param window The dimensions of the window.
     * @return A {@link AspectRatio.Result} containing the information where the image should be positioned ({@link Result#getPosition()})
     *         and the new size of the image ({@link Result#getDimension()}).
     */
    public static Result calculateOptimal(Dimension image, Dimension window) {

        //--- Key for the cache
        int key = 59 * (31 + image.hashCode()) * (31 + window.hashCode());

        if(AspectRatio.cache.containsKey(key)) {
            return AspectRatio.cache.get(key);
        }

        //--- Some basic math to scale the image correctly
        double imageRatio = image.getWidth() / image.getHeight();
        double screenRation = window.getWidth() / window.getHeight();

        double fixedWidth;
        double fixedHeight;

        if(screenRation > imageRatio) {
            fixedWidth = image.getWidth() * window.getHeight() / image.getHeight();
            fixedHeight = window.getHeight();
        } else {
            fixedWidth = window.getWidth();
            fixedHeight = image.getHeight() * window.getWidth() / image.getWidth();
        }

        Result result = new Result(
                new ILocation(
                    (int) Math.floor((window.getWidth() - fixedWidth) / 2),
                    (int) Math.floor((window.getHeight() - fixedHeight) / 2)
                ),
                new Dimension(
                        (int) Math.floor(fixedWidth),
                        (int) Math.floor(fixedHeight)
                )
        );

        AspectRatio.cache.put(key, result);
        return result;
    }

    public static class Result {

        private ILocation position;
        private Dimension dimension;

        private Result(ILocation position, Dimension dimension) {
            this.position = position;
            this.dimension = dimension;
        }

        public ILocation getPosition() {
            return this.position;
        }

        public Dimension getDimension() {
            return this.dimension;
        }

    }

}
