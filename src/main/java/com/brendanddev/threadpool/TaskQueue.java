package com.brendanddev.threadpool;

import java.util.LinkedList;

/**
 * A thread-safe FIFO queue for storing Runnable tasks in a custom thread pool.
 * 
 * All methods are synchronized to ensure that multiple threads can safely enqueue and dequeue tasks 
 * without race conditions.
 */
public class TaskQueue {

    private final LinkedList<Runnable> tasks;
    private final int capacity;

    /**
     * Constructs a TaskQueue with 'unlimited' capacity.
     */
    public TaskQueue() {
        this(Integer.MAX_VALUE);
    }
    
    /**
     * Constructs a TaskQueue with the specified capacity.
     */
    public TaskQueue(int capacity) {
        this.capacity = capacity;
        this.tasks = new LinkedList<>();
    }

    /**
     * Enqueue a task into the queue.
     * This method is synchronized to allow only one thread to modify the internal queue at a time.
     * 
     * @param task The Runnable task to be added to the queue.
     * @throws InterruptedException If the thread is interrupted while waiting to enqueue.
     */
    public synchronized void enqueue(Runnable task) throws InterruptedException {
        // Wait if the queue is full
        while (tasks.size() >= capacity) {
            wait(); // Releases lock and waits to be notified
        }
        
        // Add task at the end of the queue
        tasks.addLast(task);

        // Notify all waiting threads that a new task is available
        // Wakes up any worker thread waiiting in dequeue()
        notifyAll();
    }

    /**
     * Dequeue a task from the queue.
     * If the queue is empty, this method blocks until a task becomes available.
     * 
     * @return The Runnable task removed from the front of the queue.
     * @throws InterruptedException If the thread is interrupted while waiting to dequeue.
     */
    public synchronized Runnable dequeue() throws InterruptedException {
        // Wait while the queue is empty
        while (tasks.isEmpty()) {
            wait();
        }

        // Remove the first task from the queue
        Runnable task = tasks.removeFirst();

        // Notify any threads waiting in enqueue() that space may be available
        notifyAll();
        return task;
    }

    /**
     * Returns the current number of tasks in the queue.
     */
    public synchronized int size() { 
        return tasks.size();
    }
    
}
