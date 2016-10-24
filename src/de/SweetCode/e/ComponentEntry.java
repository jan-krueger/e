package de.SweetCode.e;

import de.SweetCode.e.rendering.Priority;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class ComponentEntry {

    private GameComponent gameComponent;
    private Priority priority;

    public ComponentEntry(GameComponent gameComponent, Priority priority) {
        this.gameComponent = gameComponent;
        this.priority = priority;
    }

    public GameComponent getGameComponent() {
        return this.gameComponent;
    }

    public Priority getValue() {
        return this.priority;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("gameComponent", this.gameComponent)
                .append("priority", this.priority)
                .build();
    }

}
