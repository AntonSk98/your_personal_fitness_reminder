package ansk.development.service;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessBotCommands;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.NotificationsRepository;
import ansk.development.repository.ScheduledJobsRepository;
import ansk.development.repository.api.IScheduledJobsRepository;
import ansk.development.service.api.IScheduledJobsService;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static ansk.development.domain.FitnessUpdateEvent.createInternalUpdateEvent;

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

    @Override
    public void sendGreetingOnStartup() {
        if (ConfigRegistry.props().forBot().isOnStartupNotificationsEnabled()) {
            TelegramFitnessExecutorService.scheduleOnce(
                    this::sendOnStartUpMessage,
                    10,
                    TimeUnit.SECONDS);
        }
    }

    @Override
    public void checkBotSessionPeriodically() {
        LOGGER.debug("Checking whether the session of a bot is still active...");
        TelegramFitnessExecutorService
                .scheduleWithFixedDelay(
                        this::checkBotSession,
                        ConfigRegistry.props().forScheduled().getCheckSession().getInitialDelay(),
                        ConfigRegistry.props().forScheduled().getCheckSession().getInterval(),
                        ConfigRegistry.props().forScheduled().getCheckSession().getTimeUnit());
    }

    @Override
    public void sendFitnessReminderSchedule(FitnessBotCommands fitnessBotCommands) {
        Runnable fitnessReminderRunnable = sendFitnessReminderRunnable();
        Thread fitnessReminderThread = new Thread(fitnessReminderRunnable);
        fitnessReminderThread.start();
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

    private Runnable sendFitnessReminderRunnable() {
        return () -> {
            for (String chatId : NotificationsRepository.getRepository().getAllChatIdsWithEnabledNotifications()) {
                ScheduledFuture<?> fitnessReminderScheduler = createFitnessReminderScheduler(chatId);
                ScheduledJobsRepository.getRepository().addScheduledJobForUser(chatId, fitnessReminderScheduler);
                try {
                    LOGGER.debug("Starting a reminder runnable from thread: {}. ChatID: {}. Date: {}", Thread.currentThread(), chatId, new Date());
                    Thread.sleep(ConfigRegistry.props().forBot().getGroupIntervalDelayInMs());
                    LOGGER.debug("Continuing to the next sender...");
                } catch (InterruptedException e) {
                    LOGGER.error("Unexpected error occurred while sending fitness reminders. ChatID: {}", chatId, e);
                }
            }
        };
    }

    private void sendOnStartUpMessage() {
        for (String chatId : NotificationsRepository.getRepository().getAllChatIds()) {
            MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry.props().forNotification().getOnStartup());
            try {
                FitnessBotResponseSender.getSender().sendMessage(messageMethod.getMessage());
            } catch (FitnessBotOperationException e) {
                LOGGER.error("Unexpected error occurred while sending a greeting message. ChatID: {}", chatId);
            }
        }
    }

    private ScheduledFuture<?> createFitnessReminderScheduler(String chatId) {
        LOGGER.debug("Sending a fitness reminder to {}. Date: {}", chatId, new Date());
        return TelegramFitnessExecutorService.scheduleWithFixedDelay(
                () -> sendFitnessReminder(chatId),
                ConfigRegistry.props().forScheduled().getSendWorkout().getInitialDelay(),
                ConfigRegistry.props().forScheduled().getSendWorkout().getInterval(),
                ConfigRegistry.props().forScheduled().getSendWorkout().getTimeUnit());
    }

    private void sendFitnessReminder(String chatId) {
        FitnessReminderBot
                .getFitnessBot()
                .getEventHandlerChain()
                .handle(createInternalUpdateEvent(chatId, FitnessBotCommands.STRETCHING));
    }

    private void checkBotSession() {
        if (FitnessReminderBot.getFitnessBot().isSessionActive()) {
            return;
        }
        FitnessReminderBot.getFitnessBot().resetSession();
    }
}
