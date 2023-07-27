package ansk.development.service.event_handlers;

import ansk.development.configuration.ConfigRegistry;
import ansk.development.domain.FitnessUpdateEvent;
import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.FitnessBotSender;
import ansk.development.service.ScheduledJobsService;
import ansk.development.service.methods.MessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that resets the timeout for the fitness reminder jobs.
 *
 * @author Anton Skripin
 */
public class ResetTimerHandler extends AbstractEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetTimerHandler.class);

    protected ResetTimerHandler(EventHandler nextEventHandler) {
        super(nextEventHandler);
    }

    @Override
    public void handle(FitnessUpdateEvent updateEvent) {
        if (updateEvent.isResetTimerEvent()) {
            resetTimer(updateEvent.getChatId());
            return;
        }
        super.handle(updateEvent);
    }

    private void resetTimer(String chatId) {
        boolean isTimerReset = ScheduledJobsService.scheduledJobsService().resetFitnessReminderTimer(chatId);
        final String success = ConfigRegistry.props().forNotification().getSuccessfullyResetTimer();
        final String fail = ConfigRegistry.props().forNotification().getFailedToResetTimer();
        MessageMethod messageMethod = new MessageMethod(chatId, isTimerReset ? success : fail);
        try {
            FitnessBotSender.getSender().sendMessage(messageMethod.getMessage());
        } catch (FitnessBotOperationException e) {
            LOGGER.error("Something went wrong while resetting a timer. ChatID: {}", chatId);
        }
    }
}
