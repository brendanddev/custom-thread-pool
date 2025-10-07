package com.brendanddev.threadpool.policies;

import com.brendanddev.threadpool.CustomThreadPool;

/**
 * Defines a strategy for handling tasks that cannot be executed by the thread pool.
 * Allows for custom behaviour when the task queue is fill and no threads are available to execute new tasks.
 */
public interface RejectionHandler {

    /**
     * Handles a task that has been rejected by the thread pool.
     * 
     * @param task The Runnable task that was rejected.
     * @param pool The CustomThreadPool instance that rejected the task.
     */
    void reject(Runnable task, CustomThreadPool pool);
    
}
