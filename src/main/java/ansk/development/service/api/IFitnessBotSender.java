package ansk.development.service.api;

import ansk.development.exception.FitnessBotOperationException;
import ansk.development.repository.WorkoutProcessRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Provides methods for communicating with the clients of this bot.
 *
 * @author Anton Skripin
 */
public interface IFitnessBotResponseSender {

    default void registerProcess(String chatId) {
        WorkoutProcessRepository.getRepository().addChatToRunningWorkoutProcesses(chatId);
    }

    default void unregisterProcess(String chatId) {
        WorkoutProcessRepository.getRepository().removeChatFromRunningWorkoutProcesses(chatId);
    }

    void sendMessage(SendMessage message) throws FitnessBotOperationException;
    void sendMessages(SendMessage... messages) throws FitnessBotOperationException;
    void sendWorkoutExercise(SendAnimation exercise) throws FitnessBotOperationException;
    void sendWorkout(String chatId, SendAnimation... exercises) throws FitnessBotOperationException;

}
