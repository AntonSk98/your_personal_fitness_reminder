package ansk.development.service;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessBotCommands;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.service.event_handlers.EventHandler;
import ansk.development.service.event_handlers.EventHandlerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Custom implementation of {@link TelegramLongPollingBot}.
 * This bot is able to accept a list of commands -> {@link FitnessBotCommands} and react accordingly.
 * Moreover, a respective scheduling service periodically sends a reminder for an end-user to perform some exercises.
 *
 * @author Anton Skripin
 */
public class FitnessReminderBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(FitnessReminderBot.class);
    private static FitnessReminderBot fitnessReminderBot;
    private final EventHandler eventHandlerChain;
    private final BotSession botSession;

    private FitnessReminderBot() {
        this.eventHandlerChain = initializeEventHandlerChain();
        this.botSession = initializeSession();
    }

    public static FitnessReminderBot getFitnessBot() {
        if (fitnessReminderBot == null) {
            fitnessReminderBot = new FitnessReminderBot();
        }
        return fitnessReminderBot;
    }

    public EventHandler getEventHandlerChain() {
        return eventHandlerChain;
    }

    @Override
    public String getBotUsername() {
        return ConfigRegistry.props().forBot().getUsername();
    }

    @Override
    public String getBotToken() {
        return ConfigRegistry.props().forBot().getToken();
    }

    @Override
    public void onUpdateReceived(Update updateEvent) {
        this.eventHandlerChain.handle(new FitnessUpdateEvent(updateEvent));
    }

    public FitnessReminderBot withGreetingOnStartup() {
        ScheduledJobsService
                .scheduledJobsService()
                .sendGreetingOnStartup();
        return this;
    }


    public FitnessReminderBot withPeriodicSessionChecks() {
        ScheduledJobsService
                .scheduledJobsService()
                .checkBotSessionPeriodically();
        return this;
    }

    public FitnessReminderBot withFitnessReminders() {
        ScheduledJobsService
                .scheduledJobsService()
                .sendFitnessReminderSchedule(FitnessBotCommands.WITH_DUMBBELLS);
        return this;
    }

    public boolean isSessionActive() {
        return this.botSession.isRunning();
    }

    public void resetSession() {
        if (this.isSessionActive()) {
            LOGGER.debug("Bot session is still active! No action is performed. BotSession: {}", botSession);
            return;
        }
        this.botSession.start();
        LOGGER.info("Bot session was restarted!");
    }

    private BotSession initializeSession() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            BotSession session = botsApi.registerBot(this);
            LOGGER.info("Fitness bot is registered and session established!");
            return session;
        } catch (TelegramApiException e) {
            LOGGER.error("The following error occurred while initializing a session", e);
            throw new RuntimeException(e);
        }
    }

    private EventHandler initializeEventHandlerChain() {
        return EventHandlerBuilder
                .initializeChain()
                .withUnknownCommandHandler()
                .withResetTimerHandler()
                .withAbsWorkoutHandler()
                .withPushUpsWorkout()
                .withWeightFreeWorkoutHandler()
                .withWorkoutWithDumbbellsHandler()
                .withStretchingWorkoutHandler()
                .withAdaptableNotificationPolicyHandler()
                .onlyWithEnabledNotifications()
                .withOneWorkoutAtTimeHandler()
                .onlyWithTodayEvents()
                .withUnknownUserHandler()
                .onlyWithChatId()
                .build();
    }
}
