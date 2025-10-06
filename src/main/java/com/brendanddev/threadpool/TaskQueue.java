package com.brendanddev.threadpool;

import java.util.LinkedList;

/**
 * A thread-safe FIFO queue for storing Runnable tasks in a custom thread pool.
 * 
 * All methods are synchronized to ensure that multiple threads can safely enqueue and dequeue tasks 
 * without race conditions.
 * 
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


    public synchronized Runnable dequeue() {
    }

    public synchronized int size() { }
    
}
