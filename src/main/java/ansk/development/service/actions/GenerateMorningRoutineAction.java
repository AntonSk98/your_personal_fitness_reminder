package ansk.development.service.actions;

import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.FitnessReminderBot;
import ansk.development.service.api.FitnessBotManager;
import ansk.development.service.generators.ExerciseGenerator;
import ansk.development.service.generators.MessageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static ansk.development.domain.ConfigurationConstants.MORNING_ROUTINE_MESSAGE;
import static ansk.development.domain.ConfigurationConstants.SEND_EXERCISE_INTERVAL_IN_MS;

public class GenerateMorningRoutineAction extends FitnessBotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateMorningRoutineAction.class);

    public GenerateMorningRoutineAction(String chatId) {
        super(chatId);
    }

    @Override
    public void doIt(FitnessReminderBot fitnessReminderBot) throws FitnessBotOperationException {
        try {
            if (!fitnessReminderBot.isEnabled(getChatId())) {
                notEnabledBotNotification(fitnessReminderBot);
                return;
            }
            MessageGenerator message = new MessageGenerator(MORNING_ROUTINE_MESSAGE, getChatId());
            ExerciseGenerator morningRoutines = new ExerciseGenerator(getChatId(), false, true);
            fitnessReminderBot.execute(message.getMessage());
            for (SendAnimation exerciseGif : morningRoutines.getExerciseGifs()) {
                fitnessReminderBot.execute(exerciseGif);
                Thread.sleep(SEND_EXERCISE_INTERVAL_IN_MS);
            }
        } catch (TelegramApiException | InterruptedException e) {
            LOGGER.error("A morning routine exercise session could not be generated due to the following exception", e);
            throw new FitnessBotOperationException(e);
        }
    }
}
