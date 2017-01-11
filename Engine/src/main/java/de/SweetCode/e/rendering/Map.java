package de.SweetCode.e.rendering;

import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.math.Location;

/**
 * <p>
 * A Map represents a map in a game.
 * </p>
 */
public class Map {

    private IBoundingBox boundingBox;

    public Map(IBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * <p>
     *    Transforms the world-location position to the position in the frame. <i>Warning: casting errors possible.</i>
     * </p>
     *
     * @param location The location of the object in the world.
     *
     * @return The location in the frame.
     */
    public ILocation transform(Location location) {
        return new ILocation(
                (int) (location.getX() - this.boundingBox.getMin().getX()),
                (int) (location.getY() - this.boundingBox.getMin().getY())
        );
    }

    /**
     * <p>
     *    Transforms the world-location position to the position in the frame.
     * </p>
     *
     * @param location The location of the object in the world.
     *
     * @return The location in the frame.
     */
    public ILocation transform(ILocation location) {
        return this.transform(new Location(location));
    }

}
