package de.SweetCode.e;

import de.SweetCode.e.input.Input;
import de.SweetCode.e.loop.RenderLoop;
import de.SweetCode.e.loop.UpdateLoop;
import de.SweetCode.e.rendering.DefaultGameScene;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.GameSceneEntry;
import de.SweetCode.e.rendering.Priority;
import de.SweetCode.e.rendering.layers.Layer;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.StringUtils;
import de.SweetCode.e.utils.log.Log;

import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class E {

    //--- Static & Final Variables
    private static E instance;
    public final static long NANO_SECOND = TimeUnit.SECONDS.toNanos(1);
    private final static Random random = new Random();
    private final static Random secureRandom = new SecureRandom();
    //---

    //--- User Input
    private final Input input;
    //---

    //--- Loop & Render Related
    private final EScreen screen;
    private final Layers layers = new Layers();

    private final List<GameComponentEntry> gameComponents = new CopyOnWriteArrayList<>();
    private final Map<Class<? extends GameScene>, GameSceneEntry> scenes = new LinkedHashMap<>();
    //---

    //--- Internals
    private final Settings settings;
    private final Log log;
    //---

    //--- Related To Loops
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private RenderLoop renderLoop;
    private UpdateLoop updateLoop;
    //---

    /**
     * Creates a new game with default settings.
     */
    public E() {
        this(new Settings() {});
    }

    /**
     * Creates a new game with custom settings.
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

        for(int i = 0; i < settings.getAmountOfLayers(); i++) {
            this.layers.add(new Layer());
        }

        this.renderLoop = new RenderLoop(this.screen, (E.NANO_SECOND / this.settings.getTargetFPS()));
        this.updateLoop = new UpdateLoop(this.input, (E.NANO_SECOND / this.settings.getTargetTicks()));


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

    public Layers getLayers() {
        return this.layers;
    }


    public Map<Class<? extends GameScene>, GameSceneEntry> getScenes() {
        return this.scenes;
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
        return this.renderLoop.getCurrentFPS();
    }

    /**
     * Returns the current tick rate of the update loop.
     * @return
     */
    public int getCurrentTicks() {
        return this.updateLoop.getCurrentTicks();
    }

    public List<GameComponentEntry> getGameComponents() {
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

        this.gameComponents.add(new GameComponentEntry(gameComponent, priority));

        // Sort
        Collections.sort(this.gameComponents, new GameComponentEntry.EntryComparator());

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

        // also add it as normal game component
        this.addComponent(gameScene, priority);

        // add to scenes
        this.scenes.put(gameScene.getClass(), new GameSceneEntry(gameScene, priority));

        // Sort
        List<Map.Entry<Class<? extends GameScene>, GameSceneEntry>> list = new LinkedList<>(this.scenes.entrySet());
        Collections.sort(list, new GameSceneEntry.EntryComparator());

        // clear scenes to put in "clear" order
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

        // set default game scene if the developer hasn't added any scene yet...
        if(this.scenes.isEmpty()) {
            this.addScene(new DefaultGameScene());
            this.show(DefaultGameScene.class);
        }
        //

        this.executor.submit(this.renderLoop);
        this.executor.submit(this.updateLoop);

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

}
