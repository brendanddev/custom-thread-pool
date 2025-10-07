package com.brendanddev.threadpool;

import java.util.ArrayList;
import java.util.List;

import com.brendanddev.threadpool.CustomThreadFactory;
import com.brendanddev.threadpool.policies.RejectionHandler;

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
    private final Thread[] workerThreads;
    private volatile boolean isShutdown = false;
    private volatile boolean isTerminated = false;

    private final CustomThreadFactory threadFactory;
    private final RejectionHandler rejectionHandler;

    // Special task to signal workers to terminate
    public static final Runnable POISON_PILL = () -> {};

    /**
     * Constructs a CustomThreadPool with a given number of worker threads and a custom thread factory.
     * 
     * @param numThreads The number of worker threads in the pool.
     * @param factory The CustomThreadFactory to create worker threads.
     */
    public CustomThreadPool(int numThreads, CustomThreadFactory factory, RejectionHandler handler) {
        this.threadFactory = factory;
        this.rejectionHandler = handler;
        taskQueue = new TaskQueue();
        workers = new WorkerThread[numThreads];
        workerThreads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            WorkerThread worker = new WorkerThread(taskQueue);
            workers[i] = worker;
            Thread t = threadFactory.newThread(worker);
            workerThreads[i] = t;
            t.start();
        }
    }

    /**
     * Constructs a CustomThreadPool with a given number of worker threads and a default thread factory.
     * 
     * @param numThreads The number of worker threads in the pool.
     */
    public CustomThreadPool(int numThreads) {
        this(numThreads, new CustomThreadFactory("Worker", false, Thread.NORM_PRIORITY), RejectionHandler.ABORT_POLICY);
    }


    /**
     * Submits a Runnable task for execution.
     * Adds the task to the shared TaskQueue if the pool is active.
     * 
     * @param task The Runnable task to be executed.
     */
    public void execute(Runnable task) {
        if (isShutdown) {
            rejectionHandler.reject(task, this);
            return;
        }
        try {
            taskQueue.enqueue(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            rejectionHandler.reject(task, this);
        }
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

        for (Thread t : workerThreads) {
            t.interrupt();
        }

        // Interrupt all worker threads to stop them immediately
        // for (WorkerThread worker : workers) {
        //     worker.shutdown();
        // }

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

        for (Thread t : workerThreads) {
            long remaining = endTime - System.currentTimeMillis();
            if (remaining <= 0) return false;   // Timed out before all threads

            try {
                t.join(remaining);
            } catch (InterruptedException e) {
                // Preserve interrupt status and exit early if interrupted
                Thread.currentThread().interrupt();
                return false;
            }
        }

        isTerminated = true;
        return true;



        // Go through each worker and wait for it to finish
        // for (WorkerThread worker : workers) {
        //     long remaining = endTime - System.currentTimeMillis();
        //     if (remaining <= 0) return false;   // Timed out before all threads finished

        //     // Wait up to 'remaining' milliseconds for this worker to finish
        //     try {
        //         worker.join(remaining);
        //     } catch (InterruptedException e) {
        //         // Preserve interrupt status and exit early if interrupted
        //         Thread.currentThread().interrupt();
        //     }
        // }
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
