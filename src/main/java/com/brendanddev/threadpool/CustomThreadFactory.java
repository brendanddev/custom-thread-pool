package com.brendanddev.threadpool;

/**
 * A custom implementation of a thread factory that creates threads with specific properties.
 * This allows for better control over thread naming, daemon status, and priority.
 * 
 * The daemon status of threads determines whether the JVM can exit while the threads are running, and
 * the priority influences the thread scheduling by the JVM.
 */
public class CustomThreadFactory {

    private final String basename;
    private int counter = 0;
    private final boolean daemon;
    private final int priority;

    /**
     * Constructs a CustomThreadFactory with the specified base name, daemon status, and priority.
     * 
     * @param basename The prefix for thread names created by this factory.
     * @param daemon Whether threads should be daemon threads.
     * @param priority The priority for threads created by this factory (between Thread.MIN_PRIORITY and Thread.MAX_PRIORITY).
     */
    public CustomThreadFactory(String basename, boolean daemon, int priority) {
        this.basename = basename;
        this.daemon = daemon;
        this.priority = priority;
    }

    /**
     * Creates a new thread with the specified Runnable task.
     * 
     * @param r The Runnable task for the new thread to execute.
     */
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, basename + "-" + counter++);
        t.setDaemon(daemon);
        t.setPriority(priority);
        return t;
    }
    
}
