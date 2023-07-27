package ansk.development.service;

import java.util.concurrent.*;

/**
 * Schedule executor service encapsulates to serve the necessary scheduled jobs for this bot.
 *
 * @author Anton Skripin
 */
public class TelegramFitnessExecutorService {

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    public static Future<?> executeAsync(Runnable runnable) {
        return executorService.submit(runnable);
    }

    public static void scheduleOnce(Runnable task, int initialDelay, TimeUnit timeUnit) {
        executorService.schedule(task, initialDelay, timeUnit);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit timeUnit) {
        return executorService.scheduleWithFixedDelay(task, initialDelay, delay, timeUnit);
    }
}
