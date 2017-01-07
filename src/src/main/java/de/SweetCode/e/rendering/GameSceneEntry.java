package de.SweetCode.e.rendering;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.Comparator;
import java.util.Map;

/**
 * A simple data holder for game scenes. Used to store scenes more handy.
 */
public class GameSceneEntry {

    private GameScene gameScene;
    private Priority priority;

    /**
     * @param gameScene The game-scene.
     * @param priority The priority the game-scene has in the render-loop.
     */
    public GameSceneEntry(GameScene gameScene, Priority priority) {
        this.gameScene = gameScene;
        this.priority = priority;
    }

    /**
     * @return Returns the wrapped {@link GameScene}.
     */
    public GameScene getGameScene() {
        return this.gameScene;
    }

    /**
     * @return Gives the {@link Priority} of the game-scene in the game-loop.
     */
    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("gameScene", this.gameScene)
                .append("priority", this.priority)
                .build();
    }

    public static class EntryComparator implements Comparator<Map.Entry<Class<? extends GameScene>, GameSceneEntry>> {

        @Override
        public int compare(
                Map.Entry<Class<? extends GameScene>, GameSceneEntry> o1,
                Map.Entry<Class<? extends GameScene>, GameSceneEntry> o2
        ) {

            if(o1.getValue().getPriority().getPriority() == o2.getValue().getPriority().getPriority()) {
                return 0;
            }

            return (o1.getValue().getPriority().getPriority() < o2.getValue().getPriority().getPriority() ? 1 : -1);

        }

    }

}
