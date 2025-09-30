package com.brendanddev.threadpool;

import com.brendanddev.threadpool.BlockingQueue;
import com.brendanddev.threadpool.CustomRunnable;

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
    
    /**
     * Continuously takes tasks from the queue and executes them.
     * Blocks if the queue is empty, and stops when 'stopWorker()' is called.
     */
    @Override
    public void run() {
        while (!isStopped) {
            try {
                // Take task from queue and execute it
                CustomRunnable task = taskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                // Restore interrupted status and exit gracefully if interrupted
                Thread.currentThread().interrupt();
            }
        }
    }


    public void stopWorker() { }
    
}
