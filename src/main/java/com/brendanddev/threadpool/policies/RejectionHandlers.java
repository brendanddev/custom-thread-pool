package com.brendanddev.threadpool.policies;

import com.brendanddev.threadpool.CustomThreadPool;
import com.brendanddev.threadpool.RejectionHandler;

/**
 * Provides built-in implementations of common rejection handling policies.
 * Each constants defines a different strategy for what to do when a task cannot be accepted
 * by the thread pool.
 */
public class RejectionHandlers {

    /**
     * A policy that throws a RuntimeException when a task is rejected.
     */
    public static final RejectionHandler ABORT_POLICY = (task, pool) -> {
        throw new RuntimeException("[ABORT] - Task rejected from " + pool);
    };

    /**
     * A policy that silently (only prints) discards the rejected task.
     */
    public static final RejectionHandler DISCARD_POLICY = (task, pool) -> {
        System.out.println("[DISCARD] - Task discarded from " + pool);
    };

    /**
     * A policy that runs the rejected task in the caller's thread.
     */
    public static final RejectionHandler CALLER_RUNS_POLICY = (task, pool) -> {
        System.out.println("[CALLER_RUNS] - Running task in caller thread from " + pool);
        task.run();
    };

    /**
     * Discards the oldest queued task to make room for the new task.
     */
    public static final RejectionHandler DISCARD_OLDEST_POLICY = (task, pool) -> {
        Runnable oldest = pool.getQueue().poll();
        if (oldest != null) {
            System.out.println("[DISCARD_OLDEST] - Discarded oldest task from " + pool);
        }
        pool.execute(task);
    };


    
}
