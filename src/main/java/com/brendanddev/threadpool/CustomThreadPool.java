package com.brendanddev.threadpool;



public class CustomThreadPool {

    private final TaskQueue taskQueue;
    private final WorkerThread[] workers;
    private volatile boolean isShutdown = false;

    /**
     * Constructs a CustomThreadPool with a given number of worker threads.
     * 
     * @param numThreads The number of worker threads in the pool.
     */
    public CustomThreadPool(int numThreads) {
        taskQueue = new TaskQueue();
        workers = new WorkerThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            workers[i] = new WorkerThread(taskQueue);
            workers[i].start();
        }
    }

    public void execute() { }
    public void shutdown() { }
    
}
