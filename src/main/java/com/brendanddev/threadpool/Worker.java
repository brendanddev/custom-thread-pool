package com.brendanddev.threadpool;

import com.brendanddev.threadpool.BlockingQueue;

/**
 * This Worker class represents a single thread in the thread pool that continuously 
 * takes tasks from the BlockingQueue and executes them.
 * 
 * It stops gracefull when requested by the thread pool.
 */
public class Worker implements Runnable {

    private final BlockingQueue taskQueue;
    private volatile boolean isStopped = false;

    /**
     * Constructs a Worker instance with the given task queue.
     * 
     * @param taskQueue The queue from which this worker will take tasks.
     */
    public Worker(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() { }
    public void stopWorker() { }
    
}
