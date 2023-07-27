package ansk.development.service.api;

import ansk.development.exception.FitnessBotOperationException;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ResponseParameters;

/**
 * Provides methods for communicating with the clients of this bot.
 *
 * @author Anton Skripin
 */
public interface IFitnessBotSender {

    void sendMessage(SendMessage message) throws FitnessBotOperationException;

    void sendMessages(SendMessage... messages) throws FitnessBotOperationException;

    void sendWorkoutExercise(SendAnimation exercise) throws FitnessBotOperationException;

    void sendWorkoutAsRegisteredProcess(String chatId, SendAnimation... exercises) throws FitnessBotOperationException;

    void onTooManyRequestsMessage(String chatId, ResponseParameters responseParameters) throws FitnessBotOperationException;

}
