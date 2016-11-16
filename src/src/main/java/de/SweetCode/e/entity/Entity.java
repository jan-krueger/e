package de.SweetCode.e.entity;

import de.SweetCode.e.GameComponent;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.UUID;

/**
 * An Entity is going to represent the most abstract type of entity in a game.
 */
public abstract class Entity implements GameComponent {

    private final UUID identifier;
    private BoundingBox boundingBox;

    /**
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
     * The constructor generating a UUID.
     * @param boundingBox The bounding box acting as collision box.
     */
    public Entity(BoundingBox boundingBox) {
        this(UUID.randomUUID(), boundingBox);
    }

    /**
     * Returns the identifier of the entity.
     * @return
     */
    public UUID getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns the center of the entity bounding box as entity location.
     * @return
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
