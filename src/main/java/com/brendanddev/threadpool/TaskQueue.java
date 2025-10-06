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


    public synchronized void enqueue() { }
    public synchronized Runnable dequeue() { }
    public synchronized int size() { }
    
}
