package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that ensures that only events from today are being processed.
 * If the bot receives an old event, it aborts its processing without passing it further along the chain.
 *
 * @author Anton Skripin
 */
public class WithOnlyTodayEventsFilter extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithOnlyTodayEventsFilter.class);

    public WithOnlyTodayEventsFilter(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (ConfigRegistry.props().botProperties().isIgnoreOldEvents() && updateEvent.isEventFromPreviousDays()) {
            LOGGER.warn("Received an old event. It will be ignored. Event: {}", updateEvent.getUpdate());
            return;
        }
        super.handle(updateEvent);
    }
}
