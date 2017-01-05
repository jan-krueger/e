package de.SweetCode.e;

import de.SweetCode.e.rendering.Priority;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.Comparator;

/**
 * A GameComponentEntry just makes storing a game-component more handy.
 */
public class GameComponentEntry {

    private GameComponent gameComponent;
    private Priority priority;

    /**
     * @param gameComponent The game-component.
     * @param priority The priority in the update-loop
     */
    public GameComponentEntry(GameComponent gameComponent, Priority priority) {
        this.gameComponent = gameComponent;
        this.priority = priority;
    }

    /**
     * Gives the game-component;
     * @return
     */
    public GameComponent getGameComponent() {
        return this.gameComponent;
    }

    /**
     * Gives the priority of the game-component in the update-loop.
     * @return
     */
    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("gameComponent", this.gameComponent)
                .append("priority", this.priority)
                .build();
    }

    public static class EntryComparator implements Comparator<GameComponentEntry> {

        @Override
        public int compare(GameComponentEntry o1, GameComponentEntry o2) {

            if(o1.getPriority().getPriority() == o2.getPriority().getPriority()) {
                return 0;
            }

            return (o1.getPriority().getPriority() < o2.getPriority().getPriority() ? 1 : -1);

        }

    }

}
