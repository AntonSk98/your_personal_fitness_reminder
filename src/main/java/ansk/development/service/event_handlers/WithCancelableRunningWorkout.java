package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.WorkoutProcessRepository;
import ansk.development.repository.api.IWorkoutProcessRepository;
import ansk.development.service.impl.FitnessBotSender;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter that checks whether a process is already running for a user.
 * If it is the case, interrupts the running process. If it is not the case, the event is passed further.
 *
 * @author Anton Skripin
 */
public class WithCancelableRunningWorkout extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithCancelableRunningWorkout.class);

    public WithCancelableRunningWorkout(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        IWorkoutProcessRepository processRepository = WorkoutProcessRepository.getRepository();
        String chatId = updateEvent.getChatId();
        if (processRepository.hasRunningProcesses(chatId)) {
            MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry
                    .props()
                    .notifications()
                    .getWorkoutInProgress());
            processRepository.interruptProcess(chatId);
            try {
                FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
            } catch (FitnessBotOperationException e) {
                LOGGER.error("Unexpected error occurred while aborting running workout");
            }
            LOGGER.warn("Changing workout type for user {}", chatId);
        }
        super.handle(updateEvent);
    }
}
