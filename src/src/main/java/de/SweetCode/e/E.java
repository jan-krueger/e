package de.SweetCode.e;

import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;
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

/**
 * The E is the heart. It is responsible for holding everything together and to have
 * a reference to all important parts of the engine and its information.
 */
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
     *
     * @param settings The custom settings to pass to the engine.
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

    /**
     * To get a {@link Random} instance.
     * @param secure If this is true it returns {@link SecureRandom}, otherwise {@link Random}.
     * @return Depending on the secure parameter the reference to the correct instance.
     */
    public Random getRandom(boolean secure) {
        return (secure ? E.secureRandom : E.random);
    }

    /**
     * @return Gives the log of the engine instance.
     */
    public Log getLog() {
        return this.log;
    }

    /**
     * @return Gives the {@link EScreen} of the engine instance.
     */
    public EScreen getScreen() {
        return this.screen;
    }

    /**
     * @return Gives a instance of the {@link Layers} of the engine, it contains all {@link de.SweetCode.e.rendering.layers.Layer}
     *         references used in the engine instance.
     */
    public Layers getLayers() {
        return this.layers;
    }

    /**
     * @return Returns the instance of the {@link ProfilerLoop}. It returns null if debugging is not enabled.
     */
    public ProfilerLoop getProfilerLoop() {
        return this.profilerLoop;
    }

    /**
     * @return A map with all scenes. The key is the {@link Class} of the {@link GameScene} and the value
     *         is the related {@link GameSceneEntry}.
     */
    public Map<Class<? extends GameScene>, GameSceneEntry> getScenes() {
        return this.scenes;
    }

    /**
     * @return The {@link Settings} of the game.
     */
    public Settings getSettings() {
        return this.settings;
    }

    /**
     * @return The current amount of frames per second.
     */
    public int getCurrentFPS() {
        return this.renderLoop.getCurrentTicks();
    }

    /**
     * @return Returns the current tick rate of the update loop.
     */
    public int getCurrentTicks() {
        return this.updateLoop.getCurrentTicks();
    }

    /**
     * @return Returns {@link CopyOnWriteArrayList} of {@link GameComponentEntry GameComponentEntries}.
     */
    public List<GameComponentEntry> getGameComponents() {
        return this.gameComponents;
    }

    /**
     * Adds a new GameComponent with {@link Priority#NORMAL}.
     * @param gameComponent The reference to the {@link GameComponent}.
     */
    public void addComponent(GameComponent gameComponent) {

        this.addComponent(gameComponent, Priority.NORMAL);

    }

    /**
     * Add a new GameComponent with the specified priority.
     * @param gameComponent The reference to the {@link GameComponent}.
     * @param priority The priority the component has in the {@link UpdateLoop update-loop}. If {@link Settings#isParallelizingUpdate()}
     *                 is true then can the priority value only be {@link Priority#NORMAL}.
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
     * Add a new scene to the engine with {@link Priority#NORMAL}..
     * @param gameScene The game scene.
     */
    public void addScene(GameScene gameScene) {

        this.addScene(gameScene, Priority.NORMAL);

    }

    /**
     * Add a new scene to the engine.
     * @param gameScene The game scene.
     * @param priority The priority the {@link GameScene#update(InputEntry, long)} method has in the {@link UpdateLoop update-loop}
     *                 and the priority of the {@link GameScene#render(Layers)} method has in the {@link RenderLoop render-loop}.
     *                 If {@link Settings#isParallelizingUpdate()} is true then can the priority value only be {@link Priority#NORMAL}.
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
     * Select the seen you wanna render at the moment.
     * @param scene The scne you wanna render.
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

        if(this.settings.isDebugging() && !(this.settings.getDebugInformation().isEmpty())) {
            this.executor.scheduleAtFixedRate(
                    this.profilerLoop,
                    0,
                    this.profilerLoop.getOptimalIterationTime(),
                    TimeUnit.NANOSECONDS
            );
        }

    }

    /**
     * @return Returns the instance of {@link E}.
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
