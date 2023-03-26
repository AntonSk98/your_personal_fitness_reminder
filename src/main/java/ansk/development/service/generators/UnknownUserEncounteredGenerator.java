package ansk.development.service.generators;

import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.FitnessReminderBot;
import ansk.development.service.api.FitnessBotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static ansk.development.domain.ConfigurationConstants.*;

public class UnknownUserEncounteredGenerator extends FitnessBotManager {

    public UnknownUserEncounteredGenerator(String chatId) {
        super(chatId);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownUserEncounteredGenerator.class);

    @Override
    public void doItWithUpdateEvent(FitnessReminderBot fitnessReminderBot, Update updateEvent) throws FitnessBotOperationException {

        Optional<String> optionalUserName = Optional
                .ofNullable(updateEvent.getMessage())
                .map(Message::getFrom)
                .map(User::getUserName);
        try {
            if (optionalUserName.isPresent()) {
                String userName = optionalUserName.get();
                MessageGenerator messageAboutUnknownUser = new MessageGenerator(MESSAGE_ABOUT_UNKNOWN_USER + String.format("--> {%s:%s}", userName, getChatId()), ROOT_CHAT_ID);
                MessageGenerator messageToUnknownUser = new MessageGenerator(MESSAGE_TO_UNKNOWN_USER, getChatId());
                fitnessReminderBot.execute(messageAboutUnknownUser.getMessage());
                fitnessReminderBot.execute(messageToUnknownUser.getMessage());
            } else {
                LOGGER.error("Received unknown user even but could not react accordingly because chatId or userName are null!");
            }
        } catch (TelegramApiException e) {
            LOGGER.error("Received the following event {} from an unknown user {} but could not process it",
                    updateEvent,
                    optionalUserName.get(),
                    e);
            throw new FitnessBotOperationException(e);
        }

    }
}
