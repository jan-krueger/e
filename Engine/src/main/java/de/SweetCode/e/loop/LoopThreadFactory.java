package de.SweetCode.e.loop;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *    The LoopThreadFactory is a {@link ThreadFactory} used by {@link de.SweetCode.e.E} to schedule {@link Loop Loops}.
 * </p>
 */
public class LoopThreadFactory implements ThreadFactory {

    //---
    private final static String NAME_FORMAT = "e-%s-thread-%d";

    //---
    private final String name;
    private final int priority;

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * <p>
     *    Creates a new instance of LoopThreadFactory. If there has been no {@link SecurityManager} established it will
     *    use the {@link Thread#getThreadGroup()} of the current thread, otherwise the {@link SecurityManager#getThreadGroup()}
     *    thread group.
     * </p>
     */
    public LoopThreadFactory(String name, int priority) {
        this.name = name;
        this.priority = priority;

        SecurityManager s = System.getSecurityManager();
        this.group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }


    @Override
    public Thread newThread(Runnable runnable) {

        Thread thread = new Thread(this.group, runnable, String.format(NAME_FORMAT, this.name, this.threadNumber.getAndIncrement()),0);

        // Note: All threads are user threads.
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        thread.setPriority(this.priority);

        // Note: All threads should have a NORMAL priority
        // @TODO Should the user be able to define the priority of each loop?
        // if (!(thread.getPriority() == Thread.NORM_PRIORITY)){
        //    thread.setPriority(Thread.NORM_PRIORITY);
        // }
        return thread;

    }

}
