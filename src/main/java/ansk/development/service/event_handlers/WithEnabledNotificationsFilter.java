package ansk.development.service.event_handlers;

import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.repository.NotificationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter that checks whether notifications are enabled for a given user.
 * If this is not the case -> abort the chain, otherwise, pass the event further.
 *
 * @author Anton Skripin
 */
public class WithEnabledNotificationsFilter extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithEnabledNotificationsFilter.class);

    public WithEnabledNotificationsFilter(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        String chatId = updateEvent.getChatId();
        if (!NotificationsRepository.getRepository().areNotificationsEnabled(chatId) && !updateEvent.isEnableNotificationEvent()) {
            LOGGER.warn("Received an event but notifications are disabled, so ignoring it!. Event: {}", updateEvent.getUpdate());
            return;
        }
        super.handle(updateEvent);
    }
}
