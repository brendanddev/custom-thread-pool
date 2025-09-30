package com.brendanddev.threadpool;

import java.util.LinkedList;
import java.util.List;

import com.brendanddev.threadpool.CustomRunnable;

/**
 * A thread-safe queue for storing tasks in a thread pool.
 * It blocks producers when the queue is full and blocks consumers when the queue is empty.
 */
public class BlockingQueue {

    private final List<CustomRunnable> queue;
    private final int capacity;

    /**
     * Constructs a new instance of a BlockingQueue with the specified capacity.
     */
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    /**
     * Adds a task to the queue. If the queue is full, the calling thread will wait until
     * space becomes available.
     * 
     * @param task The task to add.
     * @throws InterruptedException If the thread is interruped while waiting.
     */
    public synchronized void put(CustomRunnable task) throws InterruptedException {
        while (queue.size() >= capacity) {
            wait();
        }
        queue.add(task);
        notifyAll();
    }
    
    /**
     * Removes and returns a task from the queue.
     * If the queue is empty, the calling thread will wait until a task becomes available.
     * 
     * @return The next task from the queue.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized CustomRunnable take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        CustomRunnable task = queue.remove(0);
        notifyAll();
        return task;
    }

    /**
     * Returns the current number of tasks in the queue.
     */
    public synchronized int size() {
        return queue.size();
    }
    
}
