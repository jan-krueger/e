package de.SweetCode.e;

import de.SweetCode.e.input.Input;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.loop.*;
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
 * <p>
 *     The E is the heart. It is responsible for holding everything together and to have a reference to all important
 *     parts of the engine and its information.
 * </p>
 */
public class E {

    /**
     * @TODO This is the pool size representing the number of threads to keep in the pool, even if they are idle, which are
     * responsible for executing the loops. This should maybe be a changeable in the {@link Settings}.
     */
    public static int POOL_SIZE = 2;

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
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(POOL_SIZE, new LoopThreadFactory());

    private RenderLoop renderLoop;
    private UpdateLoop updateLoop;
    private ProfilerLoop profilerLoop;
    private MouseMovingLoop mouseMovingLoop;
    //---

    /**
     * <p>
     *     Creates a new instance of the engine with its default settings.
     * </p>
     */
    public E() {
        this(new Settings() {});
    }

    /**
     * <p>
     *     Creates a new instance of the engine with custom settings.
     * </p>
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
        this.updateLoop = new UpdateLoop(this.input, (C.SECOND_AS_NANO / this.settings.getTargetTicks()));
        this.mouseMovingLoop = new MouseMovingLoop((C.SECOND_AS_NANO / this.settings.getTargetTicks()));

        if(settings.isDebugging()) {
            this.profilerLoop = new ProfilerLoop();
        }
        //---

    }

    /**
     * <p>
     *     Returns a singleton instance of {@link Random} or {@link SecureRandom} depending on the given parameter.
     * </p>
     *
     * @param secure If this is true it returns {@link SecureRandom}, otherwise {@link Random}.
     * @return Depending on the secure parameter the reference to the correct instance.
     */
    public Random getRandom(boolean secure) {
        return (secure ? E.secureRandom : E.random);
    }

    /**
     * <p>
     *     Returns a reference to the {@link Log} used by engine.
     * </p>
     *
     * @return Gives the log of the engine instance.
     */
    public Log getLog() {
        return this.log;
    }

    /**
     * <p>
     *     Returns a reference to the {@link EScreen} used by the engine to render frames.
     * </p>
     *
     * @return Gives the {@link EScreen} of the engine instance.
     */
    public EScreen getScreen() {
        return this.screen;
    }

    /**
     * <p>
     *     Gives an instance of the {@link Layers} of the engine, it contains all {@link de.SweetCode.e.rendering.layers.Layer}
     *     references used in the engine instance.
     * </p>
     *
     * @return A wrapper object of {@link de.SweetCode.e.rendering.layers.Layer layers}.
     */
    public Layers getLayers() {
        return this.layers;
    }

    /**
     * <p>
     *     Gives an instance of the {@link ProfilerLoop} used by the engine to collect data if the engine is in debug mode.
     * </p>
     *
     * @return Returns a {@link ProfilerLoop} reference if debugging is enabled, otherwise null.
     */
    public ProfilerLoop getProfilerLoop() {
        return this.profilerLoop;
    }

    /**
     * <p>
     *     Gives an instance of the {@link RenderLoop} used by the engine to coordinate rendering processes.
     * </p>
     *
     * @return Returns a {@link RenderLoop} reference.
     */
    public RenderLoop getRenderLoop() {
        return this.renderLoop;
    }

    /**
     * <p>
     *    Gives an instance of the {@link MouseMovingLoop} which is responsible for tracking the position of the mouse.
     * </p>
     *
     * @return Returns a {@link MouseMovingLoop} reference.
     */
    public MouseMovingLoop getMouseMovingLoop() {
        return this.mouseMovingLoop;
    }

    /**
     * <p>
     *     Returns a {@link LinkedHashMap} of all scenes registered to the engine. The key is the {@link Class} of the
     *     {@link GameScene} and the value is the related {@link GameSceneEntry}.
     * </p>
     *
     * @return A {@link LinkedHashMap} with all scenes, if no scenes are registered then the map is empty, but never null.
     */
    public Map<Class<? extends GameScene>, GameSceneEntry> getScenes() {
        return this.scenes;
    }

    /**
     * <p>
     *     Returns a instance of the settings used by the engine currently.
     * </p>
     *
     * @return The {@link Settings} of the game.
     */
    public Settings getSettings() {
        return this.settings;
    }

    /**
     * <p>
     *     Returns the amount of frames the engine could render in the last full second.
     * </p>
     *
     * @return The amount of frames per second, never negative.
     */
    public int getCurrentFPS() {
        return this.settings.useOpenGL() ? (int) this.renderLoop.getAnimator().getLastFPS() : this.renderLoop.getCurrentTicks();
    }

    /**
     * <p>
     *     Returns the amount of updates/ticks the engine could perform in the last full second.
     * </p>
     *
     * @return Returns the tick rate of the update loop, never negative.
     */
    public int getCurrentTicks() {
        return this.updateLoop.getCurrentTicks();
    }

    /**
     * <p>
     *     Returns a {@link CopyOnWriteArrayList} of all {@link GameComponentEntry GameComponent entries} that are
     *     currently registered to the engine. This includes active and inactive components as well as {@link GameScene},
     *     because they are a sub-class of {@link GameScene}.
     * </p>
     *
     * @return Returns {@link CopyOnWriteArrayList} of {@link GameComponentEntry GameComponentEntries}.
     */
    public List<GameComponentEntry> getGameComponents() {
        return this.gameComponents;
    }

    /**
     * <p>
     *     Adds a new GameComponent with {@link Priority#NORMAL}.
     * </p>
     *
     * @param gameComponent The reference to the {@link GameComponent}.
     */
    public void addComponent(GameComponent gameComponent) {

        this.addComponent(gameComponent, Priority.NORMAL);

    }

    /**
     * <pre>
     *     Add a new GameComponent with the specified priority. The priority determines when the {@link UpdateLoop} calls
     *     the {@link GameComponent#update(InputEntry, long)} method in each iteration.
     *     If the priority is {@link Priority#HIGH} then it will be called before {@link Priority#LOW} and {@link Priority#NORMAL}
     *     in each iteration of the update loop and vice versa.
     * </pre>
     *
     * @param gameComponent The reference to the {@link GameComponent}.
     * @param priority The priority the component has in the {@link UpdateLoop update-loop}. If {@link Settings#isParallelizingUpdate()}
     *                 is true then can the priority value only be {@link Priority#NORMAL}.
     */
    public void addComponent(GameComponent gameComponent, Priority priority) {

        if(!(priority == Priority.NORMAL) && this.settings.isParallelizingUpdate()) {
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
     * <pre>
     *     Adds a new scene to the engine with the {@link Priority} set to {@link Priority#NORMAL}.
     * </pre>
     * @param gameScene The game scene.
     */
    public void addScene(GameScene gameScene) {

        this.addScene(gameScene, Priority.NORMAL);

    }

    /**
     * <pre>
     *     Add a new GameScene with the specified priority. The priority determines when the {@link RenderLoop} calls the
     *     {@link GameScene#update(InputEntry, long)} and {@link GameScene#render(Layers)} method in each iteration.
     *     If the priority is {@link Priority#HIGH} then it will be called before {@link Priority#LOW} and {@link Priority#NORMAL}
     *     in each iteration of the update loop and vice versa.
     * </pre>
     * @param gameScene The game scene.
     * @param priority The priority the {@link GameScene#update(InputEntry, long)} method has in the {@link UpdateLoop update-loop}.
     */
    public void addScene(GameScene gameScene, Priority priority) {

        // also add it as normal game component
        this.addComponent(gameScene, priority);

        // add to scenes
        this.scenes.put(gameScene.getClass(), new GameSceneEntry(gameScene, priority));

        // Sort
        List<Map.Entry<Class<? extends GameScene>, GameSceneEntry>> list = new LinkedList<>(this.scenes.entrySet());
        list.sort(Comparator.comparingInt(i -> -i.getValue().getPriority().getPriority()));

        // clear scenes to put in "clear" order
        this.scenes.clear();
        list.forEach(e -> this.scenes.put(e.getKey(), e.getValue()));

    }

    /**
     * <pre>
     *     Selects the seen that the renderer is supposed to render. - The argument is the class of the scene you wanna
     *     render.
     *     If your class is called <i>ExampleScene</i> you just provide <i>ExampleScene.class</i> as argument, however
     *     you have to register your scene with {@link E#addScene(GameScene)}or {@link E#addScene(GameScene, Priority)}
     *     before calling this method.
     * </pre>
     *
     * @param scene The scene you wanna render.
     */
    public void show(Class<?> scene) {
        Assert.assertTrue("The scene doesn't exist.", this.scenes.containsKey(scene));
        this.screen.setScene(this.scenes.get(scene).getGameScene());
    }


    /**
     * <p>
     *     This will start all loops and kick off everything that is necessary for the engine to work well. If the engine
     *     has no scene registered yet, it will just add the {@link DefaultGameScene} and show it.
     * </p>
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
        this.executor.scheduleAtFixedRate(
                this.mouseMovingLoop,
                0,
                this.mouseMovingLoop.getOptimalIterationTime(),
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
     * <p>
     *     Gives the instance of the engine that is currently running. You can use this method everywhere to get easy
     *     access to its tools and references that you may need.
     * </p>
     *
     * @return Returns the instance of {@link E}.
     */
    public static E getE() {
        return E.instance;
    }

    /**
     * <p>
     *     This class contains some constants which are often used in the engine code. You are not supposed to use them,
     *     because they may change depending on the engines needs.
     * </p>
     */
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
