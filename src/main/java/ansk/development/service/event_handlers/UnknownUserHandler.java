package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.impl.FitnessBotSender;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that notifies the owner of the bot whenever an unknown user attempts to make use of the bot.
 * The unknown user is notified is well to contact the owner of the bot to get access to it.
 *
 * @author Anton Skripin
 */
public class UnknownUserHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownUserHandler.class);

    public UnknownUserHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isEventFromUnknownUser()) {
            handleEventFromUnknownUser(updateEvent.getChatId(), updateEvent.getUsername());
            return;
        }
        super.handle(updateEvent);
    }

    private void handleEventFromUnknownUser(String chatId, String username) {
        final String messageToUnknownUser = ConfigRegistry.props().notifications().getToUnknownUser();
        final String rootChatId = ConfigRegistry.props().botProperties().getChatId();
        String messageAboutUnknownUser = String.format("%s {%s:%s}", ConfigRegistry
                .props()
                .notifications()
                .getAboutUnknownUser(), username, chatId);
        MessageMethod toRoot = new MessageMethod(rootChatId, messageAboutUnknownUser);
        MessageMethod toUnknownUser = new MessageMethod(chatId, messageToUnknownUser);

        try {
            FitnessBotSender.getSender().sendMessages(toRoot.getMessage(), toUnknownUser.getMessage());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Received a message from an unknown user but could not process it! Username: {}, ChatID: {}", username, chatId);
        }
    }
}
