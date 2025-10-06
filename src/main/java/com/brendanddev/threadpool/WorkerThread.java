package com.brendanddev.threadpool;

import com.brendanddev.threadpool.TaskQueue;

/**
 * An implementation of a WorkerThread that repeatedly pulls tasks from the shared TaskQueue and executes them.
 */
public class WorkerThread extends Thread {

    private final TaskQueue taskQueue;
    private volatile boolean running = true;

    /**
     * Constructs a WorkerThread with the specified TaskQueue.
     * 
     * @param taskQueue The shared TaskQueue from which this worker will pull tasks.
     */
    public WorkerThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() { }

    public void shutdown() { }
    
}
