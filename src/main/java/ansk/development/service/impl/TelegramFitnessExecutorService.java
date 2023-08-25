package ansk.development.service.impl;

import ansk.development.service.api.ITelegramFitnessExecutorService;

import java.util.concurrent.*;

/**
 * Implementation of {@link ITelegramFitnessExecutorService}.
 *
 * @author Anton Skripin
 */
public class TelegramFitnessExecutorService implements ITelegramFitnessExecutorService {

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    private static ITelegramFitnessExecutorService telegramFitnessExecutorService;

    private TelegramFitnessExecutorService() {
    }

    public static ITelegramFitnessExecutorService executorService() {
        if (telegramFitnessExecutorService == null) {
            telegramFitnessExecutorService = new TelegramFitnessExecutorService();
        }

        return telegramFitnessExecutorService;
    }

    public Future<?> executeAsync(Runnable runnable) {
        return TelegramFitnessExecutorService.executorService.submit(runnable);
    }

    public void scheduleOnce(Runnable task, int initialDelay, TimeUnit timeUnit) {
        TelegramFitnessExecutorService.executorService.schedule(task, initialDelay, timeUnit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit timeUnit) {
        return TelegramFitnessExecutorService.executorService.scheduleWithFixedDelay(task, initialDelay, delay, timeUnit);
    }

}
