package com.brendanddev.threadpool;

import java.util.List;

/**
 * Demonstrates the usage of the CustomThreadPool by submitting multiple tasks and showing 
 * both graceful and immediate shutdown.
 */
public class Main {

    public static void main(String[] args) {

        // Create a thread pool with 3 worker threads
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

        // Graceful shutdown, enqueues poison pills for each worker
        threadPool.shutdown();
        System.out.println("All tasks submitted. Thread pool is shutting down gracefully.");

        try {
            // Wait up to 5 seconds for all workers to finish
            boolean terminated = threadPool.awaitTermination(5000);
            System.out.println("Thread pool terminated: " + terminated);
        } catch (Exception e) {
            e.printStackTrace();
        }
}
