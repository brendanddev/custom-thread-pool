package com.brendanddev.threadpool;

import com.brendanddev.threadpool.TaskQueue;

/**
 * An implementation of a WorkerThread that repeatedly pulls tasks from the shared TaskQueue and executes them.
 * 
 * Forms the core of a custom thread pool implementation. Each worker runs in its own thread, continuously calling
 * the 'dequeue()' method of the TaskQueue to obtain the next available Runnable task. If no tasks are available,
 * it blocks until one is enqueued.
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

    /**
     * The main execution loop of the worker thread.
     * Continuously dequeues tasks from the shared TaskQueue and executes them.
     * If the queue is empty, the thread will block until a task becomes available.
     */
    @Override
    public void run() { 
        try {
            while (running) {
                // Blocks if the queue is empty
                Runnable task = taskQueue.dequeue();
                // Execute the task
                task.run();
            }
        } catch (InterruptedException e) {
            // Thread was interrupted, exit gracefully
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stops the worker after completing the current task.
     */
    public void shutdown() { 
        running = false;
        this.interrupt();   // Wake up if blocked on dequeue

    }
    
}
