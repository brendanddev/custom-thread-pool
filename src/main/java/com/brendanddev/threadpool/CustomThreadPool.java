package com.brendanddev.threadpool;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom implementation of a fixed-size thread pool that manages a group of worker threads to execute
 * submitted tasks concurrently.
 * 
 * This class coordinates task execution using a shared TaskQueue and a fixed number of WorkerThreads. 
 * Tasks submitted through the `execute(Runnable)` method are enqueued and processed by available worker threads.
 */
public class CustomThreadPool {

    private final TaskQueue taskQueue;
    private final WorkerThread[] workers;
    private volatile boolean isShutdown = false;
    private volatile boolean isTerminated = false;

    // Special task to signal workers to terminate
    public static final Runnable POISON_PILL = () -> {};

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

    /**
     * Submits a Runnable task for execution.
     * Adds the task to the shared TaskQueue if the pool is active.
     * 
     * @param task The Runnable task to be executed.
     * @throws InterruptedException If the thread is interrupted while waiting to enqueue.
     */
    public void execute(Runnable task) throws InterruptedException {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shutting down; cannot accept new tasks.");
        }
        taskQueue.enqueue(task);
    }

    /**
     * Initiates a graceful shutdown of the thread pool.
     * New tasks are rejected, but existing and queued tasks will finish.
     */
    public void shutdown() { 
        isShutdown = true;
        for (int i = 0; i < workers.length; i++) {
            try {
                // Enqueue a poison pill for each worker to signal termination
                taskQueue.enqueue(POISON_PILL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Attempts to stop all actively executing tasks and returns tasks that were not executed.
     * This interrupts all worker threads immediately.
     * 
     * @return A list of tasks that were submitted but not yet executed.
     */
    public List<Runnable> shutdownNow() {
        isShutdown = true;
        List<Runnable> remainingTasks = new ArrayList<>();

        // Synchronize on the TaskQueue to safely access its internal state
        // Ensures no worker thread is dequeuing tasks while we are iterating or removing tasks
        synchronized (taskQueue) {
            while (taskQueue.size() > 0) {
                try {
                    remainingTasks.add(taskQueue.dequeue());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Interrupt all worker threads to stop them immediately
        for (WorkerThread worker : workers) {
            worker.shutdown();
        }

        // Mark pool as terminated
        isTerminated = true;
        return remainingTasks;
    }

    /**
     * Blocks until all worker threads terminate or the timeout expires.
     * 
     * @param timeoutMillis The maximum time to wait in milliseconds.
     * @return true if all workers terminated, false if the timeout elapsed first.
     */
    public boolean awaitTermination(long timeoutMillis) {
        long endTime = System.currentTimeMillis() + timeoutMillis;
        // Go through each worker and wait for it to finish
        for (WorkerThread worker : workers) {
            long remaining = endTime - System.currentTimeMillis();
            if (remaining <= 0) return false;   // Timed out before all threads finished

            // Wait up to 'remaining' milliseconds for this worker to finish
            try {
                worker.join(remaining);
            } catch (InterruptedException e) {
                // Preserve interrupt status and exit early if interrupted
                Thread.currentThread().interrupt();
            }
        }
        isTerminated = true;
        return true;
    }

    /**
     * Checks if the thread pool has been shut down.
     */
    public boolean isTerminated() {
        return isTerminated;
    }

    /**
     * Returns the current number of tasks in the queue.
     */
    public int getQueueSize() {
        return taskQueue.size();
    }

    /**
     * Returns the number of active worker threads currently executing tasks.
     */
    public int getActiveWorkerCount() {
        return WorkerThread.workerCount.get();
    }

    /**
     * Returns the total number of tasks that have been completed by the pool.
     */
    public int getCompletedTaskCount() {
        return WorkerThread.completedTaskCount.get();
    }

    
}
