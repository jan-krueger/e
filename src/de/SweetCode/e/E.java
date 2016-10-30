package de.SweetCode.e;

import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.log.Log;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.Priority;
import de.SweetCode.e.rendering.camera.Camera;
import de.SweetCode.e.rendering.layers.Layer;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.StringUtils;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

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

    private final Camera camera;
    private final EScreen screen;
    private final Layers layers = new Layers();

    private final Settings settings;

    private final List<ComponentEntry> gameComponents = new CopyOnWriteArrayList<>();
    private final Map<Class<? extends GameScene>, SceneEntry> scenes = new LinkedHashMap<>();

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

        for(int i = 0; i < settings.getAmountOfLayers(); i++) {
            this.layers.add(new Layer());
        }

        this.camera = settings.getCamera();
        this.addComponent(this.camera);


    }

    public Random getRandom(boolean secure) {
        return (secure ? E.secureRandom : E.random);
    }

    public Log getLog() {
        return this.log;
    }

    public Camera getCamera() {
        return camera;
    }

    public EScreen getScreen() {
        return this.screen;
    }

    public Layers getLayers() {
        return this.layers;
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

    public List<ComponentEntry> getGameComponents() {
        return this.gameComponents;
    }

    /**
     * Add a new GameComponent.
     * @param gameComponent
     */
    public void addComponent(GameComponent gameComponent) {

        this.addComponent(gameComponent, Priority.NORMAL);

    }

    /**
     * Add a new GameComponent.
     * @param gameComponent
     * @param priority
     */
    public void addComponent(GameComponent gameComponent, Priority priority) {

        this.gameComponents.add(new ComponentEntry(gameComponent, priority));

        // Sort
        Collections.sort(this.gameComponents, (o1, o2) -> {

            if(o1.getValue().getPriority() == o2.getValue().getPriority()) {
                return 0;
            }

            if(o1.getValue().getPriority() < o2.getValue().getPriority()) {
                return 1;
            }

            return -1;

        });

    }

    /**
     * Add a new scene to the engine.
     * @param gameScene
     */
    public void addScene(GameScene gameScene) {

        this.addScene(gameScene, Priority.NORMAL);

    }

    /**
     * Add a new scene to the engine.
     * @param gameScene
     * @param priority
     */
    public void addScene(GameScene gameScene, Priority priority) {

        this.scenes.put(gameScene.getClass(), new SceneEntry(gameScene, priority));

        // Sort
        List<Map.Entry<Class<? extends GameScene>, SceneEntry>> list = new LinkedList<>(this.scenes.entrySet());
        Collections.sort(list, (o1, o2) -> {

            if(o1.getValue().getValue().getPriority() == o2.getValue().getValue().getPriority()) {
                return 0;
            }

            if(o1.getValue().getValue().getPriority() < o2.getValue().getValue().getPriority()) {
                return 1;
            }

            return -1;

        });
        this.scenes.clear();
        list.forEach(e -> this.scenes.put(e.getKey(), e.getValue()));

    }

    /**
     * Starts to setScene this scene at the front.
     * @param scene
     */
    public void show(Class<?> scene) {
        Assert.assertTrue("The scene doesn't exist.", this.scenes.containsKey(scene));
        this.screen.setScene(this.scenes.get(scene).getGameScene());
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
            InputEntry input = new InputEntry(this.input.getKeyboardEntries(), this.input.getMouseEntries(), this.input.getMouseWheelEntries(), this.input.getMouseDraggedEntries(), this.input.getMouseMovedEntries(), this.input.getMouseReleasedQueue());

            long delta = Math.max(this.settings.getDeltaUnit().convert(updateLength, TimeUnit.NANOSECONDS), (this.settings.roundDelta() ? 1 : 0));

            this.gameComponents.forEach(k -> {

                if(k.getGameComponent().isActive()) {
                    k.getGameComponent().update(input, delta);
                }

            });

            this.scenes.forEach((k, v) -> {

                if(v.getGameScene().isActive()) {
                    v.getGameScene().update(input, delta);
                    this.screen.invalidate();
                    this.screen.repaint();
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

        if(settings.getAmountOfLayers() < 1) {
            invalids.add("The amount of layers must be at least 1");
        }

        return invalids;

    }

    private class SceneEntry {

        private GameScene gameScene;
        private Priority priority;

        public SceneEntry(GameScene gameScene, Priority priority) {
            this.gameScene = gameScene;
            this.priority = priority;
        }

        public GameScene getGameScene() {
            return this.gameScene;
        }

        public Priority getValue() {
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

}
