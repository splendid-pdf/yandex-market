package com.marketplace.workflow.core.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class PoolConfig {
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR =
            new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

    public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
}
