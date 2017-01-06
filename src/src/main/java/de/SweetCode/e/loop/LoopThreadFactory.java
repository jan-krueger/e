package de.SweetCode.e.loop;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class LoopThreadFactory implements ThreadFactory {

    private final static String NAME_FORMAT = "e-loop-thread-%s-%d";

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public LoopThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        this.group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {

        // @TODO: Find a way to get the name of the loop thread....
        String name = !(runnable instanceof Loop) ? "default" : ((Loop) runnable).getName();

        Thread thread = new Thread(this.group, runnable, String.format(NAME_FORMAT, name, threadNumber.getAndIncrement()),0);
        // Note: All threads are user threads.
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        // Note: All threads should have a NORMAL priority
        if (!(thread.getPriority() == Thread.NORM_PRIORITY)){
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;

    }

}
