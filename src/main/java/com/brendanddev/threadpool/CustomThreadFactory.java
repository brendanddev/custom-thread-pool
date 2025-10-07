package com.brendanddev.threadpool;


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

    public void newThread() { }
    
}
