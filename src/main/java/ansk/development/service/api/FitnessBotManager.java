package ansk.development.service.api;

import ansk.development.domain.BotEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.exception.UnknownBotCommandException;
import ansk.development.service.actions.ChangeBotStatusAction;
import ansk.development.service.FitnessReminderBot;
import ansk.development.service.actions.GenerateNewExerciseAction;
import ansk.development.service.actions.GenerateMorningRoutineAction;
import ansk.development.service.generators.MessageGenerator;
import ansk.development.service.actions.SimpleMessageAction;
import ansk.development.service.generators.UnknownUserEncounteredGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static ansk.development.domain.ConfigurationConstants.SUPPRESS_BOT;

/**
 * Manager class that resolves received events and calls a respective action to be performed.
 */
public abstract class FitnessBotManager {

    private final String chatId;

    public FitnessBotManager(String chatId) {
        this.chatId = chatId;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FitnessBotManager.class);

    public static void performActionWithUpdateEvent(String chatId, FitnessReminderBot fitnessReminderBot, BotEvent botEvent, Update update) throws FitnessBotOperationException {
        if (botEvent.equals(BotEvent.UNKNOWN_USER)) {
            new UnknownUserEncounteredGenerator(chatId).doItWithUpdateEvent(fitnessReminderBot, update);
            return;
        }

        LOGGER.warn("Received unknown bot command {} with update event {}", botEvent, update);
        throw new UnknownBotCommandException("Unknown bot command: " + botEvent);
    }

    public static void performAction(String chatId, FitnessReminderBot fitnessReminderBot, BotEvent botEvent, String... params) throws FitnessBotOperationException {
        if (botEvent.equals(BotEvent.NEW_EXERCISE)) {
            LOGGER.info("Received {}. Generating new action for a client.", botEvent);
            new GenerateNewExerciseAction(chatId).doIt(fitnessReminderBot);
            return;
        }

        if (botEvent.equals(BotEvent.WARM_UP)) {
            LOGGER.info("Received {}. Generating new action for a client.", botEvent);
            new GenerateMorningRoutineAction(chatId).doIt(fitnessReminderBot);
            return;
        }

        if (botEvent.equals(BotEvent.TOO_MANY_REQUESTS)) {
            LOGGER.info("Received {}. Generating new action for a client.", botEvent);
            String retryIn = params.length > 0 ? params[0] : "some";
            new SimpleMessageAction(chatId, String.format("Too many requests. Try again later in %s seconds!", retryIn)).doIt(fitnessReminderBot);
            return;
        }

        if (botEvent.equals(BotEvent.SUPPRESS)) {
            LOGGER.info("Received {}. Generating new action for a client.", botEvent);
            new ChangeBotStatusAction(chatId).doItWithBotEvent(fitnessReminderBot, botEvent);
            return;
        }

        if (botEvent.equals(BotEvent.ENABLE)) {
            LOGGER.info("Received {}. Generating new action for a client.", botEvent);
            new ChangeBotStatusAction(chatId).doItWithBotEvent(fitnessReminderBot, botEvent);
            return;
        }

        LOGGER.warn("Received unknown bot command: {}", botEvent);
        throw new UnknownBotCommandException("Unknown bot command: " + botEvent);
    }

    public void notEnabledBotNotification(FitnessReminderBot fitnessReminderBot) throws TelegramApiException {
        LOGGER.warn("Was trying to notify a client but he suppressed the bot. I will tell him to enable me again!");
        MessageGenerator message = new MessageGenerator(SUPPRESS_BOT, getChatId());
        fitnessReminderBot.execute(message.getMessage());
    }

    public void doIt(FitnessReminderBot fitnessReminderBot) throws FitnessBotOperationException {
        LOGGER.error("Called abstract method that is not implemented");
    }

    public void doItWithBotEvent(FitnessReminderBot fitnessReminderBot, BotEvent botEvent) throws FitnessBotOperationException {
        LOGGER.error("Called abstract method that is not implemented");
    }

    public void doItWithUpdateEvent(FitnessReminderBot fitnessReminderBot, Update updateEvent) throws FitnessBotOperationException {
        LOGGER.error("Called abstract method that is not implemented");
    }

    public String getChatId() {
        return chatId;
    }
}
