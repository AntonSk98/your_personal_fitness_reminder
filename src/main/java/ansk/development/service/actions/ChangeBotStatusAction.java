package ansk.development.service.actions;

import ansk.development.domain.BotEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.FitnessReminderBot;
import ansk.development.service.generators.MessageGenerator;
import ansk.development.service.api.FitnessBotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static ansk.development.domain.ConfigurationConstants.*;

public class ChangeBotStatusAction extends FitnessBotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeBotStatusAction.class);

    public ChangeBotStatusAction(String chatId) {
        super(chatId);
    }

    @Override
    public void doItWithBotEvent(FitnessReminderBot fitnessReminderBot, BotEvent botEvent) throws FitnessBotOperationException {
        String message = null;
        if (botEvent.equals(BotEvent.ENABLE)) {
            LOGGER.info("Received a command to enable the bot");
            fitnessReminderBot.setEnabled(getChatId(), true);
            message = ENABLE_BOT;
        }
        if (botEvent.equals(BotEvent.SUPPRESS)) {
            LOGGER.info("Received a command to disable the bot. No new exercise will be sent until the bot gets enabled again");
            fitnessReminderBot.setEnabled(getChatId(), false);
            message = SUPPRESS_BOT;
        }
        MessageGenerator messageGenerator = new MessageGenerator(message, getChatId());
        try {
            fitnessReminderBot.execute(messageGenerator.getMessage());
        } catch (TelegramApiException e) {
            LOGGER.error("While changing the status of the bot the following error occurred: {}", e.getMessage(), e);
            throw new FitnessBotOperationException(e);
        }
    }
}
