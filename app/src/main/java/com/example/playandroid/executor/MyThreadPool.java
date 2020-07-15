package com.example.playandroid.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPool {
    private static final int POOL_SIZE = 8;
    private static ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

    public static void execute(Runnable task) {
        executorService.execute(task);
    }

}
