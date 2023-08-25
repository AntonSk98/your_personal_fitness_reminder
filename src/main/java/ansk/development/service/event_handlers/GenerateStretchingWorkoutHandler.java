package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.impl.FitnessBotSender;
import ansk.development.service.methods.MessageMethod;
import ansk.development.service.methods.WorkoutMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that generates a stretching routine for a client.
 *
 * @author Anton Skripin
 */
public class GenerateStretchingWorkoutHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateStretchingWorkoutHandler.class);

    public GenerateStretchingWorkoutHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isStretchingWorkoutEvent()) {
            generateStretchingWorkout(updateEvent.getChatId());
            return;
        }

        super.handle(updateEvent);
    }

    private void generateStretchingWorkout(String chatId) {
        MessageMethod message = new MessageMethod(chatId, ConfigRegistry.props().notifications().getStretchingWorkout());
        WorkoutMethod morningWorkout = WorkoutMethod.generateWorkout(chatId).stretchingWorkout();
        try {
            FitnessBotSender.getSender().sendMessage(message.getMessage());
            FitnessBotSender.getSender().sendWorkoutAsRegisteredProcess(chatId, morningWorkout.getExercises());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while sending a stretching workout to the user. ChaID: {}", chatId);
        }
    }


}
