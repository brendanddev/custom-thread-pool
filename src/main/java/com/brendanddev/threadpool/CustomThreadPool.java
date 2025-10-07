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
     * Checks if the thread pool has been shut down.
     */
    public boolean isTerminated() {
        return isTerminated;
    }

    
}
