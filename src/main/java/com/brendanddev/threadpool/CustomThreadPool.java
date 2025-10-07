package com.brendanddev.threadpool;

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
    private static final Runnable POISON_PILL = () -> {};

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
     * Gracefully shuts down all worker threads.
     * Allows ongoing tasks to complete before stopping the workers.
     */
    public void shutdown() { 
        isShutdown = true;
        for (WorkerThread worker : workers) {
            worker.shutdown();
        }
    }

    /**
     * Checks if the thread pool has been shut down.
     */
    public boolean isTerminated() {
        return isTerminated;
    }

    
}
