package de.SweetCode.e;

import de.SweetCode.e.input.Input;
import de.SweetCode.e.loop.LoopThreadFactory;
import de.SweetCode.e.loop.ProfilerLoop;
import de.SweetCode.e.loop.RenderLoop;
import de.SweetCode.e.loop.UpdateLoop;
import de.SweetCode.e.rendering.DefaultGameScene;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.GameSceneEntry;
import de.SweetCode.e.rendering.Priority;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.StringUtils;
import de.SweetCode.e.utils.log.Log;
import de.SweetCode.e.utils.log.LogEntry;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class E {

    //--- Static & Final Variables
    private static E instance;
    private final static Random random = new Random();
    private final static Random secureRandom = new SecureRandom();
    //---

    //--- User Input
    private final Input input;
    //---

    //--- Loop & Render Related
    private final EScreen screen;
    private final Layers layers;

    private final List<GameComponentEntry> gameComponents = new CopyOnWriteArrayList<>();
    private final Map<Class<? extends GameScene>, GameSceneEntry> scenes = new LinkedHashMap<>();
    //---

    //--- Internals
    private final Settings settings;
    private final Log log;
    //---

    //--- Related To Loops
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2, new LoopThreadFactory());

    private RenderLoop renderLoop;
    private UpdateLoop updateLoop;
    private ProfilerLoop profilerLoop;
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

        List<String> validation = Settings.Validator.validate(settings);
        Assert.assertTrue("Failed to validate settings: " + StringUtils.join(validation, ", "), validation.isEmpty());

        E.instance = this;
        this.settings = settings;

        //--- Setting up internals
        this.log = new Log(settings.getLogCapacity());
        this.screen = new EScreen();
        this.input = new Input();
        this.layers = new Layers(settings.getAmountOfLayers());
        //---

        //--- Setting up loops
        this.renderLoop = new RenderLoop(this.screen, (C.SECOND_AS_NANO / this.settings.getTargetFPS()));
        this.updateLoop = new UpdateLoop(this.settings, this.input, (C.SECOND_AS_NANO / this.settings.getTargetTicks()));

        if(settings.isDebugging()) {
            this.profilerLoop = new ProfilerLoop();
        }
        //---

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

    /**
     * Returns the instance of the profiler loop. It returns null if debugging is not enabled.
     * @return
     */
    public ProfilerLoop getProfilerLoop() {
        return this.profilerLoop;
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
        return this.renderLoop.getCurrentTicks();
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

        if(!(priority == Priority.NORMAL)) {
            this.getLog().log(
                    LogEntry.Builder.create().message(
                            "If you enabled isParallelizingUpdate you cannot define the priority of a GameComponent and/or" +
                                    "the GameComponent that is tied to a GameScene, however the priority for the GameScene itself " +
                                    "will be set."
                    ).build()
            );
        }

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

        //--- Schedule Various Loops (Currently: Rendering & Update Loop)
        this.executor.scheduleAtFixedRate(
                this.renderLoop,
                0,
                this.renderLoop.getOptimalIterationTime(),
                TimeUnit.NANOSECONDS
        );
        this.executor.scheduleAtFixedRate(
                this.updateLoop,
                0,
                this.updateLoop.getOptimalIterationTime(),
                TimeUnit.NANOSECONDS
        );

        if(this.settings.isDebugging()) {
            this.executor.scheduleAtFixedRate(
                    this.profilerLoop,
                    0,
                    this.profilerLoop.getOptimalIterationTime(),
                    TimeUnit.NANOSECONDS
            );
        }

    }

    /**
     * Returns the current instance of E.
     * @return
     */
    public static E getE() {
        return E.instance;
    }

    public static class C {

        /**
         * 1 second as nanoseconds.
         */
        public final static long SECOND_AS_NANO = TimeUnit.SECONDS.toNanos(1);

        /**
         * Multiplier to convert bytes to megabytes.
         */
        public final static double BYTES_TO_MEGABYTES = Math.pow(10, -6);

    }

}
