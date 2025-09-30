package com.brendanddev.threadpool;

import com.brendanddev.threadpool.BlockingQueue;
import com.brendanddev.threadpool.Worker;

/**
 * A custom implementation of a Thread Pool that manages a fixed number of worker threads and
 * a shared task queue. Tasks submitted to the pool are stored in a BlockingQueue and executed
 * by worker threads concurrently.
 */
public class CustomThreadPool {

    private final int numOfThreads;
    private final Worker[] workers;
    private final Thread[] workerThreads;
    private final BlockingQueue taskQueue;
    private volatile boolean isShutdown = false;

    /**
     * Constructs a new CustomThreadPool with a fixed number of worker threads and a specified
     * task queue capacity.
     * 
     * @param numOfThreads The number of worker threads in the pool.
     * @param queueCapacity The maximum number of tasks the queue can hold.
     */
    public CustomThreadPool(int numOfThreads, int queueCapacity) {
        this.numOfThreads = numOfThreads;
        this.taskQueue = new BlockingQueue(queueCapacity);
        this.workers = new Worker[numOfThreads];
        this.workerThreads = new Thread[numOfThreads];

        for (int i = 0; i < numOfThreads; i++) {
            workers[i] = new Worker(taskQueue);
            workerThreads[i] = new Thread(workers[i], "Worker-" + i);
            workerThreads[i].start();
        }
    }

    public void execute() { }
    public void shutdown() { }
    
    
}
