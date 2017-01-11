package de.SweetCode.e.rendering;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.Comparator;
import java.util.Map;

/**
 * <p>
 * A GameSceneEntry in {@link de.SweetCode.e.E} to store {@link GameScene GameScenes} more handy.
 * </p>
 */
public class GameSceneEntry {

    private GameScene gameScene;
    private Priority priority;

    /**
     * <p>
     *    A new GameSceneEntry instance.
     * </p>
     *
     * @param gameScene The game-scene.
     * @param priority The priority the game-scene has in the render-loop.
     */
    public GameSceneEntry(GameScene gameScene, Priority priority) {
        this.gameScene = gameScene;
        this.priority = priority;
    }

    /**
     * <p>
     *    Gives the stored {@link GameScene}.
     * </p>
     *
     * @return Returns the wrapped {@link GameScene}.
     */
    public GameScene getGameScene() {
        return this.gameScene;
    }

    /**
     * <p>
     *    Gives the priority of the {@link GameScene} in the {@link de.SweetCode.e.loop.RenderLoop}. If the priority is
     *    {@link Priority#HIGH} it will be in the first n iterations of the loop. The position depends on when it was added
     *    to it, and vice versa.
     * </p>
     *
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

}
