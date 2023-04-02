package ansk.development.repository.api;

import java.util.concurrent.ScheduledFuture;

/**
 * Collects a list of all scheduled jobs for a user.
 *
 * @author Anton Skripin
 */
public interface IScheduledJobsRepository {

    void addScheduledJobForUser(String chatId, ScheduledFuture<?> scheduledFuture);

    void removeScheduledJobForUser(String chatId);

    boolean existsRunningJobForUser(String chatId);
}
