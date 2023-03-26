package ansk.development.service;

import ansk.development.domain.BotEvent;
import ansk.development.exception.ExceptionUtils;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.api.FitnessBotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static ansk.development.domain.ConfigurationConstants.*;

/**
 * Custom implementation of {@link TelegramLongPollingBot}.
 * This bot is able to accepts a list of commands {@link BotEvent} and react accordingly
 * Moreover, every
 * {@link ansk.development.domain.ConfigurationConstants#NOTIFICATION_INTERVAL}
 * {@link ansk.development.domain.ConfigurationConstants#NOTIFICATION_INTERVAL_TIME_UNIT}
 * a respective scheduling service sends a reminder for an end-user to perform some exercises.
 */
public class FitnessReminderBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(FitnessReminderBot.class);

    private final Map<String, Boolean> userToEnabledMap = Arrays
            .stream(ALLOWED_CHAT_IDS)
            .collect(Collectors.toMap(chatId -> chatId, o -> false));

    private BotSession botSession;

    public FitnessReminderBot() {
        initializeSession();
    }

    public boolean isEnabled(String chatId) {
        return userToEnabledMap.get(chatId);
    }

    public void setEnabled(String chatId, boolean enabled) {
        userToEnabledMap.put(chatId, enabled);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            String chatId = BotEvent.getChatId(update).orElseThrow(() -> {
                LOGGER.warn("Received an update event but chat it was not present!");
                return new FitnessBotOperationException();
            });

            if (IGNORE_OLD_UPDATE_EVENTS && BotEvent.isEvenFromPreviousDays(update)) {
                LOGGER.warn("An update event is old. I am skipping it: {}", update);
                return;
            }

            if (!BotEvent.isFromAllowedUser(update)) {
                FitnessBotManager.performActionWithUpdateEvent(chatId, this, BotEvent.UNKNOWN_USER, update);
                return;
            }

            if (BotEvent.isMessageWithText(update) && BotEvent.isKnownCommand(update)) {
                BotEvent botEvent = BotEvent.toBotCommand(update);
                FitnessBotManager.performAction(chatId, this, botEvent);

            }
        } catch (FitnessBotOperationException e) {
            if (ExceptionUtils.isTooManyRequestException(e)) {
                String chatId = BotEvent.getChatId(update).get();
                try {
                    FitnessBotManager.performAction(chatId, this, BotEvent.TOO_MANY_REQUESTS, String.valueOf(ExceptionUtils.getTimeToRetry(e)));
                } catch (FitnessBotOperationException ex) {
                    LOGGER.error("Wanted to notify a user that there were too many requests but failed to do it!", ex);
                }
            }
            LOGGER.error("Received an update but could not handle it! {}", update, e);
            checkBotSessionAndReset();
        }
    }

    public void start() {
        this.sendReminder();
        this.checkFitnessBotSessionPeriodically();
    }

    public void checkFitnessBotSessionPeriodically() {
        TelegramFitnessExecutorService.scheduleWithFixedDelay(this::checkBotSessionAndReset,
                CHECK_SESSION_JOB_INITIAL_DELAY,
                CHECK_SESSION_JOB_INTERVAL,
                CHECK_SESSION_BOT_TIME_UNIT);
    }

    public void sendReminder() {
        TelegramFitnessExecutorService.scheduleWithRetry(
                () -> {
                    try {
                        for (String chatId: ALLOWED_CHAT_IDS) {
                            FitnessBotManager.performAction(chatId, this, BotEvent.NEW_EXERCISE);
                        }
                    } catch (FitnessBotOperationException e) {
                        LOGGER.error("A fitness reminder notification could not be sent because of the following exception", e);
                        checkBotSessionAndReset();
                    }
                },
                INITIAL_DELAY,
                NOTIFICATION_INTERVAL,
                NOTIFICATION_INTERVAL_TIME_UNIT);
    }

    private void checkBotSessionAndReset() {
        if (this.botSession.isRunning()) {
            LOGGER.info("Bot session is still active! No action is performed. BotSession: {}", botSession);
            return;
        }
        this.botSession.start();
        LOGGER.info("Bot session was restarted!");
    }

    private void initializeSession() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            this.botSession = botsApi.registerBot(this);
            LOGGER.info("Fitness bot is registered");
        } catch (TelegramApiException e) {
            LOGGER.error("The following error occurred while registering a fitness bot", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("Set up a session. Bot is enabled!");
    }
}
