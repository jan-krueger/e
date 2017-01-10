package de.SweetCode.e.entity;

import de.SweetCode.e.GameComponent;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.UUID;

/**
 * <p>
 * An Entity repents and entity in the engine. The purpose of this class is not fully flashed out yet and it may get
 * removed later down the path. <b>A use is not recommended, yet.</b>
 * </p>
 */
public abstract class Entity implements GameComponent {

    private final UUID identifier;
    private BoundingBox boundingBox;

    /**
     * <p>
     *    Creates a new Entity with its unique identifier and bounding box representing its hit box.
     * </p>
     *
     * @param identifier The unique identifier for the Entity.
     * @param boundingBox The bounding box acting as collision box.
     */
    public Entity(UUID identifier, BoundingBox boundingBox) {

        Assert.assertNotNull("The identifier cannot be null.", identifier);
        Assert.assertNotNull("The bounding box cannot be null.", boundingBox);

        this.identifier = identifier;
        this.boundingBox = boundingBox;
    }

    /**
     * <p>
     *    Creates a new Entity but calls {@link UUID#randomUUID()}.
     * </p>
     * @param boundingBox The bounding box acting as collision box.
     */
    public Entity(BoundingBox boundingBox) {
        this(UUID.randomUUID(), boundingBox);
    }

    /**
     * <p>
     *    Gives the unique identifier of the entity.
     * </p>
     *
     * @return Gives the identifier of the entity.
     */
    public UUID getIdentifier() {
        return this.identifier;
    }

    /**
     * <p>
     *    Gives the center location of the entitie's hit box aka. bounding box.
     * </p>
     *
     * @return Gives the center of the entity bounding box as entity location.
     */
    public Location getLocation() {
        return this.boundingBox.getCenter();
    }

    @Override
    public abstract void update(InputEntry input, long delta);

    @Override
    public abstract boolean isActive();

    public String toString() {
        return ToStringBuilder.create(this)
                .append("identifier", this.identifier.toString())
                .append("boundingBox", this.boundingBox.toString())
            .build();
    }

}
