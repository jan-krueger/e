package de.SweetCode.e.loop;

import com.sun.management.OperatingSystemMXBean;
import de.SweetCode.e.E;
import de.SweetCode.e.Settings;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private long MEMORY_HEAP_MAX = 0;
    private long MEMORY_JVM_MAX = 0;

    private List<GarbageCollectorMXBean> GC_BEANS = ManagementFactory.getGarbageCollectorMXBeans();
    private Map<String, List<Thread>> THREAD_LIST = ProfilerLoop.getThreadsByGroup();

    private double deltaTime = 0;
    private double updates = 0;

    public ProfilerLoop() {
        super("Profiler Loop", TimeUnit.MILLISECONDS.toNanos(100));
    }

    /**
     * Average CPU usage in the last time frame caused by the JVM.
     * @return
     */
    public double getAverageCPU() {
        return this.averageCPU;
    }

    /**
     * Amount of available processors.
     * @return
     */
    public int getAvailableProcessors() {
        return this.CPU_PROCESSORS;
    }

    /**
     * Returns the average memory use in the last time frame by the heap.
     * @return
     */
    public double getAverageHeapMemoryUsed() {
        return this.averageHeapMemoryUsed;
    }

    /**
     * Returns the max heap size.
     * @return
     */
    public long getMaxHeapSize() {
        return this.MEMORY_HEAP_MAX;
    }

    /**
     * Returns the average memory use in the last time frame by the JVM.
     * @return
     */
    public double getAverageJvmMemoryUsed() {
        return this.averageJvmMemoryUsed;
    }

    /**
     * Returns the max JVM size.
     * @return
     */
    public long getMaxJvmSize() {
        return this.MEMORY_JVM_MAX;
    }

    /**
     * Gives all related GC (garbage collection) beans.
     * @return
     */
    public List<GarbageCollectorMXBean> getGCBeans() {
        return this.GC_BEANS;
    }

    /**
     * Gives all threads grouped by their thread group name.
     * @return
     */
    public Map<String, List<Thread>> getThreads() {
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
                    this.MEMORY_JVM_MAX = this.memoryBean.getNonHeapMemoryUsage().getMax();

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

            //--- Reset
            if(updateRequired) {
                this.deltaTime = 0;
                this.updates = 0;
            }

    }

    /**
     * Generating a map of all threads grouped by their thread group.
     * @return
     */
    private static Map<String, List<Thread>> getThreadsByGroup() {
        // get & sort by id
        List<Thread> threads = new ArrayList<>(Thread.getAllStackTraces().keySet());
        Collections.sort(threads, Comparator.comparingLong(Thread::getId));

        // store by groups
        Map<String, List<Thread>> threadGroups = threads.stream().collect(
                Collectors.groupingBy(t -> t.getThreadGroup().getName())
        );

        return threadGroups;
    }

}