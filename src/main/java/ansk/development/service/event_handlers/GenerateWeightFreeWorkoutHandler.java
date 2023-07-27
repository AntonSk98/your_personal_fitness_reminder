package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.FitnessBotSender;
import ansk.development.service.methods.MessageMethod;
import ansk.development.service.methods.WorkoutMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that generates a workout with no equipment for a client.
 *
 * @author Anton Skripin
 */
public class GenerateWeightFreeWorkoutHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateWeightFreeWorkoutHandler.class);

    protected GenerateWeightFreeWorkoutHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isWeightFreeWorkoutEvent()) {
            generateWeightFreeWorkout(updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }

    private void generateWeightFreeWorkout(String chatId) {
        MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry.props().forNotification().getWeightFree());
        WorkoutMethod workoutMethod = WorkoutMethod.generateWorkout(chatId).weightFreeWorkout();
        try {
            FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
            FitnessBotSender.getSender().sendWorkoutAsRegisteredProcess(chatId, workoutMethod.getExercises());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while generating a weight-free workout. ChatID: {}", chatId);
        }
    }
}
