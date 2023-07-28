package ansk.development.repository;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.repository.api.IScheduledJobsRepository;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * Implementation of {@link IScheduledJobsRepository}.
 *
 * @author Anton Skripin
 */
public class ScheduledJobsRepository implements IScheduledJobsRepository {

    private static ScheduledJobsRepository scheduledJobsRepository;
    private final ScheduledJob scheduledJob;

    private ScheduledJobsRepository() {
        this.scheduledJob = new ScheduledJob(ConfigRegistry.props().botCredentials().getChatId());
    }

    public static ScheduledJobsRepository getRepository() {
        if (scheduledJobsRepository == null) {
            scheduledJobsRepository = new ScheduledJobsRepository();
        }

        return scheduledJobsRepository;
    }

    @Override
    public void addScheduledJobForUser(String chatId, ScheduledFuture<?> scheduledFuture) {
        scheduledJob.addScheduledJob(chatId, scheduledFuture);
    }

    @Override
    public void removeScheduledJobForUser(String chatId) {
        scheduledJob.removeScheduledJob(chatId);
    }

    @Override
    public boolean existsRunningJobForUser(String chatId) {
        return scheduledJob.existsRunningJobForUser(chatId);
    }

    private class ScheduledJob {
        private String chatId;
        private ScheduledFuture<?> scheduledFuture;

        public ScheduledJob(String chatId) {
            this.chatId = chatId;
        }

        public void addScheduledJob(String chatId, ScheduledFuture<?> scheduledFuture) {
            if (!this.chatId.equals(chatId)) {
                throw new IllegalStateException();
            }
            this.scheduledFuture = scheduledFuture;
        }

        public void removeScheduledJob(String chatId) {
            if (!this.chatId.equals(chatId)) {
                throw new IllegalStateException();
            }
            this.scheduledFuture = null;
        }

        public boolean existsRunningJobForUser(String chatId) {
            return this.chatId.equals(chatId) && Objects.nonNull(this.scheduledFuture);
        }
    }
}
