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
 * Handler that generates a workout with push-ups for a client.
 *
 * @author Anton Skripin
 */
public class GeneratePushUpsWorkoutHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePushUpsWorkoutHandler.class);

    public GeneratePushUpsWorkoutHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isPushUpsNotificationEvent()) {
            generatePushUpsWorkout(updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }

    private void generatePushUpsWorkout(String chatId) {
        MessageMethod messageMethod = new MessageMethod(chatId,
                ConfigRegistry
                        .props()
                        .notifications()
                        .getPushUps());
        WorkoutMethod pushUpsWorkout = WorkoutMethod.generateWorkout(chatId).pushUpsWorkout();
        try {
            FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
            FitnessBotSender.getSender().sendWorkoutAsRegisteredProcess(chatId, pushUpsWorkout.getExercises());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Unexpected error occurred while sending push-up workout. Chat ID: {}", chatId);
        }
    }
}
