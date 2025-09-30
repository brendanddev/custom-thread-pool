package com.brendanddev.threadpool;

import com.brendanddev.threadpool.CustomThreadPool;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CustomThreadPool pool = new CustomThreadPool(3, 5);

        for (int i = 0; i <= 10; i++) {
            int taskId = i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " executing task " + taskId);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted!");
                    Thread.currentThread().interrupt();
                }
            });
        }

        System.out.println("Tasks waiting in queue: " + pool.getQueueSize());
        Thread.sleep(3000);

        System.out.println("Shutting down gracefully...");
        pool.shutdown();

        Thread.sleep(2000);
        System.out.println("Shutting down immediately...");
        pool.shutdownNow();
        
    }
    
}
