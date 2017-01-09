package de.SweetCode.e.rendering;

import de.SweetCode.e.math.IDimension;
import de.SweetCode.e.math.ILocation;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * AspectRatio is a class which is responsible for fixing the aspect ratio, if the dimension of the frame don't fit onto
 * the window it is supposed to be rendered on. - The class has also a small cache keeping the last 10 calculations in it
 * making it faster.
 * </p>
 */
public class AspectRatio {

    private final static int CACHE_SIZE = 10;

    private final static Map<Integer, Result> cache = new LinkedHashMap<Integer, Result>(CACHE_SIZE) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Result> eldest) {
            return this.size() > CACHE_SIZE;
        }

    };

    private AspectRatio() {}

    /**
     * <pre>
     *     The method generates the optimal with and height as well as the new x and y coordinates in the window while
     *     keeping the aspect ratio of the original frame. Scaling can still cause bad image quality, if forced to scale
     *     up.
     * </pre>
     * @param image The dimensions of the image aka. frame.
     * @param window The dimensions of the window.
     * @return A {@link AspectRatio.Result} containing the information where the image should be positioned ({@link Result#getPosition()})
     *         and the new size of the image ({@link Result#getDimension()}).
     */
    public static Result calculateOptimal(IDimension image, IDimension window) {

        //--- Key for the cache
        int key = 59 * (31 + image.hashCode()) * (31 + window.hashCode());

        if(AspectRatio.cache.containsKey(key)) {
            return AspectRatio.cache.get(key);
        }

        Dimension d;

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
                new IDimension(
                        (int) Math.floor(fixedWidth),
                        (int) Math.floor(fixedHeight)
                )
        );

        AspectRatio.cache.put(key, result);
        return result;
    }

    /**
     * <p>
     * A wrapper class for the results of the calculations.
     *</p>
     */
    public static class Result {

        private ILocation position;
        private IDimension dimension;

        /**
         * <p>
         *    Creates a new Result.
         * </p>
         *
         * @param position The upper-left position of the frame in the window.
         * @param dimension The new width and height of the frame in the window.
         */
        private Result(ILocation position, IDimension dimension) {
            this.position = position;
            this.dimension = dimension;
        }

        /**
         * <p>
         *    Gives the Location of the upper-left position where the frame should be rendered.
         * </p>
         *
         * @return The Location of the frame in the window.
         */
        public ILocation getPosition() {
            return this.position;
        }

        /**
         * <p>
         *    Gives the dimension of the frame in the window. The width as well as the height.
         * </p>
         *
         * @return The dimension of the frame.
         */
        public IDimension getDimension() {
            return this.dimension;
        }

    }

}
