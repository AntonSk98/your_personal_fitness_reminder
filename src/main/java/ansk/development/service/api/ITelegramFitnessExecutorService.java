package ansk.development.service.api;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Schedule executor service encapsulates to serve the necessary scheduled jobs for this bot.
 *
 * @author Anton Skripin
 */
public interface ITelegramFitnessExecutorService {
    Future<?> executeAsync(Runnable runnable);

    void scheduleOnce(Runnable task, int initialDelay, TimeUnit timeUnit);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit timeUnit);
}
