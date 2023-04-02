package ansk.development.repository;

import ansk.development.repository.api.IScheduledJobsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Implementation of {@link IScheduledJobsRepository}.
 *
 * @author Anton Skripin
 */
public class ScheduledJobsRepository implements IScheduledJobsRepository {

    private static ScheduledJobsRepository scheduledJobsRepository;

    private ScheduledJobsRepository() {
    }

    public static ScheduledJobsRepository getRepository() {
        if (scheduledJobsRepository == null) {
            scheduledJobsRepository = new ScheduledJobsRepository();
        }

        return scheduledJobsRepository;
    }

    private final List<ScheduledJob> SCHEDULED_JOBS_REGISTRY = new ArrayList<>();

    @Override
    public void addScheduledJobForUser(String chatId, ScheduledFuture<?> scheduledFuture) {
        this.SCHEDULED_JOBS_REGISTRY.add(new ScheduledJob(chatId, scheduledFuture));
    }

    @Override
    public void removeScheduledJobForUser(String chatId) {
        this.SCHEDULED_JOBS_REGISTRY.removeIf(scheduledJob -> {
            if (scheduledJob.getChatId().equals(chatId)) {
                scheduledJob.getScheduledFuture().cancel(true);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean existsRunningJobForUser(String chatId) {
        return this.SCHEDULED_JOBS_REGISTRY
                .stream()
                .filter(scheduledJob -> scheduledJob.getChatId().equals(chatId))
                .anyMatch(scheduledJob -> !scheduledJob.getScheduledFuture().isDone());
    }

    private class ScheduledJob {
        private final String chatId;
        private final ScheduledFuture<?> scheduledFuture;

        private ScheduledJob(String chatId, ScheduledFuture<?> scheduledFuture) {
            this.chatId = chatId;
            this.scheduledFuture = scheduledFuture;
        }

        public String getChatId() {
            return chatId;
        }

        public ScheduledFuture<?> getScheduledFuture() {
            return scheduledFuture;
        }
    }
}
