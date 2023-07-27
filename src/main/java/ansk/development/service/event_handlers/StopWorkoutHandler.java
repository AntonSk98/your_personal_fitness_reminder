package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.WorkoutProcessRepository;
import ansk.development.repository.api.IWorkoutProcessRepository;
import ansk.development.service.FitnessBotSender;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that stop current workout.
 *
 * @author Anton Skripin
 */
public class StopWorkoutHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetTimerHandler.class);

    protected StopWorkoutHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isStopWorkoutEvent()) {
            stopWorkout(updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }

    private void stopWorkout(String chatId) {
        IWorkoutProcessRepository processRepository = WorkoutProcessRepository.getRepository();
        if (processRepository.hasRunningProcesses(chatId)) {
            MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry
                    .props()
                    .forNotification()
                    .getStopWorkout());
            processRepository.interruptProcess(chatId);
            try {
                FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
            } catch (FitnessBotOperationException e) {
                LOGGER.error("Unexpected error occurred while stopping a currently running workout. Chat id: {}", chatId);
            }
            return;
        }

        MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry
                .props()
                .forNotification()
                .getNoOngoingWorkout());

        try {
            FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while notifying a user about non-running workouts. Chat id: {}", chatId);
        }
    }


}
