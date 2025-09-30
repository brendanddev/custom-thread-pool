package com.brendanddev.threadpool;

import com.brendanddev.threadpool.CustomRunnable;
import com.brendanddev.threadpool.CustomThreadPool;
import com.brendanddev.threadpool.RejectionPolicy;

/**
 * A RejectionPolicy that runs the rejected task in the calling thread.
 * 
 * This ensures that the task is not discarded, but it can slow down the caller if the pool is full.
 */
public class CallerRunsPolicy implements RejectionPolicy {

    /**
     * Executes the rejected task in the calling thread.
     * 
     * @param task The task that is being rejected.
     * @param pool The thread pool attempting to execute the task.
     */
    @Override
    public void reject(CustomRunnable task, CustomThreadPool pool) {
        task.run();
    }
    
}
