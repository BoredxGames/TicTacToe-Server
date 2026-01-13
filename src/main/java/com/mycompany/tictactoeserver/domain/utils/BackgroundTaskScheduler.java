package com.mycompany.tictactoeserver.domain.utils;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackgroundTaskScheduler {

    private ScheduledExecutorService scheduler;
    private Runnable task;

    public BackgroundTaskScheduler(Runnable task) {
        this.task = task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public void startTask(int period, TimeUnit unit) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(
                this::safeRun,
                0,
                period,
                unit
        );

        System.out.println("Background Timer task started.");
    }

    public void stopTask() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
            System.out.println("Background Timer task stopped.");
        }
    }

    private void safeRun() {
        try {
            task.run();
            System.out.println("Background Timer task Executed!");
        } catch (Exception e) {
            System.err.println("Error in background task: " + e.getMessage());
            e.printStackTrace();
        }
    }
}