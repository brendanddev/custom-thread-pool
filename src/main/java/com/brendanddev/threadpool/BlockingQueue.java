package com.brendanddev.threadpool;

import java.util.LinkedList;


public class BlockingQueue {

    private final List<CustomRunnable> queue;
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }
    
}
