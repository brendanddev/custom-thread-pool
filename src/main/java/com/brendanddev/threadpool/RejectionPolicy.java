package com.brendanddev.threadpool;

import com.brendanddev.threadpool.CustomRunnable;
import com.brendanddev.threadpool.CustomThreadPool;

/**
 * Defines a policy for handling tasks that cannot be accepted by a thread pool.
 * 
 * When a CustomThreadPool is full, all worker threads are busy and the task queue is at
 * capacity. A RejectionPolicy determines what happens to a newly submitted task.
 */
public interface RejectionPolicy {

    /**
     * Handles a task that cannot be accepted by the pool.
     * 
     * @param task The task that is being rejected.
     * @param pool The thread pool attempting to execute the task.
     */
    void reject(CustomRunnable task, CustomThreadPool pool);
    
}
