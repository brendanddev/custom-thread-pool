package com.brendanddev.threadpool.policies;

import com.brendanddev.threadpool.policies.RejectionHandler;

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
    
}
