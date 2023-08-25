package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.impl.FitnessBotSender;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that a client whenever he/she sends an unknown command.
 *
 * @author Anton Skripin
 */
public class UnknownCommandHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownCommandHandler.class);

    protected UnknownCommandHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isUnknownCommand()) {
            unknownCommandHandler(updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }

    private void unknownCommandHandler(String chatId) {
        MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry.props().notifications().getUnknownCommand());
        try {
            FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while notifying a user about having received an unknown command. ChatID: {}", chatId);
        }
    }
}
