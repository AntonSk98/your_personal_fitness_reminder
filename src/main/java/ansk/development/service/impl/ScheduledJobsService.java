package ansk.development.service.impl;

import ansk.development.bot.FitnessReminderBot;
import ansk.development.configuration.ConfigRegistry;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.NotificationsRepository;
import ansk.development.repository.ScheduledJobsRepository;
import ansk.development.repository.api.IScheduledJobsRepository;
import ansk.development.service.api.IScheduledJobsService;
import ansk.development.service.methods.MessageMethod;
import ansk.development.service.methods.TelegramFitnessExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link IScheduledJobsRepository}
 *
 * @author Anton Skripin
 */
public class ScheduledJobsService implements IScheduledJobsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IScheduledJobsService.class);

    private static ScheduledJobsService scheduledJobsService;

    private ScheduledJobsService() {
    }

    public static ScheduledJobsService scheduledJobsService() {
        if (scheduledJobsService == null) {
            scheduledJobsService = new ScheduledJobsService();
        }

        return scheduledJobsService;
    }

    private static long calculateInitialDelay() {
        ZoneId zoneId = ZoneId.of(ConfigRegistry.props().botProperties().getTimezone());

        ZonedDateTime now = toZonedTime(LocalTime.now(), ZoneId.systemDefault());

        ZonedDateTime startAt = toZonedTime(ConfigRegistry
                .props()
                .scheduledJobs()
                .getNoReminders()
                .getTo(), zoneId)
                .withZoneSameInstant(ZoneId.systemDefault());

        long sendFitnessReminderIntervalInSeconds = ConfigRegistry
                .props()
                .scheduledJobs()
                .getSendWorkout()
                .getInterval();

        if (now.isBefore(startAt)) {
            return startAt.toEpochSecond() - now.toEpochSecond();
        }

        if (isTimeWithinNoReminderRange(now.plusSeconds(sendFitnessReminderIntervalInSeconds))) {
            return startAt.plusDays(1).toEpochSecond() - now.toEpochSecond();
        }

        // in this case calculate normal delay
        for (int iteration = 1; ; iteration++) {
            ZonedDateTime nextReminder = startAt.plusSeconds(iteration * sendFitnessReminderIntervalInSeconds);
            if (now.isBefore(nextReminder)) {
                return nextReminder.toEpochSecond() - now.toEpochSecond();
            }
        }
    }

    private static boolean areNotificationsDisabled(String chatId) {
        return !NotificationsRepository
                .getRepository()
                .areNotificationsEnabled(chatId);
    }

    private static boolean isCurrentTimeWithinNoRemindersRange() {
        ZoneId zoneId = ZoneId.of(ConfigRegistry.props().botProperties().getTimezone());
        return isTimeWithinNoReminderRange(toZonedTime(LocalTime.now(), zoneId));

    }

    private static boolean isTimeWithinNoReminderRange(ZonedDateTime time) {
        ZoneId zoneId = time.getZone();

        ZonedDateTime from = toZonedTime(ConfigRegistry
                .props()
                .scheduledJobs()
                .getNoReminders()
                .getFrom(), zoneId);

        ZonedDateTime to = toZonedTime(ConfigRegistry
                .props()
                .scheduledJobs()
                .getNoReminders()
                .getTo(), zoneId)
                .plusDays(1);

        return time.isAfter(from) && time.isBefore(to);
    }

    private static ZonedDateTime toZonedTime(LocalTime time, ZoneId timezone) {
        return ZonedDateTime
                .of(time.atDate(LocalDate.now()), timezone);
    }

    @Override
    public void sendGreetingOnStartup() {
        if (ConfigRegistry.props().botProperties().isOnStartupNotificationsEnabled()) {
            TelegramFitnessExecutorService
                    .executorService()
                    .scheduleOnce(
                            this::sendOnStartUpMessage,
                            5,
                            TimeUnit.SECONDS);
        }
    }

    @Override
    public void checkBotSessionPeriodically() {
        LOGGER.debug("Checking whether the session of a bot is still active...");
        TelegramFitnessExecutorService
                .executorService()
                .scheduleWithFixedDelay(
                        this::checkBotSession,
                        ConfigRegistry.props().scheduledJobs().getCheckSession().getInitialDelay(),
                        ConfigRegistry.props().scheduledJobs().getCheckSession().getInterval(),
                        TimeUnit.SECONDS);
    }

    @Override
    public void sendFitnessReminderSchedule() {
        String notifiedChatId = NotificationsRepository.getRepository().getNotifiedChatId();
        ScheduledFuture<?> fitnessReminderScheduler = createFitnessReminderScheduler(notifiedChatId);
        ScheduledJobsRepository.getRepository().addScheduledJobForUser(notifiedChatId, fitnessReminderScheduler);
    }

    @Override
    public boolean resetFitnessReminderTimer(String chatId) {
        try {
            ScheduledJobsRepository.getRepository().removeScheduledJobForUser(chatId);
            ScheduledJobsRepository.getRepository().addScheduledJobForUser(chatId, createFitnessReminderScheduler(chatId));
            return true;
        } catch (Exception e) {
            LOGGER.error("While resetting the timer an unexpected error occurred. ChatID: {}", chatId, e);
            return false;
        }
    }

    private void sendOnStartUpMessage() {
        String chatId = NotificationsRepository.getRepository().getNotifiedChatId();
        MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry.props().notifications().getOnStartup());
        try {
            FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while sending a greeting message. ChatID: {}", chatId);
        }
    }

    private ScheduledFuture<?> createFitnessReminderScheduler(String chatId) {
        LOGGER.debug("Sending a fitness reminder to {}. Date: {}", chatId, new Date());
        return TelegramFitnessExecutorService.executorService().scheduleWithFixedDelay(
                () -> {
                    if (areNotificationsDisabled(chatId)) {
                        LOGGER.warn("No fitness reminder sent for {}. Notifications are disabled!", chatId);
                        return;
                    }
                    if (isCurrentTimeWithinNoRemindersRange()) {
                        LOGGER.warn("Current time is out of reminder hours for {}. Notifications are disabled", chatId);
                        return;
                    }
                    sendFitnessReminder(chatId);
                },
                calculateInitialDelay(),
                ConfigRegistry.props().scheduledJobs().getSendWorkout().getInterval(),
                TimeUnit.SECONDS);
    }

    private void sendFitnessReminder(String chatId) {
        MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry.props().notifications().getWorkoutReminder());
        try {
            FitnessBotSender.getSender().sendMessages(messageMethod.getMessage());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while sending a fitness reminder. ChatID: {}", chatId);
        }
    }

    private void checkBotSession() {
        if (FitnessReminderBot.getFitnessBot().isSessionActive()) {
            return;
        }
        FitnessReminderBot.getFitnessBot().resetSession();
    }
}
