package ansk.development.service;

import ansk.development.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Schedule executor method that sends a reminder to a user to do some exercises.
 * If an error occurs due to request overflow, this job will schedule one-time job.
 */
public class TelegramFitnessExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramFitnessExecutorService.class);

    public static void scheduleWithRetry(Runnable task, long initialDelay, long delay, TimeUnit timeUnit) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> {
            try {
                LOGGER.info("Trying to send notification to the client to do some exercises");
                task.run();
                LOGGER.info("Client is successfully notified!");
            } catch (Exception e) {
                processExceptionWithRetry(executorService, task, e);
            }
        }, initialDelay, delay, timeUnit);
    }

    public static void scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit timeUnit) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(task, initialDelay, delay, timeUnit);
    }

    private static void processExceptionWithRetry(ScheduledExecutorService executor, Runnable task, Exception e) {
        try {
            LOGGER.error("The following exception occurred: {}. I will apply a retry mechanism.", e.getMessage(), e);
            if (ExceptionUtils.isTooManyRequestException(e)) {
                long timeToRetry = ExceptionUtils.getTimeToRetry(e);
                LOGGER.error("Error occurred because too many requests were sent. I will retry one more time in {} seconds", timeToRetry);
                executor.schedule(task, timeToRetry, TimeUnit.SECONDS);
            } else {
                LOGGER.error("While sending notification to the client the following error occurred: {}", e.getMessage(), e);
            }
        } catch (Exception unexpectedException) {
            LOGGER.error("Unexpected error occurred while processing exception.", unexpectedException);
        }

    }
}
