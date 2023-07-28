package ansk.development.service.api;

import ansk.development.domain.FitnessBotCommands;

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
