package com.brendanddev.threadpool;

import com.brendanddev.threadpool.BlockingQueue;
import com.brendanddev.threadpool.CustomRunnable;
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

    /**
     * Submits a task to the thread pool, blocking if the task queue is full.
     * 
     * @param task The CustomRunnable task to execute.
     * @throws InterruptedException If the thread is interrupted while waiting to add the task.
     * @throws IllegalStateException If the pool has been shut down.
     */
    public void execute(CustomRunnable task) throws InterruptedException {
        if (isShutdown) {
            throw new IllegalStateException("Thread pool is shutdown, cannot accept new tasks");
        }
        taskQueue.put(task);
    }

    /**
     * Shuts down the thread pool gracefully.
     * Current tasks will finish, and worker threads will stop afterwards.
     */
    public void shutdown() { 
        isShutdown = true;

        for (Worker worker : workers) worker.stopWorker();
        for (Thread t : workerThreads) t.interrupt();
    }

    /**
     * Immediately shuts down the thread pool.
     * Worker threads stop as soon as possible, and remaining tasks in the queue are discarded.
     */
    public void shutdownNow() {
        isShutdown = true;

        for (Worker worker : workers) worker.stopWorker();

        synchronized (taskQueue) {
            while (taskQueue.size() > 0) {
                try {
                    taskQueue.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        for (Thread t : workerThreads) t.interrupt();
    }

    /**
     * Returns the number of tasks waiting in the pool's task queue.
     */
    public int getQueueSize() {
        return taskQueue.size();
    }

    public int getNumOfThreads() {
        return numOfThreads;
    }

    public Thread getWorkerThread(int index) {
        if (index < 0 || index >= workerThreads.length) {
            throw new IllegalArgumentException("Invalid worker index: " + index);
        }
        return workerThreads[index];
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    
}
