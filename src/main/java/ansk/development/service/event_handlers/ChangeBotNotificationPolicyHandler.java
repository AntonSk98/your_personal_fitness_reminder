package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessBotCommands;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.NotificationsRepository;
import ansk.development.service.FitnessBotResponseSender;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static ansk.development.domain.FitnessBotCommands.DISABLE_NOTIFICATIONS;
import static ansk.development.domain.FitnessBotCommands.ENABLE_NOTIFICATIONS;

/**
 * Event handler that is responsible for enabling and disabling notifications for a given client.
 *
 * @author Anton Skripin
 */
public class ChangeBotNotificationPolicyHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeBotNotificationPolicyHandler.class);

    public ChangeBotNotificationPolicyHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isEnableNotificationEvent()) {
            enableNotifications(updateEvent.getChatId());
            return;
        }

        if (updateEvent.isDisableNotificationsEvent()) {
            disableNotifications(updateEvent.getChatId());
            return;
        }

        super.handle(updateEvent);
    }

    private void disableNotifications(String chatId) {
        try {
            FitnessBotResponseSender.getSender().sendMessage(getMessage(chatId, DISABLE_NOTIFICATIONS));
            NotificationsRepository.getRepository().disableNotificationsForUser(chatId);
        } catch (FitnessBotOperationException fitnessBotOperationException) {
            LOGGER.error("An error occurred while handling an event to disable notifications. ChatID: {}", chatId);
        }
    }

    private void enableNotifications(String chatId) {
        try {
            FitnessBotResponseSender.getSender().sendMessage(getMessage(chatId, ENABLE_NOTIFICATIONS));
            NotificationsRepository.getRepository().enableNotificationsForUser(chatId);
        } catch (FitnessBotOperationException fitnessBotOperationException) {
            LOGGER.error("An error occurred while handling an event to enable notifications. ChatID: {}", chatId);
        }
    }

    private SendMessage getMessage(String chatId, FitnessBotCommands event) {
        if (ENABLE_NOTIFICATIONS.equals(event)) {
            final String enableNotificationsMessage = ConfigRegistry
                    .props()
                    .forNotification()
                    .getEnabledNotifications();
            return new MessageMethod(chatId, enableNotificationsMessage).getMessage();
        } else {
            final String disableNotificationsMessage = ConfigRegistry
                    .props()
                    .forNotification()
                    .getDisabledNotifications();
            return new MessageMethod(chatId, disableNotificationsMessage).getMessage();
        }


    }
}
