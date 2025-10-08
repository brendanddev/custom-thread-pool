package com.brendanddev.threadpool;

import java.util.List;
import com.brendanddev.threadpool.policies.RejectionHandlers;

/**
 * Demonstrates the usage of the CustomThreadPool by submitting multiple tasks and showing 
 * both graceful and immediate shutdown, aswell as examples of all rejection handling policies.
 */
public class Main {

    // Run: mvn clean compile exec:java
    public static void main(String[] args) {

        System.out.println("=== Graceful Shutdown ===");
        demoGracefulShutdown();

        System.out.println("\n=== Immediate Shutdown ===");
        demoImmediateShutdown();

        System.out.println("\n=== Rejection Policies ===");
        demoRejectionPolicies();
    }

    /**
     * Demonstrates a normal (graceful) shutdown where all tasks complete execution before the pool terminates.
     * THe pool enqueues poison pills to signal workers to stop after finishing their current work.
     */
    private static void demoGracefulShutdown() {
        CustomThreadPool threadPool = new CustomThreadPool(3);

        // Submit 10 simple tasks that simulate work
        for (int i = 0; i < 10; i++) {
            int taskId = i;
            threadPool.execute(() -> {
                System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("Task " + taskId + " completed on " + Thread.currentThread().getName());
            });
        }

        // Begin graceful shutdown
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
    }

    /**
     * Demonstrates an immediate shutdown where tasks are interrupted mid-execution.
     * The pool stops processing, interrupts worker threads, and returns any tasks that were not executed.
     */
    private static void demoImmediateShutdown() {
        CustomThreadPool threadPool2 = new CustomThreadPool(3);

        // Submit tasks that simulate longer running work
        for (int i = 0; i < 5; i++) {
            int taskId = i;
            threadPool2.execute(() -> {
                System.out.println("[Immediate] Task " + taskId + " running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("[Immediate] Task " + taskId + " interrupted!");
                    Thread.currentThread().interrupt();
                }
                System.out.println("[Immediate] Task " + taskId + " finished on " + Thread.currentThread().getName());
            });
        }

        // Interrupt workers immediately and retrieve remaining unexecuted tasks
        List<Runnable> remainingTasks = threadPool2.shutdownNow();
        System.out.println("Immediate shutdown called. Remaining tasks: " + remainingTasks.size());

        // Print any tasks that were still in the queue
        for (int i = 0; i < remainingTasks.size(); i++) {
            System.out.println("Unexecuted task at queue index: " + i);
        }
    }

    /**
     * Demonstrates the behavior of each built-in rejection policy.
     * This simulates a rejection scenario by attempting to execute tasks after shutdown, forcing the pool to 
     * invoke the configured RejectionHandler.
     */
    private static void demoRejectionPolicies() {
        int numThreads = 2;
        Runnable testTask = () -> System.out.println("Running test task on " + Thread.currentThread().getName());

        // Abort policy example
        CustomThreadPool abortPool = new CustomThreadPool(numThreads, new CustomThreadFactory("AbortWorker", false, Thread.NORM_PRIORITY), RejectionHandlers.ABORT_POLICY);
        abortPool.shutdown();
        System.out.println("-- ABORT POLICY --");
        abortPool.execute(testTask);

        // Discard policy example
        CustomThreadPool discardPool = new CustomThreadPool(numThreads, new CustomThreadFactory("DiscardWorker", false, Thread.NORM_PRIORITY), RejectionHandlers.DISCARD_POLICY);
        discardPool.shutdown();
        System.out.println("-- DISCARD POLICY --");
        discardPool.execute(testTask);

        // Caller-runs policy example
        CustomThreadPool callerRunsPool = new CustomThreadPool(numThreads, new CustomThreadFactory("CallerWorker", false, Thread.NORM_PRIORITY), RejectionHandlers.CALLER_RUNS_POLICY);
        callerRunsPool.shutdown();
        System.out.println("-- CALLER_RUNS POLICY --");
        callerRunsPool.execute(testTask);

        // Discard-oldest policy example
        CustomThreadPool discardOldestPool = new CustomThreadPool(numThreads, new CustomThreadFactory("OldestWorker", false, Thread.NORM_PRIORITY), RejectionHandlers.DISCARD_OLDEST_POLICY);
        discardOldestPool.shutdown();
        System.out.println("-- DISCARD_OLDEST POLICY --");
        discardOldestPool.execute(testTask);
    }
}
