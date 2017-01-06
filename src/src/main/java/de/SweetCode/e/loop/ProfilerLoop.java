package de.SweetCode.e.loop;

import com.sun.management.OperatingSystemMXBean;
import de.SweetCode.e.E;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
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

    private double deltaTime = 0;
    private double updates = 0;

    public ProfilerLoop() {
        super(TimeUnit.MILLISECONDS.toNanos(100));
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

    @Override
    public void tick(long updateLength) {

        this.deltaTime += updateLength;
        this.updates++;

        // We gonna update the display-values every: 1 second.
        if(this.deltaTime >= E.C.SECOND_AS_NANO) {
            this.averageCPU = (this.TMP_CPU / this.updates);
            this.averageMemoryUsed = (this.TMP_MEMORY_USED / this.updates);

            //--- Update usually constant values
            this.CPU_PROCESSORS = this.bean.getAvailableProcessors();
            this.MEMORY_MAX = Runtime.getRuntime().maxMemory();
            this.GC_BEANS = ManagementFactory.getGarbageCollectorMXBeans();

            // reset
            this.deltaTime = 0;
            this.updates = 0;

            this.TMP_CPU = 0;
            this.TMP_MEMORY_USED = 0;
        }

        this.TMP_CPU += this.bean.getProcessCpuLoad();
        this.TMP_MEMORY_USED += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

    }

}
