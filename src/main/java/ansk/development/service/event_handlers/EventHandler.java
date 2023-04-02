package ansk.development.service.event_handlers;

import ansk.development.domain.FitnessUpdateEvent;

import java.util.Optional;

/**
 * The core of any possible event handlers.
 * If any particular handler is not able to process an event, it passes the event further along the chain.
 *
 * @author Anton Skripin
 */
public interface EventHandler {
    Optional<EventHandler> getNext();

    EventHandler setNext(EventHandler eventHandler);

    void handle(FitnessUpdateEvent updateEvent);
}
