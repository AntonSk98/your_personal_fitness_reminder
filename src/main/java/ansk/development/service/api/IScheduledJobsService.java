package ansk.development.service.api;

/**
 * Provides methods to manage scheduled jobs.
 *
 * @author Anton Skripin
 */
public interface IScheduledJobsService {

    void sendGreetingOnStartup();

    void checkBotSessionPeriodically();

    void sendFitnessReminderSchedule();

    boolean resetFitnessReminderTimer(String chatId);
}
