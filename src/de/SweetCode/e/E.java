package de.SweetCode.e;

import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.log.Log;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.StringUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class E {

    private static E instance;
    private final static long NANO_SECOND = TimeUnit.SECONDS.toNanos(1);
    private final static Random random = new Random();
    private final static Random secureRandom = new SecureRandom();

    private final Input input;
    private final Log log;

    private final EScreen screen;
    private final Settings settings;

    private final List<GameComponent> gameComponents = new CopyOnWriteArrayList<>();
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
        this.log = new Log(settings.getLogCapacity());
        this.screen = new EScreen();
        this.input = new Input();


        this.optimalTime =  E.NANO_SECOND / this.settings.getTargetFPS();

    }

    public Random getRandom(boolean secure) {
        return (secure ? E.secureRandom : E.random);
    }

    public Log getLog() {
        return this.log;
    }

    public EScreen getScreen() {
        return this.screen;
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

    public List<GameComponent> getGameComponents() {
        return this.gameComponents;
    }

    /**
     * Add a new GameComponent.
     * @param gameComponent
     */
    public void addComponent(GameComponent gameComponent) {

        this.gameComponents.add(gameComponent);

    }

    /**
     * Add a new scene to the engine.
     * @param gameScene
     */
    public void addScene(GameScene gameScene) {

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

            if(lastFrameTime >= E.NANO_SECOND) {
                this.currentFPS = tmpFPS;
                lastFrameTime = 0;
                tmpFPS = 0;
            }

            // get the input
            InputEntry input = new InputEntry(this.input.getKeyboardEntries(), this.input.getMouseEntries());

            long delta = Math.max(this.settings.getDeltaUnit().convert(updateLength, TimeUnit.NANOSECONDS), (this.settings.roundDelta() ? 1 : 0));

            this.gameComponents.forEach(e -> {

                if(e.isActive()) {
                    e.update(input, delta);
                }

            });

            this.scenes.forEach((k, v) -> {

                if(v.isActive()) {
                    v.update(input, delta);
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
