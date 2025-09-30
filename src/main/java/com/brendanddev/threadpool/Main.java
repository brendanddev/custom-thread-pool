package com.brendanddev.threadpool;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CustomThreadPool pool = new CustomThreadPool(3, 5);
        Random random = new Random();

        Thread monitor = new Thread(() -> {
            while (!pool.isShutdown()) {
                StringBuilder status = new StringBuilder("Pool status: Queue=" + pool.getQueueSize() + " | ");
                for (int i = 0; i < pool.getNumOfThreads(); i++) {
                    status.append("Worker-").append(i).append("=")
                            .append(pool.getWorkerThread(i).getState()).append(" | ");
                }
                System.out.println(status);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        monitor.setDaemon(true);
        monitor.start();

        for (int i = 0; i < 15; i++) {
            int taskId = i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " started task " + taskId);
                try {
                    Thread.sleep(500 + random.nextInt(1500));
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted!");
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " finished task " + taskId);
            });
            Thread.sleep(200);
        }

        Thread.sleep(5000);

        System.out.println("\nShutting down gracefully...");
        pool.shutdown();
        Thread.sleep(2000);

        System.out.println("\nShutting down immediately...");
        pool.shutdownNow();
    }
}
