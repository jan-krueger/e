package de.SweetCode.e.loop;

import com.sun.management.OperatingSystemMXBean;
import de.SweetCode.e.E;
import de.SweetCode.e.GameComponentEntry;
import de.SweetCode.e.Settings;
import de.SweetCode.e.event.Event;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * ProfilerLoop is part of the debugging system that comes with e. It is mainly responsible for collecting data frequently.
 * The loop only gets registered if {@link Settings#isDebugging()} is true and only keeps track of values specified by
 * {@link Settings#getDebugInformation()}.
 * </p>
 */
public class ProfilerLoop extends Loop {

    private OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    private double TMP_CPU = 0;
    private double TMP_MEMORY_HEAP_USED = 0;
    private double TMP_MEMORY_JVM_USED = 0;

    private double averageCPU = 0;
    private double averageHeapMemoryUsed = 0;
    private double averageJvmMemoryUsed = 0;

    private int CPU_PROCESSORS = 0;
    private long OBJECTS_ACTIVE = 0;
    private long MEMORY_HEAP_MAX = 0;

    private int EVENT_SIZE = 0;

    private List<GarbageCollectorMXBean> GC_BEANS = ManagementFactory.getGarbageCollectorMXBeans();
    private Map<ThreadGroup, List<Thread>> THREAD_LIST = ProfilerLoop.getThreadsByGroup();

    private double deltaTime = 0;
    private double updates = 0;

    /**
     * <p>
     *    Creates a new profiler loop.
     * </p>
     */
    public ProfilerLoop() {
        super("Profiler Loop", TimeUnit.MILLISECONDS.toNanos(100));
    }

    /**
     * <p>
     *    Returns the average CPU value measured in the last full second. The value is relative and is always in the rang
     *    of [0; 1].
     * </p>
     *
     * @return Average CPU usage.
     */
    public double getAverageCPU() {
        return this.averageCPU;
    }

    /**
     * <p>
     *    The amount of process threads available for the JVM.
     * </p>
     *
     * @return Amount of available processors.
     */
    public int getAvailableProcessors() {
        return this.CPU_PROCESSORS;
    }

    /**
     * <p>
     *    The average amount of heap memory used by the application in bytes.
     * </p>
     *
     * @return Average heap usage in bytes.
     */
    public double getAverageHeapMemoryUsed() {
        return this.averageHeapMemoryUsed;
    }

    /**
     * <p>
     *    The max. size of the heap memory space in bytes.
     * </p>
     *
     * @return Heap size in bytes.
     */
    public long getMaxHeapSize() {
        return this.MEMORY_HEAP_MAX;
    }

    /**
     * <p>
     *     The average amount of memory used by the JVM (Java Runtime Environment) in the last full second in bytes.
     * </p>
     *
     * @return JVM memory usage in bytes.
     */
    public double getAverageJvmMemoryUsed() {
        return this.averageJvmMemoryUsed;
    }

    /**
     * <p>
     *    The amount of active {@link de.SweetCode.e.GameComponent GameComponents} in the last full second.
     * </p>
     * @return Amount of active objects.
     */
    public long getActiveObjects() {
        return this.OBJECTS_ACTIVE;
    }

    /**
     * <p>
     *    The amount of queued {@link de.SweetCode.e.event.Event} via {@link de.SweetCode.e.event.EventHandler#trigger(Event, boolean)}.
     * </p>
     *
     * @return Amount of queued events.
     */
    public int getEventSize() {
        return this.EVENT_SIZE;
    }

    /**
     * <p>
     *    A {@link List} of GC beans.
     * </p>
     *
     * @return Gives all related GC (garbage collection) beans.
     */
    public List<GarbageCollectorMXBean> getGCBeans() {
        return this.GC_BEANS;
    }

    /**
     * <p>
     *    Returns a map of all threads found by the engine. The key of the map is the {@link ThreadGroup} of a thread
     *    and the value is a {@link List} of {@link Thread Threads}. The threads are sorted by their IDs.
     * </p>
     *
     * @return All threads.
     */
    public Map<ThreadGroup, List<Thread>> getThreads() {
        return this.THREAD_LIST;
    }

    @Override
    public void tick(long updateLength) {

        List<Settings.DebugDisplay> displays = E.getE().getSettings().getDebugInformation();

        this.deltaTime += updateLength;
        this.updates++;

        // We gonna update the display-values every: 1 second.
        boolean updateRequired = (this.deltaTime >= E.C.SECOND_AS_NANO);

        //--- CPU
        if(displays.contains(Settings.DebugDisplay.CPU_PROFILE)) {
            if(updateRequired) {
                this.averageCPU = (this.TMP_CPU / this.updates);
                this.CPU_PROCESSORS = this.osBean.getAvailableProcessors();

                this.TMP_CPU = 0;
            } else {
                this.TMP_CPU += this.osBean.getProcessCpuLoad();
            }
        }

        //--- Memory
        if(displays.contains(Settings.DebugDisplay.MEMORY_PROFILE)) {

            if(updateRequired) {
                this.MEMORY_HEAP_MAX = this.memoryBean.getHeapMemoryUsage().getMax();

                this.averageHeapMemoryUsed = (this.TMP_MEMORY_HEAP_USED / this.updates);
                this.averageJvmMemoryUsed = (this.TMP_MEMORY_JVM_USED / this.updates);

                this.TMP_MEMORY_HEAP_USED = 0;
                this.TMP_MEMORY_JVM_USED = 0;
            } else {
                this.TMP_MEMORY_HEAP_USED += this.memoryBean.getHeapMemoryUsage().getUsed();
                this.TMP_MEMORY_JVM_USED += this.memoryBean.getNonHeapMemoryUsage().getUsed();
            }

        }

        //--- GC
        if(displays.contains(Settings.DebugDisplay.GC_PROFILE) && updateRequired) {
            this.GC_BEANS = ManagementFactory.getGarbageCollectorMXBeans();
        }

        //--- Threads
        if(displays.contains(Settings.DebugDisplay.THREAD_PROFILE) && updateRequired) {
            this.THREAD_LIST = ProfilerLoop.getThreadsByGroup();
        }

        //--- Profile
        if(displays.contains(Settings.DebugDisplay.LOOP_PROFILE) && updateRequired) {
            this.OBJECTS_ACTIVE = E.getE().getGameComponents().stream()
                    .filter(e -> e.getGameComponent().isActive())
                    .count();
        }

        //--- Event
        if(displays.contains(Settings.DebugDisplay.EVENT_PROFILE) && updateRequired) {
            this.EVENT_SIZE = E.getE().getEventHandler().getQueuedEvents().size();
        }

        //--- Reset
        if(updateRequired) {
            this.deltaTime = 0;
            this.updates = 0;
        }

    }

    /**
     * <p>
     *    Generates a map with {@link ThreadGroup} as key and a {@link List} of {@link Thread Threads} as value. It grabs
     *    the threads by calling {@link Thread#getAllStackTraces()}. It is also sorting the value by the ID of the threads,
     *    and grouping the thread by their {@link ThreadGroup}.
     * </p>
     *
     * @return A map of threads and thread groups.
     */
    private static Map<ThreadGroup, List<Thread>> getThreadsByGroup() {
        // get & sort by id
        List<Thread> threads = new ArrayList<>(Thread.getAllStackTraces().keySet());
        threads.sort(Comparator.comparing(Thread::getId));

        // store by groups
        return threads.stream().collect(
                Collectors.groupingBy(Thread::getThreadGroup)
        );
    }

}
