package de.SweetCode.e.entity;

import de.SweetCode.e.GameComponent;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.utils.ToStringBuilder;

public abstract class Entity implements GameComponent {

    private BoundingBox boundingBox;

    public Entity(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Location getLocation() {
        return this.boundingBox.getCenter();
    }

    @Override
    public void update(InputEntry input, long delta) {
    }

    @Override
    public abstract boolean isActive();

    public String toString() {
        return ToStringBuilder.create(this).append(this).build();
    }

}
