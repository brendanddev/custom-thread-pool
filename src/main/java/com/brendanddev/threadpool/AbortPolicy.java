package com.brendanddev.threadpool;

import com.brendanddev.threadpool.CustomRunnable;
import com.brendanddev.threadpool.CustomThreadPool;
import com.brendanddev.threadpool.RejectionPolicy;

import java.util.concurrent.RejectedExecutionException;

/**
 * A RejectionPolicy that immediately aborts a task submission by throwing a 
 * RejectedExecutionException when the thread pool is full.
 */
public class AbortPolicy implements RejectionPolicy {

    /**
     * Rejects the given task by throwing a RejectedExecutionException.
     * 
     * @param task The task that is being rejected.
     * @param pool The thread pool attempting to execute the task.
     * @throws RejectedExecutionException Always thrown to indicate task rejection.
     */
    @Override
    public void reject(CustomRunnable task, CustomThreadPool pool) {
        throw new RejectedExecutionException("Task " + task + " rejected from " + pool);
    }
    
}
