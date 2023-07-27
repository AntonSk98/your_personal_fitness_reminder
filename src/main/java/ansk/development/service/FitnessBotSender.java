package ansk.development.service;

import ansk.development.exception.FitnessBotOperationException;
import ansk.development.service.api.IFitnessBotResponseSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Implementation of {@link IFitnessBotResponseSender}.
 *
 * @author Anton Skripin
 */
public class FitnessBotResponseSender implements IFitnessBotResponseSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(IFitnessBotResponseSender.class);

    private static IFitnessBotResponseSender fitnessBotResponseSender;


    private FitnessBotResponseSender() {

    }

    public static IFitnessBotResponseSender getSender() {
        if (fitnessBotResponseSender == null) {
            fitnessBotResponseSender = new FitnessBotResponseSender();
        }
        return fitnessBotResponseSender;
    }

    @Override
    public void sendMessage(SendMessage message) throws FitnessBotOperationException {
        try {
            FitnessReminderBot.getFitnessBot().execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("An error occurred while sending a message: {}", message, e);
            throw new FitnessBotOperationException(e);
        }
    }

    @Override
    public void sendWorkoutExercise(SendAnimation exercise) throws FitnessBotOperationException {
        try {
            FitnessReminderBot.getFitnessBot().execute(exercise);
        } catch (TelegramApiException e) {
            LOGGER.error("An error occurred while sending an exercise: {}", exercise, e);
            throw new FitnessBotOperationException(e);
        }
    }

    @Override
    public void sendMessages(SendMessage... messages) throws FitnessBotOperationException {
        for (SendMessage sendMessage : messages) {
            sendMessage(sendMessage);
        }
    }

    @Override
    public void sendWorkout(String chatId, SendAnimation... exercises) {
        registerProcess(chatId);
        var future = TelegramFitnessExecutorService.executeAsync(() -> {
            LOGGER.info("I am about to send the workout. Thread: {}.", Thread.currentThread());
            for (SendAnimation exercise : exercises) {
                try {
                    sendWorkoutExercise(exercise);
                    Thread.sleep(10000);
                } catch (FitnessBotOperationException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    break;
                }
            }
            unregisterProcess(chatId);
        });
    }

}
