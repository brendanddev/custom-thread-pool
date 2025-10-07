package com.brendanddev.threadpool;

import java.util.List;

/**
 * Demonstrates the usage of the CustomThreadPool by submitting multiple tasks and showing 
 * both graceful and immediate shutdown.
 */
public class Main {

    // Run: mvn clean compile exec:java
    public static void main(String[] args) {

        // Create a thread pool with 3 worker threads
        CustomThreadPool threadPool = new CustomThreadPool(3);

        // Submit 10 tasks to the thread pool
        for (int i = 0; i < 10; i++) {
            int taskId = i;
            try {
                threadPool.execute(() -> {
                    System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                    // Simulate workers doing some work
                    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
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
            System.out.println("Final completed tasks: " + threadPool.getCompletedTaskCount());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Immediate shutdown
        CustomThreadPool threadPool2 = new CustomThreadPool(3);

        // Submit 5 tasks
        for (int i = 0; i < 5; i++) {
            int taskId = i;
            try {
                threadPool2.execute(() -> {
                    System.out.println("[Immediate] Task " + taskId + " running on " + Thread.currentThread().getName());
                    try {
                        // Longer work to demo interrupt
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("[Immediate] Task " + taskId + " interrupted!");
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("[Immediate] Task " + taskId + " finished on " + Thread.currentThread().getName());
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Immediate shutdown enqueues poison pills and interrupts workers
        List<Runnable> remainingTasks = threadPool2.shutdownNow();
        System.out.println("Immediate shutdown called. Remaining tasks: " + remainingTasks.size());
        
        // Print tasks that were not executed
        for (int i = 0; i < remainingTasks.size(); i++) {
            System.out.println("Unexecuted task at queue index: " + i);
        }
    }
}
