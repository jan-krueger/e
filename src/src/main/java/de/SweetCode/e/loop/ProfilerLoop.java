package de.SweetCode.e.loop;

import com.sun.management.OperatingSystemMXBean;
import de.SweetCode.e.E;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class ProfilerLoop extends Loop {

    private OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    private double TMP_CPU = 0;
    private double TMP_MEMORY_USED = 0;

    private double averageCPU = 0;
    private double averageMemoryUsed = 0;

    private double deltaTime = 0;
    private double updates = 0;

    public ProfilerLoop() {
        super(TimeUnit.MILLISECONDS.toNanos(100));
    }

    public double getAverageCPU() {
        return this.averageCPU;
    }

    public double getAverageMemoryUsed() {
        return this.averageMemoryUsed;
    }

    @Override
    public void tick(long updateLength) {

        this.deltaTime += updateLength;
        this.updates++;

        // We gonna update the display-values every: 1 second.
        if(this.deltaTime >= E.C.SECOND_AS_NANO) {
            this.averageCPU = (this.TMP_CPU / this.updates);
            this.averageMemoryUsed = (this.TMP_MEMORY_USED / this.updates);

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
