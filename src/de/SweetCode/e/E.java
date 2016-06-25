package de.SweetCode.e;

import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.KeyEntry;
import de.SweetCode.e.log.Log;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class E {

    private static E instance;
    private final static long NANO_SECOND = TimeUnit.SECONDS.toNanos(1);

    private final Input input = new Input();
    private final Log log;

    private final EScreen screen;
    private final Settings settings;

    private final List<GameComponent> gameComponents = new ArrayList<>();
    private final Map<Class<? extends GameScene>, GameScene> scenes = new HashMap<>();

    private final long optimalTime;

    private int currentFPS = 0;

    private boolean isRunning = true;

    /**
     * Creates a new game.
     *
     */
    public E(Settings settings) {

        Assert.assertNull("An instance of E already exists.", E.instance);
        Assert.assertNotNull("Settings cannot be null.", settings);

        List<String> validation = E.validateSettings(settings);
        Assert.assertTrue("Failed to validate settings: " + StringUtils.join(validation, ", "), validation.isEmpty());

        E.instance = this;

        this.settings = settings;
        this.screen = new EScreen();
        this.log = new Log(settings.getLogCapacity());

        this.optimalTime =  E.NANO_SECOND / this.settings.getTargetFPS();

    }

    /**
     * The settings of the game.
     * @return
     */
    public Settings getSettings() {
        return this.settings;
    }

    /**
     * The current number of frames per second.
     * @return
     */
    public int getCurrentFPS() {
        return this.currentFPS;
    }


    /**
     * Add a new GameComponent.
     * @param gameComponent
     */
    public void addComponent(GameComponent gameComponent) {

        Assert.assertFalse("The component has already been registered.", this.gameComponents.contains(gameComponent));

        this.gameComponents.add(gameComponent);

    }

    /**
     * Add a new scene to the engine.
     * @param gameScene
     */
    public void addScene(GameScene gameScene) {

        Assert.assertFalse("The scene has already been registered.", this.scenes.containsKey(gameScene.getClass()));

        this.scenes.put(gameScene.getClass(), gameScene);

    }

    /**
     * Starts to render this scene at the front.
     * @param scene
     */
    public void show(Class<? extends GameScene> scene) {

        Assert.assertTrue("The scene doesn't exist.", this.scenes.containsKey(scene));
        this.screen.render(this.scenes.get(scene));

    }

    /**
     * Starts the engine.
     */
    public void run() {

        long lastIteration = System.nanoTime();
        long lastFrameTime = 0;

        int tmpFPS = 0;

        while (this.isRunning) {

            long now = System.nanoTime();
            long updateLength = now - lastIteration;
            lastIteration = now;

            // update the frame counter
            lastFrameTime += updateLength;
            tmpFPS++;

            int delta = (int) (updateLength / Double.valueOf(this.optimalTime));

            if(lastFrameTime >= E.NANO_SECOND) {
                lastFrameTime = 0;
                this.currentFPS = tmpFPS;
                tmpFPS = 0;
            }

            // get the input
            Stream<KeyEntry> input = this.input.getPressedKeys();

            this.gameComponents.forEach(e -> {

                if(e.isActive()) {
                    e.update(input, this.settings.getDeltaUnit().convert(delta, TimeUnit.NANOSECONDS));
                }

            });

            this.scenes.forEach((k, v) -> {

                if(v.isActive()) {
                    v.update(input, this.settings.getDeltaUnit().convert(delta, TimeUnit.NANOSECONDS));
                    this.screen.render(v);
                }

            });


            try{
                long delay = TimeUnit.NANOSECONDS.toMillis(this.optimalTime - (System.nanoTime() - now));
                if(delay > 0) {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Returns the current instance of E.
     * @return
     */
    public static E getE() {
        return E.instance;
    }

    /**
     * Validates the settings.
     * @param settings
     * @return
     */
    private static List<String> validateSettings(Settings settings) {

        List<String> invalids = new ArrayList<>();

        if(settings.getName() == null) {
            invalids.add("name cannot be null");
        }

        if(settings.getDeltaUnit() == null) {
            invalids.add("deltaUnit cannot be null");
        }

        if(settings.getVersion() == null) {
            invalids.add("version cannot be null");
        }

        if(settings.getTargetFPS() < 0) {
            invalids.add("targetFPS cannot be negative");
        }

        if(settings.getWidth() < 1) {
            invalids.add("width cannot be negative or 0");
        }

        if(settings.getHeight() < 1) {
            invalids.add("height cannot be negative or 0");
        }

        if(settings.getLogCapacity() < 1) {
            invalids.add("logCapacity cannot be negative or 0");
        }

        return invalids;

    }

}
