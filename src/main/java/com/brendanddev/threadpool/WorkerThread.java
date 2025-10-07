package com.brendanddev.threadpool;

import java.util.concurrent.atomic.AtomicInteger;

import com.brendanddev.threadpool.CustomThreadPool;
import com.brendanddev.threadpool.TaskQueue;

/**
 * An implementation of a WorkerThread that repeatedly pulls tasks from the shared TaskQueue and executes them.
 * 
 * Forms the core of a custom thread pool implementation. Each worker runs in its own thread, continuously calling
 * the 'dequeue()' method of the TaskQueue to obtain the next available Runnable task. If no tasks are available,
 * it blocks until one is enqueued.
 */
public class WorkerThread implements Runnable {

    private final TaskQueue taskQueue;
    private volatile boolean running = true;

    private static final Runnable POISON_PILL = CustomThreadPool.POISON_PILL;
    public static final AtomicInteger workerCount = new AtomicInteger(0);
    public static final AtomicInteger completedTaskCount = new AtomicInteger(0);

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

                // If task is the poison pill, exit loop gracefully
                if (task == POISON_PILL) break;

                workerCount.incrementAndGet();

                // Execute the task safely
                try {
                    task.run();
                } catch (Exception e) {
                    // Prevent worker from dying, log the exception
                    System.err.println("Task threw an exception: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Task finished (either normally or exceptionally)
                    completedTaskCount.incrementAndGet();
                    workerCount.decrementAndGet();
                    printStats();
                }
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
    }

    /**
     * Prints the current state of the worker thread and internal task queue.
     */
    public void printStats() {
        System.out.println("Queue size: " + taskQueue.size() +
                        ", Active workers: " + workerCount.get() +
                        ", Completed tasks: " + completedTaskCount.get());
    }
    
}
