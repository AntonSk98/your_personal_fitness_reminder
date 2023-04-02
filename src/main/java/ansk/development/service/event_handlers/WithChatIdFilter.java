package ansk.development.service.event_handlers;

import ansk.development.domain.FitnessUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter that checks whether an event has chatId present.
 *
 * @author Anton Skripin
 */
public class WithChatIdFilter extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithChatIdFilter.class);

    public WithChatIdFilter(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (!updateEvent.isChatIdPresent()) {
            LOGGER.warn("Received an event but chat id was not part of the event. Event: {}", updateEvent);
            return;
        }
        super.handle(updateEvent);
    }
}
