package com.brendanddev.threadpool;

import com.brendanddev.threadpool.CustomThreadPool;

/**
 * Demonstrates the usage of the CustomThreadPool by submitting multiple tasks that run concurrently.
 */
public class Main {

    public static void main(String[] args) {

        // Creates a thread pool with 3 worker threads
        CustomThreadPool threadPool = new CustomThreadPool(3);
        
        // Submit 10 tasks to the thread pool
        for (int i = 0; i < 10; i++) {
            int taskId = i;
            try {
                threadPool.execute(() -> {
                    System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                    try {
                        // Simulate workers doing some work
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Task " + taskId + " completed on " + Thread.currentThread().getName());
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Shutdown the thread pool after all tasks are submitted
        threadPool.shutdown();
        System.out.println("All tasks submitted. Thread pool is shutting down.");
    }
    
}
