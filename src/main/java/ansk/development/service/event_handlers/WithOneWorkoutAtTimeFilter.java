package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.WorkoutProcessRepository;
import ansk.development.service.FitnessBotResponseSender;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter that ensures that only one command operationg is running for a given user.
 * If this is not the case, then the event must be aborted and not passed further.
 *
 * @author Anton Skripin
 */
public class WithOneWorkoutAtTimeFilter extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithOneWorkoutAtTimeFilter.class);

    public WithOneWorkoutAtTimeFilter(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (WorkoutProcessRepository.getRepository().hasRunningProcesses(updateEvent.getChatId())) {
            MessageMethod messageMethod = new MessageMethod(updateEvent.getChatId(), ConfigRegistry.props().forNotification().getWorkoutInProgress());
            try {
                FitnessBotResponseSender.getSender().sendMessage(messageMethod.getMessage());
            } catch (FitnessBotOperationException e) {
                throw new RuntimeException(e);
            }
            LOGGER.error("User {} is already doing some workout. But he is still sending me messages...Ignoring this spammer...", updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }
}
