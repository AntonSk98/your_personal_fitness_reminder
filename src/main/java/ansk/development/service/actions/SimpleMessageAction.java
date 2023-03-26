package ansk.development.service.actions;

import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.FitnessReminderBot;
import ansk.development.service.api.FitnessBotManager;
import ansk.development.service.generators.MessageGenerator;
import ansk.development.service.generators.UnknownUserEncounteredGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SimpleMessageAction extends FitnessBotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownUserEncounteredGenerator.class);

    private final String message;


    public SimpleMessageAction(String chatId, String message) {
        super(chatId);
        this.message = message;
    }

    @Override
    public void doIt(FitnessReminderBot fitnessReminderBot) throws FitnessBotOperationException {
        try {
            MessageGenerator generatedMessage = new MessageGenerator(message, getChatId());
            fitnessReminderBot.execute(generatedMessage.getMessage());
        } catch (TelegramApiException e) {
            LOGGER.error("Received a simple message '{}' that was supposed to be sent but an exception occurred!", message, e);
            throw new FitnessBotOperationException(e);
        }
    }
}
