package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.FitnessBotResponseSender;
import ansk.development.service.methods.MessageMethod;
import ansk.development.service.methods.WorkoutMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that generates abs workout upon receiving an event with the abs command.
 *
 * @author Anton Skripin
 */
public class GenerateAbsWorkoutHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateAbsWorkoutHandler.class);

    protected GenerateAbsWorkoutHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isAbsWorkout()) {
            generateAbsWorkout(updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }

    private void generateAbsWorkout(String chatId) {
        MessageMethod messageMethod = new MessageMethod(chatId, ConfigRegistry.props().forNotification().getAbsWorkout());
        WorkoutMethod workoutMethod = WorkoutMethod.generateWorkout(chatId).absWorkout();
        try {
            FitnessBotResponseSender.getSender().sendMessage(messageMethod.getMessage());
            FitnessBotResponseSender.getSender().sendWorkout(workoutMethod.getExercises());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while generating an abs workout. ChatID: {}", chatId);
        }
    }
}
