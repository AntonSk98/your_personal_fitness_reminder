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
            MessageMethod messageMethod = new MessageMethod(updateEvent.getChatId(),
                    ConfigRegistry
                            .props()
                            .forNotification()
                            .getPushUps());
            WorkoutMethod pushUpsWorkout = WorkoutMethod.generateWorkout(updateEvent.getChatId()).pushUpsWorkout();
            try {
                FitnessBotResponseSender.getSender().sendMessage(messageMethod.getMessage());
                FitnessBotResponseSender.getSender().sendWorkout(pushUpsWorkout.getExercises());
            } catch (FitnessBotOperationException e) {
                LOGGER.error("Unexpected error occurred while sending push-up workout. Chat ID: {}", updateEvent.getChatId());
            }

            return;
        }
        super.handle(updateEvent);
    }
}
