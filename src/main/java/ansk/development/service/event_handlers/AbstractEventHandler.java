package ansk.development.service.event_handlers;

import ansk.development.domain.FitnessUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Encapsulates common implementation among all event handlers.
 *
 * @author Anton Skripin
 */
public abstract class AbstractEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventHandler.class);
    private EventHandler nextEventHandler;

    public AbstractEventHandler() {
    }

    protected AbstractEventHandler(EventHandler nextEventHandler) {
        this.nextEventHandler = nextEventHandler;
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        this.getNext()
                .ifPresentOrElse(
                        eventHandler -> eventHandler.handle(updateEvent),
                        () -> LOGGER.warn("Received an event but could not handle it! Here is the event: {}", updateEvent)
                );
    }

    @Override
    public EventHandler setNext(EventHandler eventHandler) {
        this.nextEventHandler = eventHandler;
        return this;
    }

    @Override
    public Optional<EventHandler> getNext() {
        return Optional.ofNullable(this.nextEventHandler);
    }
}
