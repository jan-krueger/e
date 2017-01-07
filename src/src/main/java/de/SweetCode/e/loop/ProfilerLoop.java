package de.SweetCode.e.loop;

import com.sun.management.OperatingSystemMXBean;
import de.SweetCode.e.E;
import de.SweetCode.e.Settings;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ProfilerLoop extends Loop {

    private OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    private double TMP_CPU = 0;
    private double TMP_MEMORY_USED = 0;

    private double averageCPU = 0;
    private double averageMemoryUsed = 0;

    private int CPU_PROCESSORS = 0;
    private long MEMORY_MAX = 0;

    private List<GarbageCollectorMXBean> GC_BEANS = ManagementFactory.getGarbageCollectorMXBeans();
    private Set<Thread> THREAD_LIST = Thread.getAllStackTraces().keySet();

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
     * Returns the average memory use in the last time frame.
     * @return
     */
    public double getAverageMemoryUsed() {
        return this.averageMemoryUsed;
    }

    /**
     * Returns the max heap size.
     * @return
     */
    public long getMaxMemory() {
        return this.MEMORY_MAX;
    }

    /**
     * Gives all related GC (garbage collection) beans.
     * @return
     */
    public List<GarbageCollectorMXBean> getGCBeans() {
        return this.GC_BEANS;
    }

    /**
     * Gives all threads.
     * @return
     */
    public Set<Thread> getThreads() {
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
                    this.CPU_PROCESSORS = this.bean.getAvailableProcessors();
                    this.TMP_CPU = 0;
                } else {
                    this.TMP_CPU += this.bean.getProcessCpuLoad();
                }
            }

            //--- Memory
            if(displays.contains(Settings.DebugDisplay.MEMORY_PROFILE)) {

                if(updateRequired) {
                    this.MEMORY_MAX = Runtime.getRuntime().maxMemory();
                    this.averageMemoryUsed = (this.TMP_MEMORY_USED / this.updates);
                    this.TMP_MEMORY_USED = 0;
                } else {
                    this.TMP_MEMORY_USED += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                }

            }

            //--- GC
            if(displays.contains(Settings.DebugDisplay.GC_PROFILE) && updateRequired) {
                this.GC_BEANS = ManagementFactory.getGarbageCollectorMXBeans();
            }

            //--- Threads
            if(displays.contains(Settings.DebugDisplay.THREAD_PROFILE) && updateRequired) {
                this.THREAD_LIST = Thread.getAllStackTraces().keySet();
            }

            //--- Reset
            if(updateRequired) {
                this.deltaTime = 0;
                this.updates = 0;
            }

    }

}
