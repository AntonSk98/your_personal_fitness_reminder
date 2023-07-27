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
 * Handler that generates a workout with dumbbells.
 *
 * @author Anton Skripin
 */
public class GenerateWorkoutWithDumbbellsHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateWorkoutWithDumbbellsHandler.class);

    public GenerateWorkoutWithDumbbellsHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isWorkoutWithDumbbellsEvent()) {
            generateWorkoutWithDumbbells(updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }

    private void generateWorkoutWithDumbbells(String chatId) {
        MessageMethod message = new MessageMethod(chatId, ConfigRegistry.props().forNotification().getWorkoutWithDumbbells());
        WorkoutMethod workoutWithDumbbells = WorkoutMethod.generateWorkout(chatId).withDumbbells();
        try {
            FitnessBotSender.getSender().sendMessages(message.getMessage());
            FitnessBotSender.getSender().sendWorkoutAsRegisteredProcess(chatId, workoutWithDumbbells.getExercises());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while generating workout with dumbbells. ChatID: {}", chatId);
        }
    }
}
